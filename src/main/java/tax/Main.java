package tax;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

import lombok.Cleanup;
import lombok.Data;
import tax.bnb.RateRetriever;
import tax.nra.model.Appendix5;
import tax.nra.model.Appendix5.TableTwoRow;
import tax.nra.model.Appendix8;
import tax.nra.model.Appendix8.StocksEnum;
import tax.nra.model.Declaration;
import tax.nra.model.Part3;

public class Main {

    public static void main(String[] args) throws IOException {

        Main.Args argv = new Args();
        JCommander cmd = JCommander.newBuilder()
                .addObject(argv)
                .columnSize(1000)
                .build();

        cmd.parse(args);

        if (argv.isHelp()) {
            cmd.usage();
            return;
        }

        @Cleanup
        RateRetriever rateRetriever = new RateRetriever();
        Appendix5 appendix5 = populateAppendix5(rateRetriever, argv.getSoldFile());
        Appendix8 appendix8 = populateAppendix8(rateRetriever, argv.getHoldingsFile());

        Declaration declaration = Declaration.builder()
                .part3(Part3.builder()
                        .issetapp5(appendix5 != null ? 1 : 0)
                        .issetapp8(appendix8 != null ? 1 : 0)
                        .build())
                .appendix5(appendix5)
                .appendix8(appendix8)
                .build();

        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

        xmlMapper.writeValue(new File("declaration.xml"), declaration);

        System.out.println(declaration);
    }

    public static Appendix8 populateAppendix8(RateRetriever rateRetriever, String fileLocation) {
        if (fileLocation == null || fileLocation.isEmpty()) {
            return null;
        }

        List<StocksEnum> stocks = SellableStocks.load(fileLocation).stream()
                .map(sellableStock ->
                        StocksEnum.builder()
                                .country("САЩ")
                                .count(sellableStock.amount)
                                .acquireDate(sellableStock.date)
                                .priceInCurrency(sellableStock.price * sellableStock.amount)
                                .price(sellableStock.price * rateRetriever.retrieveRate(
                                        sellableStock.date) * sellableStock.amount)
                                .build())
                .collect(Collectors.toList());

        return Appendix8.builder().stocks(stocks).build();
    }

    public static Appendix5 populateAppendix5(RateRetriever rateRetriever, String fileLocation) {

        if (fileLocation == null || fileLocation.isEmpty()) {
            return null;
        }
        TableTwoRow ttr = SoldStocks.load(fileLocation).stream()
                .map(ss -> {
                    Double rateAcquired = rateRetriever.retrieveRate(ss.dateAcquired);
                    Double rateSold = rateRetriever.retrieveRate(ss.dateSold);

                    double buyValue = ss.adjustedCostBasis * rateAcquired;
                    double sellValue = ss.totalProceeds * rateSold;
                    double difference = sellValue - buyValue;

                    return TableTwoRow.builder()
                            .code(508)
                            .buyValue(buyValue)
                            .sellValue(sellValue)
                            .profit(difference > 0 ? difference : 0)
                            .loss(difference < 0 ? difference : 0)
                            .build();
                })
                .reduce(TableTwoRow.builder()
                        .buyValue(0).sellValue(0).profit(0).loss(0).code(508)
                        .build(), TableTwoRow::accumulate);

        return Appendix5.builder().tableTwoRow(ttr).build();
    }

    @Data
    @Parameters(separators = "=")
    public static class Args {

        @Parameter(names = "--holdingsFilePath",
                description = "Path to file that excel file that contains stock holdings\n"
                        + "\t\tTo retrieve the excel file\n"
                        + "\t\t 1. Login to e-trade\n"
                        + "\t\t 2. Navigate to Stock plan\n"
                        + "\t\t 3. Navigate to Holdings\n"
                        + "\t\t 4. Click on View by Status\n"
                        + "\t\t 5. Download expanded")
        private String holdingsFile;

        @Parameter(names = "--soldFilePath",
                description = "Path to file that excel file that contains sold stocks\n"
                        + "\t\t To retrieve the excel file\n"
                        + "\t\t 1. Login to e-trade\n"
                        + "\t\t 2. Navigate to Stock plan\n"
                        + "\t\t 3. Navigate to My Account\n"
                        + "\t\t 4. Click on Gains & Losses\n"
                        + "\t\t 5. Select the year for taxation\n"
                        + "\t\t 6. Download expanded\n"
                        + "\t\t 7. Set the downloaded file location")
        private String soldFile;

        @Parameter(names = { "--help", "-h" }, help = true)
        private boolean help;

    }
}

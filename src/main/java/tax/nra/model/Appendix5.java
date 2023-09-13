package tax.nra.model;

import static tax.Utils.scaledBigDecimal;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

@JacksonXmlRootElement(localName = "app5")
public class Appendix5 {

    @JacksonXmlProperty(localName = "table1")
    private final Table1 table1 = new Table1();
    @JacksonXmlProperty(localName = "t1row7")
    private final BigDecimal t1row7 = scaledBigDecimal(0);

    @JacksonXmlElementWrapper(localName = "table2")
    @JacksonXmlProperty(localName = "rowenum")
    public List<TableTwoRow> tableTwoRows;

    @JacksonXmlProperty(localName = "t2row6pr")
    private final BigDecimal t2row6pr;
    @JacksonXmlProperty(localName = "t2row6ls")
    private final BigDecimal t2row6ls;
    @JacksonXmlProperty(localName = "t2row7")
    private final BigDecimal t2row7;
    @JacksonXmlProperty(localName = "part2row1")
    private final BigDecimal part2row1;
    @JacksonXmlProperty(localName = "part2row2")
    private final Object part2row2 = null;
    @JacksonXmlProperty(localName = "part2row3")
    private final Object part2row3 = null;
    @JacksonXmlProperty(localName = "part2row4")
    private final BigDecimal part2row4;
    @JacksonXmlProperty(localName = "partstatus")
    private final int partstatus = 1;


    @Builder
    public Appendix5(@NonNull @Singular List<TableTwoRow> tableTwoRows) {

        this.t2row6pr = tableTwoRows.stream()
                .map(TableTwoRow::getProfit)
                .reduce(scaledBigDecimal(0), BigDecimal::add);

        this.t2row6ls = tableTwoRows.stream()
                .map(TableTwoRow::getLoss)
                .reduce(scaledBigDecimal(0), BigDecimal::add);

        BigDecimal profit = this.t2row6pr.subtract(this.t2row6ls);

        this.t2row7 = profit.doubleValue() < 0 ? scaledBigDecimal(0) : profit;
        this.part2row1 = this.t2row7;
        this.part2row4 = this.t2row7;

        this.tableTwoRows = tableTwoRows;
    }

    @Override
    public String toString() {
        return "Appendix 5:\n" + this.tableTwoRows.stream().map(TableTwoRow::toString)
                .collect(Collectors.joining("\n"));
    }

    @Getter
    @Builder
    @JacksonXmlRootElement(localName = "table2")
    public static class TableTwoRow {
        @JacksonXmlProperty(localName = "code")
        private int code;
        @NonNull
        @JacksonXmlProperty(localName = "sellvalue")
        private BigDecimal sellValue;
        @NonNull
        @JacksonXmlProperty(localName = "buyvalue")
        private BigDecimal buyValue;
        @NonNull
        @JacksonXmlProperty(localName = "profit")
        private BigDecimal profit;
        @NonNull
        @JacksonXmlProperty(localName = "loss")
        private BigDecimal loss;

        public static TableTwoRow accumulate(TableTwoRow ttr1, TableTwoRow ttr2) {

            return TableTwoRow.builder()
                    .code(ttr1.code)
                    .sellValue(
                            ttr1.getSellValue().doubleValue() + ttr2.getSellValue().doubleValue())
                    .buyValue(ttr1.getBuyValue().doubleValue() + ttr2.getBuyValue().doubleValue())
                    .profit(ttr1.getProfit().doubleValue() + ttr2.getProfit().doubleValue())
                    .loss(ttr1.getLoss().doubleValue() + ttr2.getLoss().doubleValue())
                    .build();
        }

        @Override
        public String toString() {
            return String.format(
                    "Code: %d \t Price sold: %s \t Price acquired: %s \t Profit: %s \t Loss: %s",
                    code, sellValue.toPlainString(), buyValue.toPlainString(),
                    profit.toPlainString(), loss.toPlainString());
        }

        public static class TableTwoRowBuilder {

            public TableTwoRowBuilder sellValue(double sellValue) {
                this.sellValue = scaledBigDecimal(sellValue);
                return this;
            }

            public TableTwoRowBuilder buyValue(double buyValue) {
                this.buyValue = scaledBigDecimal(buyValue);
                return this;
            }

            public TableTwoRowBuilder profit(double profit) {
                this.profit = scaledBigDecimal(profit);
                return this;
            }

            public TableTwoRowBuilder loss(double loss) {
                this.loss = scaledBigDecimal(loss);
                return this;
            }
        }
    }

    @JacksonXmlRootElement
    private static class App5Table1RowEnum {
        @JacksonXmlProperty
        private Object code;
        @JacksonXmlProperty
        private Object sellvalue;
        @JacksonXmlProperty
        private Object buyvalue;
        @JacksonXmlProperty
        private Object posdiff;
        @JacksonXmlProperty
        private Object expense;
        @JacksonXmlProperty
        private Object partvalue;
        @JacksonXmlProperty
        private Object leasingvalue;
        @JacksonXmlProperty
        private Object value;
    }

    @JacksonXmlRootElement
    private static class Table1 {

        @JacksonXmlProperty(localName = "rowenum")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<App5Table1RowEnum> app5Table1RowEnums;

        public Table1() {
            this.app5Table1RowEnums = List.of(new App5Table1RowEnum());
        }
    }
}

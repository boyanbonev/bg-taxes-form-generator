package tax;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.SneakyThrows;

public class SellableStocks {

    private final static DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("dd-MMM-yyyy")
            .toFormatter();

    private SellableStocks() {
    }

    @SneakyThrows
    public static List<SellableStock> load(String filePath) {

        OPCPackage pkg = OPCPackage.open(new File(filePath));

        XSSFWorkbook wb = new XSSFWorkbook(pkg);

        assertNoPendingSales(wb);
        XSSFSheet sheet = wb.getSheet("Sellable");

        int rows = sheet.getPhysicalNumberOfRows();


        List<SellableStock> sellableStocks = new ArrayList<>();

        // First row is table header
        for (int i = 1; i < rows-1; i++) {
            XSSFRow row = sheet.getRow(i);

            SellableStock sellableStock = new SellableStock();

            sellableStock.date = Optional.ofNullable(row)
                    .map(r -> r.getCell(3))
                    .map(Cell::getStringCellValue)
                    .map(v -> LocalDate.parse(v, formatter))
                    .orElse(null);

            sellableStock.price = Optional.ofNullable(row)
                    .map(r -> r.getCell(27))
                    .map(Cell::getStringCellValue)
                    .map(v -> Double.parseDouble(v.substring(1)))
                    .orElse(null);

            sellableStock.amount = Optional.ofNullable(row)
                    .map(r -> r.getCell(4))
                    .map(Cell::getNumericCellValue)
                    .orElse(null);

            sellableStocks.add(sellableStock);
        }
        pkg.close();
        LocalDate now = LocalDate.now();
        return sellableStocks.stream()
                // Filter stock from this year
                .filter(sellableStock -> sellableStock.date.getYear() < now.getYear())
                .collect(Collectors.toList());
    }

    private static void assertNoPendingSales(XSSFWorkbook wb) {
        Iterator<org.apache.poi.ss.usermodel.Sheet> it = wb.sheetIterator();
        while (it.hasNext()) {
            Sheet sheet = it.next();
            if (sheet.getSheetName().contains("Pending Sale")) {
                throw new IllegalArgumentException("Failed to generate Appendix 8 due to pending sales."
                        + " Please cancel all pending sales and generate the report again.");
            }
        }
    }

    public static class SellableStock {
        public LocalDate date;
        public Double amount;
        public Double price;
    }
}

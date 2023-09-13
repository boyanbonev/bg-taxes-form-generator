package tax;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.SneakyThrows;

public class SoldStocks {

    private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("MM/dd/yyyy")
            .toFormatter();

    private SoldStocks() {
    }

    @SneakyThrows
    public static List<SoldStock> load(String filePath) {

        OPCPackage pkg = OPCPackage.open(new File(filePath));

        XSSFWorkbook wb = new XSSFWorkbook(pkg);
        XSSFSheet sheet = wb.getSheetAt(0);

        int rows = sheet.getPhysicalNumberOfRows();

        List<SoldStock> soldStocks = new ArrayList<>();

        // First 2 rows is table header
        for (int i = 2; i < rows; i++) {
            XSSFRow row = sheet.getRow(i);

            SoldStock soldStock = new SoldStock();

            soldStock.dateAcquired = Optional.ofNullable(row)
                    .map(r -> r.getCell(4))
                    .map(Cell::getStringCellValue)
                    .map(v -> LocalDate.parse(v, formatter))
                    .orElse(null);

            soldStock.adjustedCostBasis = Optional.ofNullable(row)
                    .map(r -> r.getCell(10))
                    .map(Cell::getNumericCellValue)
                    .orElse(null);

            soldStock.dateSold = Optional.ofNullable(row)
                    .map(r -> r.getCell(12))
                    .map(Cell::getStringCellValue)
                    .map(v -> LocalDate.parse(v, formatter))
                    .orElse(null);

            soldStock.totalProceeds = Optional.ofNullable(row)
                    .map(r -> r.getCell(13))
                    .map(Cell::getNumericCellValue)
                    .orElse(null);

            soldStocks.add(soldStock);
        }
        pkg.close();

        return soldStocks;
    }

    public static class SoldStock {
        public LocalDate dateAcquired;
        public Double adjustedCostBasis;
        public LocalDate dateSold;
        public Double totalProceeds;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("SoldStock: ").append("\n");
            sb.append("dateAcquired: ").append(dateAcquired).append("\n");
            sb.append("adjustedCostBasis: ").append(adjustedCostBasis).append("\n");
            sb.append("dateSold: ").append(dateSold).append("\n");
            sb.append("totalProceeds: ").append(totalProceeds).append("\n");

            return sb.toString();
        }
    }
}

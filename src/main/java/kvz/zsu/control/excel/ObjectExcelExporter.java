package kvz.zsu.control.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjectExcelExporter {

    private XSSFWorkbook workbook;
    private Sheet sheet;

    private Map<String, List<String>> objectsMap;

    public ObjectExcelExporter(Map<String, List<String>> map) throws IOException, InvalidFormatException {
        objectsMap = map;
        workbook = new XSSFWorkbook(new File("src/main/resources/excel/Додаток.xlsx"));
        sheet = workbook.getSheetAt(0);
    }

    private void writeObjectsToRows() {
        int rowCounter = 17;
        int counter = 1;

        List<String> keys = new ArrayList<>(objectsMap.keySet());

        CellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();

        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        font.setFontName("Times New Roman");

        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setFont(font);

        for (String key : keys) {
            if (objectsMap.get(key).size() == 0)
                continue;

            sheet.getRow(rowCounter).getCell(1).setCellStyle(cellStyle);
            sheet.getRow(rowCounter++).getCell(1).setCellValue(key);

            for (int j = 0; j < objectsMap.get(key).size(); j++) {
                sheet.getRow(rowCounter).getCell(0).setCellValue(counter++);
                sheet.getRow(rowCounter).getCell(2).setCellValue("компл.");
                sheet.getRow(rowCounter++).getCell(1).setCellValue(objectsMap.get(key).get(j));
            }
        }
    }


    public void export(HttpServletResponse response) {
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            writeObjectsToRows();
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

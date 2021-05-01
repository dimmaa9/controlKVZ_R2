package kvz.zsu.control.excel;

import kvz.zsu.control.models.Object;
import kvz.zsu.control.models.Thing;
import kvz.zsu.control.models.Unit;
import kvz.zsu.control.services.ObjectService;
import kvz.zsu.control.services.ThingService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ObjectExcelExporterImporter {


    private XSSFWorkbook workbook;
    private Sheet sheet;

    private Map<String, List<String>> objectsMap;

    public ObjectExcelExporterImporter(Map<String, List<String>> map) throws IOException, InvalidFormatException {
        objectsMap = map;
        workbook = new XSSFWorkbook(new File("src/main/resources/excel/dodatok_01-04-2021_18_28_30.xlsx"));
        sheet = workbook.getSheetAt(0);
    }

    public ObjectExcelExporterImporter(MultipartFile file) throws IOException {
        workbook = new XSSFWorkbook(file.getInputStream());
        sheet = workbook.getSheetAt(0);
    }



    private void writeObjectsToRows() {
        int rowCounter = 16;
        int counter = 1;

        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

        sheet.getRow(7).getCell(2).setCellValue("станом на " + format.format(new Date()));

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

//    public List<Thing> importToDB(Unit unit , ObjectService service) throws IOException {
//
//
//        return thingList;
//    }
}

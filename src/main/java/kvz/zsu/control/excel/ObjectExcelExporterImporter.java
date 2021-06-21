package kvz.zsu.control.excel;

import kvz.zsu.control.models.Object;
import kvz.zsu.control.models.Thing;
import kvz.zsu.control.models.Unit;
import kvz.zsu.control.services.ObjectService;
import kvz.zsu.control.services.ThingService;
import kvz.zsu.control.services.UnitService;
import lombok.SneakyThrows;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObjectExcelExporterImporter {


    private XSSFWorkbook workbook;
    private Sheet sheet;

    private MultipartFile file;
    private long id;

    private Map<String, List<String>> objectsMap;

    private ObjectService objectService;
    private UnitService unitService;
    private ThingService thingService;

    public ObjectExcelExporterImporter(Map<String, List<String>> map) throws IOException, InvalidFormatException {
        objectsMap = map;
        workbook = new XSSFWorkbook(new File("src/main/resources/newExcel/1.xlsx"));
        sheet = workbook.getSheetAt(0);
    }

    public ObjectExcelExporterImporter(MultipartFile file, long id,
                                       ObjectService objectService,
                                       UnitService unitService,
                                       ThingService thingService) {
        this.file = file;
        this.id = id;
        this.objectService = objectService;
        this.unitService = unitService;
        this.thingService = thingService;
    }

    @SneakyThrows
    public void importExcel() {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            //thingService.deleteByUnit(unitService.findById(id));

            int row = 7;

            //Ячейка наявність
            int rowCell = 7;

            Map<String, Object> stringObjectMap = new HashMap<>();

            List<Object> objectList = objectService.findAll();
            List<String> objectsName = new ArrayList<>();


            for (var item : objectList) {
                stringObjectMap.put(item.getObjectName(), item);
            }


            while (true) {
                Cell cell = sheet.getRow(row).getCell(0);


                if (!getCellText(cell).equals("")) {
                    objectsName.add(getCellText(cell));
                    row++;
                } else break;
            }
            System.out.println(objectsName.toString());


            String regex = "\\d{2}\\.\\d{2}\\.\\d{4}";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(sheet.getRow(3).getCell(2).getStringCellValue());


            LocalDate localDate = null;
            if (matcher.find()) {
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                localDate = LocalDate.parse(matcher.group(), dateFormat);
            }


            for (String s : objectsName) {

                if (stringObjectMap.containsKey(s)) {
                    Thing thing = new Thing();

                    thing.setUnit(unitService.findById(id));
                    thing.setObject(stringObjectMap.get(s));

                    System.out.println(localDate);
                    if (localDate != null) {
                        thing.setLocalDate(LocalDate.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth()));
                    } else {
                        thing.setLocalDate(LocalDate.now());
                    }

                    //Не вистаяає
                    Cell cellNotEnough = sheet.getRow(rowCell).getCell(5);
                    System.out.println(cellNotEnough.getNumericCellValue());
                    //Надлишок
                    Cell cellExcess = sheet.getRow(rowCell).getCell(6);
                    System.out.println(cellExcess.getNumericCellValue());
                    //Наявність
                    Cell cellHave = sheet.getRow(rowCell).getCell(4);
                    System.out.println(cellHave.getNumericCellValue());

                    if (cellHave.getCellType() == CellType.BLANK && cellExcess.getCellType() == CellType.BLANK &&
                            cellNotEnough.getCellType() == CellType.BLANK) {
                        continue;
                    } else {
                        if ((int) cellHave.getNumericCellValue() == 0 && (int) cellExcess.getNumericCellValue() == 0 &&
                                (int) cellNotEnough.getNumericCellValue() == 0) {
                            continue;
                        }

                        thing.setGeneralHave((int) cellHave.getNumericCellValue());

                        if ((cellNotEnough.getCellType() == CellType.BLANK && cellExcess.getCellType() == CellType.BLANK) ||
                                ((int)cellNotEnough.getNumericCellValue() == 0 && (int)cellExcess.getNumericCellValue() == 0)) {

                            thing.setGeneralNeed((int) cellHave.getNumericCellValue());

                        } else if (cellNotEnough.getCellType() == CellType.BLANK || (int) cellNotEnough.getNumericCellValue() == 0) {

                            thing.setGeneralNeed(((int) cellHave.getNumericCellValue()) - ((int) cellExcess.getNumericCellValue()));

                        } else if (cellExcess.getCellType() == CellType.BLANK || (int)cellExcess.getNumericCellValue() == 0) {

                            thing.setGeneralNeed(((int) cellHave.getNumericCellValue()) + ((int) cellNotEnough.getNumericCellValue()));

                        }
                        System.out.println(thing.getLocalDate());
                        thingService.save(thing);
                    }
                    rowCell++;
                }
            }
            workbook.close();
    }

    private void writeObjectsToRows() {
        int rowCounter = 7;
        int counter = 1;

        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

        sheet.getRow(3).getCell(2).setCellValue("станом на " + format.format(new Date()));

        List<String> keys = new ArrayList<>(objectsMap.keySet());

        CellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();

        font.setFontHeightInPoints((short) 12);
//        font.setBold(true);
        font.setFontName("Times New Roman");

//        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setFont(font);
        cellStyle.setWrapText(true);

        for (String key : keys) {
            if (objectsMap.get(key).size() == 0)
                continue;



            for (int j = 0; j < objectsMap.get(key).size(); j++) {
                sheet.getRow(rowCounter).getCell(0).setCellStyle(cellStyle);
                sheet.getRow(rowCounter++).getCell(0).setCellValue(objectsMap.get(key).get(j));
            }
        }
    }

    private String getCellText(Cell cell) {
        String res = "";

        switch (cell.getCellType()) {
            case STRING:
                res = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    res = cell.getDateCellValue().toString();
                } else {
                    res = Double.toString(cell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                res = Boolean.toString(cell.getBooleanCellValue());
                break;
            case FORMULA:
                res = cell.getCellFormula().toString();
                break;

            default:
                break;
        }

        return res;
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

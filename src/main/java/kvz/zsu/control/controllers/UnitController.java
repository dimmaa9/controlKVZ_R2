package kvz.zsu.control.controllers;

import kvz.zsu.control.excel.ObjectExcelExporterImporter;
import kvz.zsu.control.models.*;
import kvz.zsu.control.models.Object;
import kvz.zsu.control.services.*;
import lombok.AllArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@AllArgsConstructor
@RequestMapping("/units")
public class UnitController {
    private final UserService userService;
    private final UnitService unitService;
    private final ThingService thingService;
    private final ObjectService objectService;
    private final TypeService typeService;

    @GetMapping
    public String getUnitsTable() {
        return "table-units";
    }

    @PostMapping("/{id}")
    public String mapReapExcelDatatoDB(@RequestParam("file") MultipartFile reapExcelDataFile,
                                       @PathVariable("id") Long id) {

        try (XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            thingService.deleteByUnit(unitService.findById(id));

            int row = 16;
            int rowCellNeedAndHave = 16;

            Map<String, Object> stringObjectMap = new HashMap<>();

            List<Object> objectList = objectService.findAll();
            List<String> objectsName = new ArrayList<>();


            for (var item : objectList) {
                stringObjectMap.put(item.getObjectName(), item);
            }


            while (true) {
                Cell cell = sheet.getRow(row).getCell(1);


                if (!getCellText(cell).equals("")) {
                    objectsName.add(getCellText(cell));
                    row++;
                } else break;
            }
            System.out.println(objectsName.toString());

            for (String s : objectsName) {

                if (stringObjectMap.containsKey(s)) {
                    Thing thing = new Thing();
                    Cell cellNeed = sheet.getRow(rowCellNeedAndHave).getCell(3);
                    Cell cellHave = sheet.getRow(rowCellNeedAndHave).getCell(2);

                    thing.setUnit(unitService.findById(id));
                    thing.setObject(stringObjectMap.get(s));

                    if (cellNeed.getCellType() != CellType.STRING || cellHave.getCellType() != CellType.STRING){
                        thing.setGeneralNeed((int) cellNeed.getNumericCellValue());
                        thing.setGeneralHave((int) cellHave.getNumericCellValue());
                    }
                    else {
                        thing.setGeneralHave(0);
                        thing.setGeneralNeed(0);
                    }
                    System.out.println(thing.getObject().getObjectName());

                    thingService.save(thing);
                }

                rowCellNeedAndHave++;

            }
        } catch (Exception exception) {
            return "redirect:/units";
        }


        return "redirect:/units";

    }


//    @GetMapping("/generate/{id}")
//    public void exportToExcel (HttpServletResponse response,
//                               @PathVariable("id") Long id) throws IOException, InvalidFormatException {
//        DateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
//
//        String fileName = "attachement; filename=" + "dodatok_" + dataFormat.format(new Date()) + ".xlsx";
//
//        response.setContentType("application/octet-stream");
//        response.setHeader("Content-Disposition", fileName);
//
//        Map<String, List<String>> map = new HashMap<>();
//        List<Type> typeList = typeService.findAll();
//        System.out.println(typeList.toString());
//        for (Type type : typeList) {
//
//            List<String> list = new ArrayList<>();
//            var objectList = objectService.findAll();
//
//            for (Object object : objectList) {
//                if (object.getType().equals(type)) {
//                    list.add(object.getObjectName());
//                }
//            }
//
//            map.put(type.getType(), list);
//        }
//
//        ObjectExcelExporterImporter excelExporter = new ObjectExcelExporterImporter(map);
//        excelExporter.export(response);
//    }

//    @GetMapping("/edit/{id}")
//    public ModelAndView getEditPage(@PathVariable("id") Long id) {
//        ModelAndView mav = new ModelAndView("edit-units");
//        mav.addObject("unitEdit", unitService.findById(id));
//        return mav;
//    }
//
//    @PostMapping(value = "/create", params = {"saveUnit"})
//    public String save(Unit unit) {
//        unitService.save(unit);
//        return "redirect:/units";
//    }
//
//    @PostMapping(value = "/edit/{id}", params = {"saveUnit"})
//    public String saveEditUnit(Unit unit) {
//        return save(unit);
//    }

    @ModelAttribute("user")
    public User getUser(@AuthenticationPrincipal User user) {
        return userService.findById(user.getId());
    }

    @ModelAttribute("units")
    public List<Unit> getUnits() {
        return unitService.findAll();
    }

    public String getCellText(Cell cell) {
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
}

package kvz.zsu.control.controllers;

import kvz.zsu.control.excel.ObjectExcelExporterImporter;
import kvz.zsu.control.models.Object;
import kvz.zsu.control.models.Thing;
import kvz.zsu.control.models.Unit;
import kvz.zsu.control.models.User;
import kvz.zsu.control.services.ObjectService;
import kvz.zsu.control.services.ThingService;
import kvz.zsu.control.services.UnitService;
import kvz.zsu.control.services.UserService;
import lombok.AllArgsConstructor;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
@RequestMapping("/units")
public class UnitController {
    private final UserService userService;
    private final UnitService unitService;
    private final ThingService thingService;
    private final ObjectService objectService;

    @GetMapping
    public String getUnitsTable() {
        return "table-units";
    }

    @PostMapping("/{id}")
    public String mapReapExcelDatatoDB(@RequestParam("file") MultipartFile reapExcelDataFile,
                                       @PathVariable("id") Long id) {

        try (XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            int row = 16;

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
                    thing.setUnit(unitService.findById(id));
                    thing.setObject(stringObjectMap.get(s));

                    thing.setGeneralHave(1);
                    thing.setGeneralNeed(12);


                    thingService.save(thing);
                }

            }
        } catch (Exception exception) {
            return "redirect:/units";
        }


        return "redirect:/units";

    }


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

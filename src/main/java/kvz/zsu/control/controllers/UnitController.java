package kvz.zsu.control.controllers;

import kvz.zsu.control.models.*;
import kvz.zsu.control.models.Object;
import kvz.zsu.control.services.*;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

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
    private final ScopeService scopeService;

    @GetMapping
    public String getUnitsTable() {
        return "tables/table-units";
    }

    @GetMapping("/create")
    public String createUnit (Model model) {
        model.addAttribute("unit", new Unit());

        return "create/create-unit";
    }

    @PostMapping("/{id}")
    public String mapReapExcelDataToDB(@RequestParam("file") MultipartFile reapExcelDataFile,
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

                    if (cellNeed.getCellType() != CellType.BLANK && cellHave.getCellType() != CellType.BLANK &&
                    cellNeed.getCellType() != CellType.STRING && cellHave.getCellType() != CellType.STRING){
                        thing.setGeneralNeed((int) cellNeed.getNumericCellValue());
                        thing.setGeneralHave((int) cellHave.getNumericCellValue());
                        thingService.save(thing);
                    }
                    System.out.println(thing.getObject().getObjectName());


                }

                rowCellNeedAndHave++;

            }
        } catch (Exception exception) {
            return "redirect:/units";
        }

        return "redirect:/units";

    }

    @GetMapping("/table/{id}")
    public ModelAndView getTableUnit(@PathVariable long id) {
        ModelAndView mav = new ModelAndView("tables/table-unit-things");
        mav.addObject("unit", unitService.findById(id));
        return mav;
    }

    @GetMapping("/{id}/create")
    public String createThing (@PathVariable long id, Model model) {
        Thing thing = new Thing();
        thing.setUnit(unitService.findById(id));
        model.addAttribute("thing", thing);

        return "create/create-thing";
    }

    @PostMapping("/save")
    public String saveThing(Thing thing){
        thingService.save(thing);
        return "redirect:/units/table/" + thing.getUnit().getId().toString();
    }

    @GetMapping("/delete/{id}")
    public String deleteThing(@PathVariable long id) {
        String idUnit = thingService.findById(id).getUnit().getId().toString();
        thingService.deleteById(id);

        return "redirect:/units/table/" + idUnit;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editThing(@PathVariable long id) {
        ModelAndView mav = new ModelAndView("edit/edit-thing");
        mav.addObject("thing", thingService.findById(id));

        return mav;
    }

    @PostMapping("/unit/saveNull")
    public String saveUnitNull(Unit unit){
        unit.setParentUnit(null);
        unitService.save(unit);
        return "redirect:/units";
    }

    @PostMapping("/unit/save")
    public String saveUnit(Unit unit){
        unitService.save(unit);
        return "redirect:/units";
    }

    @GetMapping("/unit/edit/{id}")
    public ModelAndView editUnit(@PathVariable long id){
        ModelAndView mav = new ModelAndView("edit/edit-unit");
        mav.addObject("unit", unitService.findById(id));
        return mav;
    }

    @GetMapping("/unit/delete/{id}")
    public String deleteUnit(@PathVariable long id){
        Unit unit = unitService.findById(id);
        if(unit.getThingList().size() == 0 || unit.getThingList() == null || unit.getUnits() == null || unit.getUnits().size() == 0){
            unitService.deleteById(id);
        }
        return "redirect:/units";
    }

    @GetMapping("/{id}/deleteAll")
    public String deleteAllThings(@PathVariable long id) {
        thingService.deleteByUnit(unitService.findById(id));
        return "redirect:/units/table/" + id;
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

    @ModelAttribute("scopes")
    public List<Scope> scopeList() {
        return scopeService.findAll();
    }

    @ModelAttribute("types")
    public List<Type> typeList() {
        return typeService.findAll();
    }

    @ModelAttribute("objects")
    public List<Object> objectList() {
        return objectService.findAll();
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

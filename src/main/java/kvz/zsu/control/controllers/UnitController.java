package kvz.zsu.control.controllers;

import kvz.zsu.control.excel.ObjectExcelExporterImporter;
import kvz.zsu.control.models.*;
import kvz.zsu.control.models.Object;
import kvz.zsu.control.services.*;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/units")
@AllArgsConstructor
public class UnitController {

    private final UserService userService;
    private final UnitService unitService;
    private final ThingService thingService;
    private final ObjectService objectService;
    private final TypeService typeService;
    private final ScopeService scopeService;


    @GetMapping
    public String getUnitsTable(Model model) {
        List<Unit> list = unitService.findAll().stream()
                .filter(x -> x.getParentUnit() == null)
                .collect(Collectors.toList());
        model.addAttribute("unitsList", list);
        model.addAttribute("unitParentName", "Підрозділи");
        return "tables/table-units";
    }

    @GetMapping("/unit/{id}")
    public String getUnitsTableFromParentUnit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("unitsList", unitService.findById(id).getUnits());
        model.addAttribute("unitParentName", "Підрозділи (" + unitService.findById(id).getNameUnit() + ")");
        return "tables/table-units";
    }

    @GetMapping("/create")
    public String createUnit(Model model) {
        model.addAttribute("unit", new Unit());

        return "create/create-unit";
    }

    @PostMapping("/{id}")
    public String mapReapExcelDataToDB(@RequestParam("file") MultipartFile reapExcelDataFile,
                                       @PathVariable("id") Long id) {

        ObjectExcelExporterImporter objectExcelExporterImporter = new ObjectExcelExporterImporter(reapExcelDataFile, id,
                objectService, unitService, thingService);

        try {
            objectExcelExporterImporter.importExcel();
        } catch (Exception e) {
            return "redirect:/units";
        }

        return "redirect:/units";

    }

    @GetMapping("/table/{id}")
    public ModelAndView getTableUnit(@PathVariable long id) {
        ModelAndView mav = new ModelAndView("tables/table-unit-things");

        Unit unit = unitService.findById(id);

        //Уникальные элементи
        List<LocalDate> dateListAll = new ArrayList<>();
        unit.getThingList().forEach(x -> {
            dateListAll.add(x.getLocalDate());
        });
        Set<LocalDate> uniqKeys = new TreeSet<LocalDate>();
        uniqKeys.addAll(dateListAll);

        mav.addObject("unit", unit);
        mav.addObject("dateList", uniqKeys);
        return mav;
    }

    @GetMapping("/table/{id}/all")
    public @ResponseBody
    Map<Long, String> allThingsFromUnit(@PathVariable long id) {
        Map<Long, String> thingMap = new HashMap<>();
        List<Thing> list = unitService.findById(id).getThingList();
        for (var item : list) {
            thingMap.put(item.getId(),
                    item.getObject().getObjectName() + "|||" +
                            item.getGeneralNeed() + " " +
                            item.getGeneralHave() + " " +
                            item.getLocalDate().toString());
        }
        return thingMap;
    }

    @GetMapping("/table/{id}/{date}")
    public @ResponseBody
    Map<Long, String> findDateThingsFromUnit(@PathVariable long id,
                                             @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        Map<Long, String> thingMap = new HashMap<>();
        List<Thing> list = unitService.findById(id).getThingList()
                .stream()
                .filter(x -> x.getLocalDate().equals(date))
                .collect(Collectors.toList());
        for (var item : list) {
            thingMap.put(item.getId(),
                    item.getObject().getObjectName() + "|||" +
                            item.getGeneralNeed() + " " +
                            item.getGeneralHave());
        }
        return thingMap;
    }

    @GetMapping("/{id}/create")
    public String createThing(@PathVariable long id, Model model) {
        Thing thing = new Thing();
        thing.setUnit(unitService.findById(id));
        thing.setLocalDate(LocalDate.now());

        model.addAttribute("thing", thing);

        return "create/create-thing";
    }

    @PostMapping("/save")
    public String saveThing(Thing thing) {

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
    public String saveUnitNull(Unit unit) {
        unit.setParentUnit(null);
        unitService.save(unit);
        return "redirect:/units";
    }

    @PostMapping("/unit/save")
    public String saveUnit(Unit unit) {
        System.out.println(unit.getParentUnit().getNameUnit());
        unitService.save(unit);
        return "redirect:/units";
    }

    @GetMapping("/unit/edit/{id}")
    public ModelAndView editUnit(@PathVariable long id) {
        ModelAndView mav = new ModelAndView("edit/edit-unit");
        mav.addObject("unit", unitService.findById(id));
        return mav;
    }

    @GetMapping("/unit/delete/{id}")
    public String deleteUnit(@PathVariable long id) {
        Unit unit = unitService.findById(id);
        if (unit.getThingList().size() == 0 || unit.getThingList() == null || unit.getUnits() == null || unit.getUnits().size() == 0) {
            unitService.deleteById(id);
        }
        return "redirect:/units";
    }

    @GetMapping("/{id}/deleteAll")
    public String deleteAllThings(@PathVariable long id) {
        thingService.deleteByUnit(unitService.findById(id));
        return "redirect:/units/table/" + id;
    }

    @PostMapping("/table/{id}")
    @SneakyThrows
    public String avatar(@RequestParam("file") MultipartFile file,
                         @PathVariable long id) {
        Unit unit = unitService.findById(id);
        if (file != null) {

            File uploadDir = new File("src/main/resources/static/avatars");

            if (!uploadDir.exists())
                uploadDir.mkdir();

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            String absolutePath = new File("src/main/resources/static/avatars").getAbsolutePath();

            file.transferTo(new File(absolutePath + "\\avatars" + resultFilename));

            unit.setFilename("avatars" + resultFilename);
        }

        unitService.save(unit);

        return "redirect:/units/table/" + id;
    }

    @GetMapping("/delete-avatar/{id}")
    public String deleteAvatar(@PathVariable long id) {
        Unit byId = unitService.findById(id);

        File ava = new File("src/main/resources/static/avatars/" + byId.getFilename());
        if (ava.exists()) {
            ava.delete();
        }

        byId.setFilename(null);
        unitService.save(byId);

        return "redirect:/units/table/" + id;
    }


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


}

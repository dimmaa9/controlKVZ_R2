package kvz.zsu.control.controllers;

import kvz.zsu.control.models.*;
import kvz.zsu.control.models.Object;
import kvz.zsu.control.services.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/table")
@AllArgsConstructor
public class TableController {

    private final UserService userService;
    private final ThingService thingService;
    private final ScopeService scopeService;
    private final TypeService typeService;
    private final ObjectService objectService;
    private final UnitService unitService;

    @GetMapping
    public String getTable(Model model) {
        return "tables";
    }

    @GetMapping(value = "/units")
    @ResponseBody
    public Map<Long, String> getUnits(@RequestParam(value = "_units_") String arr) {
        if (arr.equals("") || arr.equals("undefined")) {
            return new HashMap<>();
        }

        List<Integer> list = new ArrayList<>();
        for (var item : arr.split(",")) {
            list.add(Integer.parseInt(item));
        }
        return unitService.findByAllId(list);
    }

    @GetMapping("/scopes")
    @ResponseBody
    public Map<Long, String> getTypes(@RequestParam(value = "_scope_") String arr) {
        if (arr.equals(""))
            return new HashMap<>();

        List<Integer> list = new ArrayList<>();
        for (var item : arr.split(","))
            list.add(Integer.parseInt(item));

        return scopeService.findByAllId(list);
    }

    @GetMapping("/types")
    @ResponseBody
    public Map<Long, String> getObjects(@RequestParam(value = "_type_") String arr) {
        if (arr.equals(""))
            return new HashMap<>();

        List<Integer> list = new ArrayList<>();
        for (var item : arr.split(","))
            list.add(Integer.parseInt(item));

        return typeService.findByAllId(list);
    }

    @GetMapping("/unit")
    @ResponseBody
    public Map<String, String> getUnitsData(@RequestParam(value = "unit") String unitStr,
                                            @RequestParam(value = "valueScope") String valueScope,
                                            @RequestParam(value = "valueType") String valueType,
                                            @RequestParam(value = "valueObject") String valueObject) {
        Map<String, String> map = new HashMap<>();
        if (valueScope.equals("null") && valueType.equals("null") && valueObject.equals("null")){
            if (unitStr.equals(""))
                return new HashMap<>();

            List<Long> list = new ArrayList<>();
            for (var item : unitStr.split(",")){
                list.add(Long.parseLong(item));
            }

            List<Unit> unitList = new ArrayList<>();
            for(var item : list){
                unitList.add(unitService.findById(item));
            }


            // unit name, % toString

            for (var item : unitList){
                map.put(item.getNameUnit(), thingService.percentUnit(item).toString());
            }
            return map;
        }else {
            return map;
        }

        //var test = thingService.percentUnitByObject(unit, objectService.findById(1L));
        //System.out.println(test);

        //var test2 = thingService.percentUnitByObjectList(unit, Arrays.asList(objectService.findById(1L), objectService.findById(2L)));
        //System.out.println(test2);
    }

    @GetMapping("/filter")
    @ResponseBody
    public Map<String, String> getItemData(@RequestParam(value = "_idScope_") String idScope) {
        return new HashMap<>();
    }

    //ModelAttribute
    @ModelAttribute("things")
    public List<Thing> getThings() {
        return thingService.findAll();
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

    @ModelAttribute("unitsParentNull")
    public List<Unit> unitList() {
        return unitService.findAll().stream().filter(x -> x.getParentUnit() == null).collect(Collectors.toList());
    }

    @ModelAttribute("user")
    public User getUser(@AuthenticationPrincipal User user) {
        return userService.findById(user.getId());
    }
}

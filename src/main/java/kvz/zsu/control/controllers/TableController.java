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
        return "tables/tables";
    }

    @GetMapping(value = "/units")
    public @ResponseBody Map<Long, String> getUnits(@RequestParam(value = "_units_") String arr) {
        if (arr.equals("") || arr.equals("undefined"))
            return new HashMap<>();

        List<Integer> list = new ArrayList<>();
        for (var item : arr.split(","))
            list.add(Integer.parseInt(item));

        return unitService.findByAllId(list);
    }

    @GetMapping("/scopes")
    public @ResponseBody Map<Long, String> getTypes(@RequestParam(value = "_scope_") String arr) {
        if (arr.equals(""))
            return new HashMap<>();

        List<Integer> list = new ArrayList<>();
        for (var item : arr.split(","))
            list.add(Integer.parseInt(item));

        return scopeService.findByAllId(list);
    }

    @GetMapping("/types")
    public @ResponseBody Map<Long, String> getObjects(@RequestParam(value = "_type_") String arr) {
        if (arr.equals(""))
            return new HashMap<>();

        List<Integer> list = new ArrayList<>();
        for (var item : arr.split(","))
            list.add(Integer.parseInt(item));

        return typeService.findByAllId(list);
    }

    @GetMapping("/filterAll")
    public @ResponseBody List<List<String>> getAllData(@RequestParam(value = "unit") String unitStr,
                                                       @RequestParam(value = "valueScope") String valueScope,
                                                       @RequestParam(value = "valueType") String valueType,
                                                       @RequestParam(value = "valueObject") String valueObject) {
        boolean flag = true;
        List<Unit> unitList = new ArrayList<>();
        for (var item : unitStr.split(",")){
            unitList.add(unitService.findById(Long.parseLong(item)));
        }

        List<List<String>> lists = new ArrayList<>();

        List<String> s0 = new ArrayList<>();
        s0.add("");
        for (var unit : unitList){
            s0.add(unit.getNameUnit());
        }
        lists.add(s0);

        if(valueObject.equals("null") && valueType.equals("null")){

            List<Scope> scopeList = new ArrayList<>();
            for (var item : valueScope.split(",")){
                scopeList.add(scopeService.findById(Long.parseLong(item)));
            }

            if(scopeList.size() <= unitList.size()){
                flag = false;
            }

            Map<Scope, List<Object>> scopeMap = new HashMap<>();
            for (var item : scopeList){
                List<Type> types = item.getTypeList();
                List<Object> objects = new ArrayList<>();
                for (var t : types){
                    objects.addAll(t.getObjectList());
                }
                scopeMap.put(item, objects);
            }

            for (Scope scope : scopeList) {
                List<String> listOutput = new ArrayList<>();
                listOutput.add(scope.getScope());
                for (Unit unit : unitList) {
                    listOutput.add(thingService.percentUnitByObjectList(
                            unit, scopeMap.get(scope)).toString());
                }
                lists.add(listOutput);
            }
        }else if(valueObject.equals("null")){
            List<Type> typeList = new ArrayList<>();
            for (var item : valueType.split(",")){
                typeList.add(typeService.findById(Long.parseLong(item)));
            }

            if(typeList.size() <= unitList.size()){
                flag = false;
            }

            Map<Type, List<Object>> typeMap = new HashMap<>();
            for (var item : typeList){
                typeMap.put(item, item.getObjectList());
            }

            for (Type type : typeList) {
                List<String> listOutput = new ArrayList<>();
                listOutput.add(type.getTypeName());
                for (Unit unit : unitList) {
                    listOutput.add(thingService.percentUnitByObjectList(
                            unit, typeMap.get(type)).toString());
                }
                lists.add(listOutput);
            }

        }else{
            List<Object> objectList = new ArrayList<>();
            for (var item : valueObject.split(",")){
                objectList.add(objectService.findById(Long.parseLong(item)));
            }

            if(objectList.size() <= unitList.size()){
                flag = false;
            }

            for (Object object : objectList) {
                List<String> listOutput = new ArrayList<>();
                listOutput.add(object.getObjectName());
                for (Unit unit : unitList) {
                    listOutput.add(thingService.percentUnitByObject(
                            unit, object).toString());
                }
                lists.add(listOutput);
            }
        }

        if(!flag){
            List<List<String>> lists1 = new ArrayList<>();
            String[][] array = new String[lists.get(0).size()][lists.size()];
            for (int i = 0; i < lists.size(); i++){
                for(int j = 0; j < lists.get(i).size(); j++){
                    array[j][i] = lists.get(i).get(j);
                }
            }

            for (String[] strings : array) {
                List<String> list = new ArrayList<>();
                Collections.addAll(list, strings);
                lists1.add(list);
            }
            return lists1;
        }else {
            return lists;
        }
    }


    @GetMapping("/filterUnit")
    public @ResponseBody Map<String, String> getUnitsData(@RequestParam(value = "unit") String unitStr){
        List<Unit> unitList = new ArrayList<>();
        for (var item : unitStr.split(",")){
            unitList.add(unitService.findById(Long.parseLong(item)));
        }

        //nameUnit, %
        Map<String, String> stringMap = new HashMap<>();
        for (var item : unitList)
            stringMap.put(item.getNameUnit(), thingService.percentUnit(item).toString());

        return stringMap;
    }

    @GetMapping("/filterObject")
    public @ResponseBody Map<String, String> getObjectData(@RequestParam(value = "valueScope") String valueScope,
                                                           @RequestParam(value = "valueType") String valueType,
                                                           @RequestParam(value = "valueObject") String valueObject){
        Map<String, String> stringMap = new HashMap<>();
        List<Unit> unitsAll = unitService.findAll();

        if(valueObject.equals("null") && valueType.equals("null")){
            List<Scope> scopeList = new ArrayList<>();
            for (var item : valueScope.split(",")){
                scopeList.add(scopeService.findById(Long.parseLong(item)));
            }
            Map<Scope, List<Object>> scopeMap = new HashMap<>();
            for (var item : scopeList){
                List<Type> types = item.getTypeList();
                List<Object> objects = new ArrayList<>();
                for (var t : types){
                    objects.addAll(t.getObjectList());
                }
                scopeMap.put(item, objects);
            }

            for (var item : scopeList){
                stringMap.put(item.getScope(), thingService.percentUnitListByObjectList(unitsAll, scopeMap.get(item)).toString());
            }
        }else if(valueObject.equals("null")){
            List<Type> typeList = new ArrayList<>();
            for (var item : valueType.split(",")){
                typeList.add(typeService.findById(Long.parseLong(item)));
            }

            for (var item : typeList){
                stringMap.put(item.getTypeName(), thingService.percentUnitListByObjectList(unitsAll, item.getObjectList()).toString());
            }

        }else{
            List<Object> objectList = new ArrayList<>();
            for (var item : valueObject.split(",")){
                objectList.add(objectService.findById(Long.parseLong(item)));
            }

            for (var item : objectList){
                List<Object> object0 = new ArrayList<>();
                object0.add(item);
                stringMap.put(item.getObjectName(), thingService.percentUnitListByObjectList(unitsAll, object0).toString());
            }
        }

        return stringMap;
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

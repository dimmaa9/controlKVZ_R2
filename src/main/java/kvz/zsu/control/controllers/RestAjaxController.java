package kvz.zsu.control.controllers;

import kvz.zsu.control.models.Object;
import kvz.zsu.control.models.Thing;
import kvz.zsu.control.models.Unit;
import kvz.zsu.control.services.ObjectService;
import kvz.zsu.control.services.ScopeService;
import kvz.zsu.control.services.ThingService;
import kvz.zsu.control.services.UnitService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class RestAjaxController {

    private final ScopeService scopeService;
    private final ThingService thingService;
    private final UnitService unitService;
    private final ObjectService objectService;

    @GetMapping("/getScopsData")
    public Map<String, String> getScopsName() {
        Map<String, String> scopeData = new HashMap<>();

        for (var item : scopeService.findAll()) {
            List<Object> objects = new ArrayList<>();
            for (var i : item.getTypeList()){
                objects.addAll(i.getObjectList());
            }
            List<Thing> things = new ArrayList<>();
            for (var i : objects){
                things.addAll(i.getThingList());
            }
            Integer countOutput = 0;
            for (var i : things){
                try {
                    countOutput += i.getGeneralHave();
                }
                catch (NullPointerException ex) {
                    countOutput = 0;
                }

            }

            scopeData.put(item.getScope(), countOutput.toString());
        }

        return scopeData;
    }

    @GetMapping("/getPercent")
    public List<String> getPercentInfo(){
        Integer intUk = 0;

        try {
            intUk = thingService.percentUnitListByObjectList(unitService.findAll(), objectService.findAll());
        }
        catch (NullPointerException ex) {
            intUk = 0;
        }

        int intNoyUk = 100 - intUk;
        List<String> listOutput = new ArrayList<>();
        listOutput.add(intUk.toString());
        listOutput.add(String.valueOf(intNoyUk));
        return listOutput;
    }

    @GetMapping("/getNullUnitValue")
    public Map<String, Integer> getNullUnitValue() {
        Map<String, Integer> map = new HashMap<>();
        List<Unit> unitList = unitService.findAll().stream().filter(x -> x.getParentUnit() == null).collect(Collectors.toList());
        List<Object> objectList = objectService.findAll();

        for (var item : unitList){
            try {
                map.put(item.getNameUnit(), thingService.percentUnitByObjectList(item, objectList));
            }
            catch (NullPointerException ex){
                map.put(item.getNameUnit(), 0);
            }

        }
        return map;
    }

}

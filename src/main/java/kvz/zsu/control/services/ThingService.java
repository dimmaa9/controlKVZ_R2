package kvz.zsu.control.services;

import kvz.zsu.control.models.Object;
import kvz.zsu.control.models.Thing;
import kvz.zsu.control.models.Unit;
import kvz.zsu.control.repositories.ThingRepo;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ThingService {

    private final ThingRepo thingRepo;


    public ThingService(ThingRepo thingRepo) {
        this.thingRepo = thingRepo;
    }

    public List<Thing> findAll() {
        return thingRepo.findAll();
    }

    public void save(Thing thing) {
        thingRepo.save(thing);
    }


    public Thing findById(Long id) {
        return thingRepo.findById(id).get();
    }

    public void deleteById(Long id) {
        thingRepo.deleteById(id);
    }

    public List<Thing> findAllByIdUnit(Long id) {
        return thingRepo.findAll().stream().filter(x -> x.getUnit().getId().equals(id)).collect(Collectors.toList());
    }

    public void deleteByUnit(Unit unit) {
        unit.getThingList().forEach(x -> thingRepo.deleteById(x.getId()));
    }

    public Thing createThing() {
        return new Thing();
    }

    //Актуальные Things (с последней датой)
    public List<Thing> currentThings(Unit unit) {
        List<Thing> thingList = unit.getThingList();

        Map<String, List<Thing>> objectMap = new HashMap<>();
        for (var i = 0; i < thingList.size(); i++) {
            if (objectMap.containsKey(thingList.get(i).getObject().getObjectName())) continue;

            List<Thing> tList = new ArrayList<>();
            tList.add(thingList.get(i));

            for (var j = 0; j < thingList.size(); j++) {
                if (j == i) continue;

                if (thingList.get(j).getObject().equals(thingList.get(i).getObject())) {
                    tList.add(thingList.get(j));
                }
            }
            objectMap.put(thingList.get(i).getObject().getObjectName(), tList);
        }

        List<Thing> returnList = new ArrayList<>();
        for (Map.Entry<String, List<Thing>> entry : objectMap.entrySet()) {
            //System.out.println(entry.getKey() + ":" + entry.getValue());
            Thing finalThing = entry.getValue().get(0);
            for (var item : entry.getValue()) {
                if (item.getLocalDate().isAfter(finalThing.getLocalDate())) {
                    finalThing = item;
                }
            }
            returnList.add(finalThing);
        }
        return returnList;
    }

    //

    public Integer integerNeed(List<Thing> things) {
        Integer need = 0;
        for (var item : things) {
            need += item.getGeneralNeed();
        }
        return need;
    }

    public Integer integerHave(List<Thing> things) {
        Integer have = 0;
        for (var item : things) {
            have += item.getGeneralHave();
        }
        return have;
    }

    public Integer percentUnit(Unit unit) {
        Integer need = 0, have = 0;

        List<Unit> unitList = unitsAllByUnit(unit);

        if (unitList != null || unitList.size() != 0) {
            for (var item : unitList) {
                if (item.getThingList().size() != 0 || item.getThingList() != null) {
                    need += integerNeed(item.getThingList());
                    have += integerHave(item.getThingList());
                }
            }
        }

        if(need == 0)
            return 0;

        return (int)Math.round((have * 100.0) / need);
    }

    public List<Unit> unitsAllByUnit(Unit unit) {
        List<Unit> units = new ArrayList<>();
        units.add(unit);

        if (unit.getUnits() != null || unit.getUnits().size() != 0) {
            for (var item : unit.getUnits()) {
                units.addAll(unitsAllByUnit(item));
            }
            return units;
        } else {
            return units;
        }
    }

    public Integer percentUnitByObject(Unit unit, Object object){
        Integer need = 0, have = 0;

        List<Unit> unitList = unitsAllByUnit(unit);

        if (unitList != null || unitList.size() != 0) {
            for (var item : unitList) {
                if (item.getThingList().size() != 0 || item.getThingList() != null) {
                    Thing thing = null;
                    for (var i : item.getThingList()){
                        if(i.getObject() == object)
                            thing = i;
                    }

                    if(thing != null){
                        have += thing.getGeneralHave();
                        need += thing.getGeneralNeed();
                    }
                }
            }
        }

        if(need == 0)
            return 0;

        return (int)Math.round((have * 100.0) / need);
    }

    public Integer percentUnitByObjectList(Unit unit, List<Object> objectList){
        Integer need = 0, have = 0;

        List<Unit> unitList = unitsAllByUnit(unit);

        if (unitList != null || unitList.size() != 0) {
            for (var item : unitList) {
                if (item.getThingList().size() != 0 || item.getThingList() != null) {
                    List<Thing> things = new ArrayList<>();
                    for (var i : item.getThingList()){
                        for (var j = 0; j < objectList.size(); j++){
                            if(i.getObject() == objectList.get(j)){
                                things.add(i);
                            }
                        }
                    }

                    if(things.size() != 0){
                        for (var thing : things){
                            have += thing.getGeneralHave();
                            need += thing.getGeneralNeed();
                        }
                    }
                }
            }
        }

        if(need == 0)
            return 0;

        return (int)Math.round((have * 100.0) / need);
    }

    public Integer percentUnitListByObjectList(List<Unit> unitList, List<Object> objectList){
        Integer need = 0, have = 0;

        if (unitList != null || unitList.size() != 0) {
            for (var item : unitList) {
                if (item.getThingList().size() != 0 || item.getThingList() != null) {
                    List<Thing> things = new ArrayList<>();
                    for (var i : item.getThingList()){
                        for (var j = 0; j < objectList.size(); j++){
                            if(i.getObject() == objectList.get(j)){
                                things.add(i);
                            }
                        }
                    }

                    if(things.size() != 0){
                        for (var thing : things){
                            have += thing.getGeneralHave();
                            need += thing.getGeneralNeed();
                        }
                    }
                }
            }
        }

        if(need == 0)
            return 0;

        return (int)Math.round((have * 100.0) / need);
    }
}

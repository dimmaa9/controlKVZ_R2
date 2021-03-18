package kvz.zsu.control.services;

import kvz.zsu.control.models.Thing;
import kvz.zsu.control.models.Unit;
import kvz.zsu.control.repositories.ThingRepo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public void save (Thing thing) {
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

    public Thing createThing(){
        return new Thing();
    }

    public Thing addThing(Thing thing) {
        Thing thingTemp = new Thing();
        thingTemp.setParentThing(thing);
        thing.getThings().add(thingTemp);
        return thing;
    }

    public Thing removeThing(Thing thing, Long id) {
        thing.getThings().remove(id.intValue());
        return thing;
    }

    public int getCountThingVn(Unit unit) {
        List<Thing> things = unit.getThingList();
        int countAll = things.size();
        int countVn = things.stream().filter(x -> x.getState().getState().equals("В наявності")).collect(Collectors.toList()).size();
        return (int) ((100 * countVn) / countAll);
    }

    public int getCountThingPb(Unit unit) {
        List<Thing> things = unit.getThingList();
        int countAll = things.size();
        int countPb = things.stream().filter(x -> x.getState().getState().equals("Потреба")).collect(Collectors.toList()).size();
        return (int) ((100 * countPb) / countAll);
    }

    public int getCountThingRm(Unit unit) {
        List<Thing> things = unit.getThingList();
        int countAll = things.size();
        int countRm = things.stream().filter(x -> x.getState().getState().equals("Ремонт")).collect(Collectors.toList()).size();
        return (int) ((100 * countRm) / countAll);
    }

    public List<Integer> getListCountVnInCategory(Unit unit) {
        List<Thing> things = unit.getThingList().stream().filter(x -> x.getState().getState().equals("В наявності")).collect(Collectors.toList());
        int c1 = 0, c2 = 0, c3 = 0, c4 = 0, c5 = 0;
        for (var item: things) {
            if (item.getCategory().equals(1))
                c1++;
            else if (item.getCategory().equals(2))
                c2++;
            else if (item.getCategory().equals(3))
                c3++;
            else if (item.getCategory().equals(4))
                c4++;
            else if (item.getCategory().equals(5))
                c5++;
        }
        return Arrays.asList(c1, c2, c3, c4, c5);
    }

    public List<Integer> getListCountPbInCategory(Unit unit) {
        List<Thing> things = unit.getThingList().stream().filter(x -> x.getState().getState().equals("Потреба")).collect(Collectors.toList());
        int c1 = 0, c2 = 0, c3 = 0, c4 = 0, c5 = 0;
        for (var item: things) {
            if (item.getCategory().equals(1))
                c1++;
            else if (item.getCategory().equals(2))
                c2++;
            else if (item.getCategory().equals(3))
                c3++;
            else if (item.getCategory().equals(4))
                c4++;
            else if (item.getCategory().equals(5))
                c5++;
        }
        return Arrays.asList(c1, c2, c3, c4, c5);
    }

    public List<Integer> getListCountRmInCategory(Unit unit) {
        List<Thing> things = unit.getThingList().stream().filter(x -> x.getState().getState().equals("Ремонт")).collect(Collectors.toList());
        int c1 = 0, c2 = 0, c3 = 0, c4 = 0, c5 = 0;
        for (var item: things) {
            if (item.getCategory().equals(1))
                c1++;
            else if (item.getCategory().equals(2))
                c2++;
            else if (item.getCategory().equals(3))
                c3++;
            else if (item.getCategory().equals(4))
                c4++;
            else if (item.getCategory().equals(5))
                c5++;
        }
        return Arrays.asList(c1, c2, c3, c4, c5);
    }

}

package kvz.zsu.control.services;

import kvz.zsu.control.models.Thing;
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

    public Thing createThing() {
        return new Thing();
    }


    //Відсоток укомплектованості по Thing
    //Формула которую придумал Ярик
    //Реализация Димона Биньковського
    //Печатал и форматировал код Бандура Тарас
    //Кит Валентин на дне открытых дверей
    public Map<Thing, Integer> staffing() {
        Map<Thing, Integer> map = new HashMap<>();

        for (var item : findAll()) {
            List<List<Integer>> y = calculationStaffing(item);
            y.add(Arrays.asList(item.getGeneralHave(), item.getGeneralNeed()));
            int x = showIntFinal(y);
            map.put(item, x);
        }
        return map;
    }

    public Integer showIntFinal(List<List<Integer>> list) {
        AtomicInteger sumHave = new AtomicInteger();
        AtomicInteger sumNeed = new AtomicInteger();

        list.forEach(x -> {
            sumHave.addAndGet(x.get(0));
            sumNeed.addAndGet(x.get(1));
        });

        return (int) sumHave.get() * 100 / sumNeed.get();
    }

    public List<List<Integer>> calculationStaffing(Thing thing) {
        //1) Have  2) Need
        List<List<Integer>> list = new ArrayList<>();

        for (var item : findAll().stream().filter(x -> x.getObject().equals(thing.getObject())).collect(Collectors.toList())) {
            if (item.getUnit().getParentUnit() == null)
                continue;

            if (item.getUnit().getParentUnit().equals(thing.getUnit())) {
                list.add(Arrays.asList(item.getGeneralHave(), item.getGeneralNeed()));
                if (item.getUnit().getUnits() != null) {
                    List<List<Integer>> tList = calculationStaffing(item);
                    list.addAll(tList);
                }
            }
        }
        return list;
    }

    //Конец формулы Ярика

}

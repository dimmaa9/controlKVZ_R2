package kvz.zsu.control.services;

import kvz.zsu.control.models.Thing;
import kvz.zsu.control.models.Unit;
import kvz.zsu.control.repositories.ThingRepo;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;
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

    public Map<Thing, Integer> staffing() {
        Map<Thing, Integer> map = new HashMap<>();

        for (var item : findAll()) {
            List<Integer> y = calculationStaffing2(item);
            y.add(calculationStaffingOne(item));
            int x = showIntFinal(y);
            map.put(item, x);
        }
        return map;
    }

    public Integer showIntFinal(List<Integer> list){
        return (int)list.stream().mapToInt(a -> a).sum() / list.size();
    }

    public int calculationStaffingOne(Thing thing){
        return (int) (thing.getGeneralHave() * 100 / thing.getGeneralNeed());
    }

    public List<Integer> calculationStaffing2(Thing thing) {
        List<Integer> list = new ArrayList<>();

        for (var item : findAll().stream().filter(x -> x.getObject().equals(thing.getObject())).collect(Collectors.toList())) {
            if (item.getUnit().getParentUnit() == null)
                continue;
            if (item.getUnit().getParentUnit().equals(thing.getUnit())) {
                list.add(calculationStaffingOne(item));
                if (item.getUnit().getUnits() != null){
                    List<Integer> tList = calculationStaffing2(item);
                    list.addAll(tList);
                }
            }
        }

        return list;
    }


}

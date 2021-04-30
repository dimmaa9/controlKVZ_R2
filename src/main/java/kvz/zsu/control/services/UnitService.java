package kvz.zsu.control.services;

import kvz.zsu.control.models.Unit;
import kvz.zsu.control.repositories.UnitRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UnitService {

    private final UnitRepo repo;

    public UnitService(UnitRepo repo) {
        this.repo = repo;
    }

    public List<Unit> findAll() {
        return repo.findAll();
    }

    public Unit findById(Long id) {
        return repo.findById(id).get();
    }

    public void save(Unit unit) {
        repo.save(unit);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }



    public Map<Long, String> findByAllId(List<Integer> idArr) {
        List<Unit> unitList = new ArrayList<>();
        for (var item : idArr) {
            unitList.add(findById(item.longValue()));
        }
        Map<Long, String> returnMap = new HashMap<>();
        for (var item : unitList) {
            for (var item2 : item.getUnits()) {
                returnMap.put(item2.getId(), item2.getNameUnit());
            }
        }

        return returnMap;
    }

    public List<Unit> unitsAllByUnitID(Long id) {
        Unit thisUnit = findById(id);
        List<Unit> units = new ArrayList<>();
        units.add(thisUnit);

        if(thisUnit.getUnits() != null || thisUnit.getUnits().size() != 0){
            for (var item : thisUnit.getUnits()){
                units.addAll(unitsAllByUnitID(item.getId()));
            }
            return units;
        }else {
            return units;
        }
    }
}

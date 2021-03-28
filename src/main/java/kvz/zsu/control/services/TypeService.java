package kvz.zsu.control.services;

import kvz.zsu.control.models.Scope;
import kvz.zsu.control.models.Type;
import kvz.zsu.control.models.Unit;
import kvz.zsu.control.repositories.TypeRepo;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TypeService {

    private TypeRepo repo;


    public TypeService(TypeRepo repo) {
        this.repo = repo;
    }

    public List<Type> findAll() {
        return repo.findAll();
    }

    public Type findById(Long id) {
        return repo.findById(id).get();
    }

    public Map<Long, String> findByAllId(List<Integer> idArr) {
        List<Type> typeList = new ArrayList<>();
        for (var item: idArr ) {
            typeList.add(findById(item.longValue()));
        }
        Map<Long, String> returnMap = new HashMap<>();
        for(var item: typeList){
            for (var item2: item.getObjectList()){
                returnMap.put(item2.getId(), item2.getObjectName());
            }
        }
        return returnMap;
    }
}

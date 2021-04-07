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

    public void save(Type type) {
        repo.save(type);
    }

    public void deleteById(long id) {
        repo.deleteById(id);
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

    public Map<Long, String> findAllByScopeId(Long id) {
        Map<Long, String> map = new HashMap<>();
        repo.findAll().stream()
                .filter(x -> x.getScope().getId().equals(id))
                .forEach(x -> map.put(x.getId(), x.getTypeName()));
        return map;
    }

    public Map<Long, Map<Long, String>> getTypes(Long id){
        Map<Long, Map<Long, String>> map = new HashMap<>();
        map.put(id, findAllByScopeId(id));
        return map;
    }
}

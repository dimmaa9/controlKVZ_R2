package kvz.zsu.control.services;

import kvz.zsu.control.models.Scope;
import kvz.zsu.control.models.Type;
import kvz.zsu.control.repositories.ScopeRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScopeService {

    private final ScopeRepo repo;

    public ScopeService(ScopeRepo repo) {
        this.repo = repo;
    }

    public List<Scope> findAll() {
        return repo.findAll();
    }

    public Scope findById(Long id) {
        return repo.findById(id).get();
    }

    public List<Type> findTypesById(Long id) {
        return findById(id).getTypeList();
    }

    public Scope findByScopeName(String name) {
        return repo.findByScope(name);
    }

    public Map<Long, String> findByAllId(List<Integer> idArr) {
        List<Scope> scopeList = new ArrayList<>();
        for (var item: idArr ) {
            scopeList.add(findById(item.longValue()));
        }

        Map<Long, String> returnMap = new HashMap<>();

        for(var item: scopeList){
            for (var item2: item.getTypeList()){
                returnMap.put(item2.getId(), item2.getType());
            }
        }
        return returnMap;
    }
}

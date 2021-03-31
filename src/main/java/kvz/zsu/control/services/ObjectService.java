package kvz.zsu.control.services;

import kvz.zsu.control.models.Object;
import kvz.zsu.control.repositories.ObjectRepo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ObjectService {

    private final ObjectRepo repo;

    public ObjectService(ObjectRepo repo) {
        this.repo = repo;
    }

    public Object findById(Long id){
        return repo.findById(id).get();
    }

    public List<Object> findAll() {
        return repo.findAll();
    }

    public Object findByName(String name) {
        return repo.findByObjectName(name);
    }

    public Map<Long, String> findAllByTypeId(Long id) {
        Map<Long, String> map = new HashMap<>();
        repo.findAll().stream()
                .filter(x -> x.getType().getId().equals(id))
                .forEach(x -> map.put(x.getId(), x.getObjectName()));
        return map;
    }

    public Map<Long, Map<Long, String>> getObject(Long id){
        Map<Long, Map<Long, String>> map = new HashMap<>();
        map.put(id, findAllByTypeId(id));
        return map;
    }

    public void save(Object object) {
        repo.save(object);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}

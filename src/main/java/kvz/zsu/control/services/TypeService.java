package kvz.zsu.control.services;

import kvz.zsu.control.models.Type;
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

    public Map<Long, String> findAllByScopeId(Long id) {
        Map<Long, String> map = new HashMap<>();
        repo.findAll().stream()
                .filter(x -> x.getScope().getId().equals(id))
                .forEach(x -> map.put(x.getId(), x.getType()));
        return map;
    }

    public Map<Long, Map<Long, String>> getTypes(Long id){
        Map<Long, Map<Long, String>> map = new HashMap<>();
        map.put(id, findAllByScopeId(id));
        return map;
    }
}

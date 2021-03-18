package kvz.zsu.control.services;

import kvz.zsu.control.models.Scope;
import kvz.zsu.control.models.Type;
import kvz.zsu.control.repositories.ScopeRepo;
import org.springframework.stereotype.Service;

import java.util.List;

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
}

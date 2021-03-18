package kvz.zsu.control.services;

import kvz.zsu.control.models.Unit;
import kvz.zsu.control.repositories.UnitRepo;
import org.springframework.stereotype.Service;

import java.util.List;

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

}

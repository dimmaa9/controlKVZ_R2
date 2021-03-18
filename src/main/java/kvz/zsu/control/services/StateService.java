package kvz.zsu.control.services;

import kvz.zsu.control.models.State;
import kvz.zsu.control.repositories.StateRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateService {

    private StateRepo repo;

    public StateService(StateRepo repo) {
        this.repo = repo;
    }

    public List<State> findAll() {
        return repo.findAll();
    }
}

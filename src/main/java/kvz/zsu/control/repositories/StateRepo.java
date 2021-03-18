package kvz.zsu.control.repositories;

import kvz.zsu.control.models.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepo extends JpaRepository<State, Long> {
}

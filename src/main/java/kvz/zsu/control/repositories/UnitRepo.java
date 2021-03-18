package kvz.zsu.control.repositories;

import kvz.zsu.control.models.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepo extends JpaRepository<Unit, Long> {
}

package kvz.zsu.control.repositories;

import kvz.zsu.control.models.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepo extends JpaRepository<Type, Long> {
}

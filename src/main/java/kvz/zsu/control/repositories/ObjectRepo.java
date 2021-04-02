package kvz.zsu.control.repositories;

import kvz.zsu.control.models.Object;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectRepo extends JpaRepository<Object, Long> {

    Object findByObjectName (String name);
}

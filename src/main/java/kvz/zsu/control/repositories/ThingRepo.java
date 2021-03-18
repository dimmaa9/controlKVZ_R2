package kvz.zsu.control.repositories;


import kvz.zsu.control.models.Thing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThingRepo extends JpaRepository<Thing, Long> {

}

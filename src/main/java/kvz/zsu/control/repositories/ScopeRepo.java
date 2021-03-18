package kvz.zsu.control.repositories;

import kvz.zsu.control.models.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScopeRepo extends JpaRepository<Scope, Long> {

    Scope findByScope(String scope);
}

package dev.zachmaddox.scorecard.api.repository;

import java.util.List;
import java.util.Optional;

import dev.zachmaddox.scorecard.api.domain.Action;
import dev.zachmaddox.scorecard.api.domain.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepository extends JpaRepository<Action, Long> {
	
	Optional<Action> findByName(String name);
	Optional<Action> findByServiceAndName(Service service, String name);
	List<Action> findByService(Service service);
    Optional<Action> findById(Long actionId);
}

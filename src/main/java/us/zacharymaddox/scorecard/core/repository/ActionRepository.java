package us.zacharymaddox.scorecard.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import us.zacharymaddox.scorecard.core.domain.Action;
import us.zacharymaddox.scorecard.core.domain.Service;

public interface ActionRepository extends JpaRepository<Action, Long> {
	
	public Optional<Action> findByName(String name);
	public Optional<Action> findByServiceAndName(Service service, String name);
	public List<Action> findByService(Service service);

}

package us.zacharymaddox.scorecard.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import us.zacharymaddox.scorecard.core.domain.Action;

public interface ActionRepository extends JpaRepository<Action, Long> {
	
	public Optional<Action> findByName(String name);

}

package us.zacharymaddox.scorecard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import us.zacharymaddox.scorecard.domain.Action;

public interface ActionRepository extends JpaRepository<Action, Long> {

}

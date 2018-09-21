package us.zacharymaddox.scorecard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import us.zacharymaddox.scorecard.domain.ScoreCard;

public interface ScoreCardRepository extends JpaRepository<ScoreCard, Long> {

}

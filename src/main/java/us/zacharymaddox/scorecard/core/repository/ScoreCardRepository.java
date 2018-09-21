package us.zacharymaddox.scorecard.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import us.zacharymaddox.scorecard.core.domain.ScoreCard;

public interface ScoreCardRepository extends JpaRepository<ScoreCard, Long> {
	
	@Query(value="select SCORE_CARD_ID_SEQUENCE.nextval from dual", nativeQuery=true)
	public Long fetchNextScoreCardId();

}

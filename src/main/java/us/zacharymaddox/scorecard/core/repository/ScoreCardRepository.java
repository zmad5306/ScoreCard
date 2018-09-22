package us.zacharymaddox.scorecard.core.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import us.zacharymaddox.scorecard.core.domain.ScoreCard;
import us.zacharymaddox.scorecard.domain.ScoreCardStatus;

public interface ScoreCardRepository extends JpaRepository<ScoreCard, Long> {
	
	@Query(value="select SCORE_CARD_ID_SEQUENCE.nextval from dual", nativeQuery=true)
	public Long fetchNextScoreCardId();
	
	public List<ScoreCard> findByScoreCardStatusOrderByScoreCardIdDesc(ScoreCardStatus scoreCardStatus, Pageable pageable);

}

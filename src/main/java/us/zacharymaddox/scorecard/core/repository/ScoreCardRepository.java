package us.zacharymaddox.scorecard.core.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import us.zacharymaddox.scorecard.core.domain.ScoreCard;
import us.zacharymaddox.scorecard.domain.ScoreCardStatus;

public interface ScoreCardRepository extends JpaRepository<ScoreCard, Long> {
	
	@Query(value="select SCORE_CARD_ID_SEQUENCE.nextval from dual", nativeQuery=true)
	public Long fetchNextScoreCardId();
	
	public List<ScoreCard> findByScoreCardStatusOrderByScoreCardIdDesc(ScoreCardStatus scoreCardStatus, Pageable pageable);
	
	@Query("select sc from ScoreCardAction sca join sca.scoreCard sc where sca.status = 'FAILED' and sc.transactionName = :transaction_name and sc.scoreCardStatus <> 'COMPLETED'")
	public List<ScoreCard> findByFaliedScoreCards(@Param("transaction_name") String transactionName, Pageable pageable);

}

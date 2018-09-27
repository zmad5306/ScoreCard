package us.zacharymaddox.scorecard.core.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import us.zacharymaddox.scorecard.core.domain.ScoreCard;
import us.zacharymaddox.scorecard.domain.ScoreCardActionStatus;

public interface ScoreCardRepository extends JpaRepository<ScoreCard, Long> {
	
	@Query(value="select SCORE_CARD_ID_SEQUENCE.nextval from dual", nativeQuery=true)
	public Long fetchNextScoreCardId();
	
	public List<ScoreCard> findByTransactionNameOrderByScoreCardIdDesc(String transactionName, Pageable pageable);
	
	@Query("from ScoreCard order by scoreCardId desc")
	public List<ScoreCard> findAllOrderByScoreCardIdDesc(Pageable pageable);
	
	@Query("select sc from ScoreCardAction sca join sca.scoreCard sc where sca.status = :score_card_action_status and sc.transactionName = :transaction_name order by sc.scoreCardId desc")
	public List<ScoreCard> findByActionStatusAndTransactionNameOrderByScoreCardIdDesc(@Param("score_card_action_status") ScoreCardActionStatus status, @Param("transaction_name")String transactionName, Pageable pageable);
	
}

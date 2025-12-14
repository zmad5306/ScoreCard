package dev.zachmaddox.scorecard.api.repository;

import java.util.List;

import dev.zachmaddox.scorecard.api.domain.ScoreCard;
import dev.zachmaddox.scorecard.common.domain.ScoreCardActionStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScoreCardRepository extends JpaRepository<ScoreCard, Long> {
	
	@Query(value="select nextval('score_card.score_card_id_sequence')", nativeQuery=true)
	Long fetchNextScoreCardId();
	
	List<ScoreCard> findByTransactionNameOrderByScoreCardIdDesc(String transactionName, Pageable pageable);
	
	Long countByTransactionName(String transactionName);
	
	@Query("from ScoreCard order by scoreCardId desc")
	List<ScoreCard> findAllOrderByScoreCardIdDesc(Pageable pageable);
	
	@Query("select sc from ScoreCardAction sca join sca.scoreCard sc where sca.status = :score_card_action_status and sc.transactionName = :transaction_name order by sc.scoreCardId desc")
	List<ScoreCard> findByActionStatusAndTransactionNameOrderByScoreCardIdDesc(@Param("score_card_action_status") ScoreCardActionStatus status, @Param("transaction_name")String transactionName, Pageable pageable);
	
}

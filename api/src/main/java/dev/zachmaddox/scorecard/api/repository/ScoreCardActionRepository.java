package dev.zachmaddox.scorecard.api.repository;

import java.util.List;
import java.util.Optional;

import dev.zachmaddox.scorecard.api.domain.ScoreCard;
import dev.zachmaddox.scorecard.api.domain.ScoreCardAction;
import dev.zachmaddox.scorecard.common.domain.ScoreCardActionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScoreCardActionRepository extends JpaRepository<ScoreCardAction, Long> {
	
	@Query("from ScoreCardAction where scoreCard.id = :score_card_id and actionId = :action_id")
	Optional<ScoreCardAction> findByScoreCardIdAndActionId(@Param("score_card_id") Long scoreCardId, @Param("action_id") Long actionId);
	
	@Query("from ScoreCardAction where scoreCard.id = :score_card_id and scoreCardActionId = :score_card_action_id")
	Optional<ScoreCardAction> findByScoreCardIdAndScoreCardActionId(@Param("score_card_id") Long scoreCardId, @Param("score_card_action_id") Long actionId);
	
	Long countByScoreCardAndStatusIn(ScoreCard scoreCard, List<ScoreCardActionStatus> status);

}

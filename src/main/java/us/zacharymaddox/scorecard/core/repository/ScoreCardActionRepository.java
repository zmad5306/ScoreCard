package us.zacharymaddox.scorecard.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import us.zacharymaddox.scorecard.common.domain.ScoreCardActionStatus;
import us.zacharymaddox.scorecard.core.domain.ScoreCard;
import us.zacharymaddox.scorecard.core.domain.ScoreCardAction;

public interface ScoreCardActionRepository extends JpaRepository<ScoreCardAction, Long> {
	
	@Query("from ScoreCardAction where scoreCard.id = :score_card_id and action.id = :action_id")
	public Optional<ScoreCardAction> findByScoreCardIdAndActionId(@Param("score_card_id") Long scoreCardId, @Param("action_id") Long actionId);
	
	@Query("from ScoreCardAction where scoreCard.id = :score_card_id and scoreCardActionId = :score_card_action_id")
	public Optional<ScoreCardAction> findByScoreCardIdAndScoreCardActionId(@Param("score_card_id") Long scoreCardId, @Param("score_card_action_id") Long actionId);
	
	public Long countByScoreCardAndStatusIn(ScoreCard scoreCard, List<ScoreCardActionStatus> status);

}

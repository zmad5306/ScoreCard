package dev.zachmaddox.scorecard.lib.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.zachmaddox.scorecard.common.domain.ScoreCardActionStatus;
import dev.zachmaddox.scorecard.lib.domain.exception.ActionNotFoundException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoreCard implements Serializable {

	@JsonProperty("score_card_id")
	private Long scoreCardId;
	@JsonProperty("transaction_id")
	private Long transactionId;
	@JsonProperty("transaction_name")
	private String transactionName;
	private List<Action> actions;

	public Action getAction(String name) {
		return actions.stream().filter(a -> name.equalsIgnoreCase(a.getName())).findFirst().orElseThrow(ActionNotFoundException::new);
	}
	public Action getAction(Long id) {
		return actions.stream().filter(a -> a.getActionId().equals(id)).findFirst().orElseThrow(ActionNotFoundException::new);
	}
	public boolean hasFailedActions() {
		long failedActionCount = actions.stream().filter(a -> ScoreCardActionStatus.FAILED.equals(a.getStatus())).count();
		return failedActionCount > 0;
	}
	public boolean hasPendingActions() {
		if (hasFailedActions()) {
			return false;
		} 
		List<ScoreCardActionStatus> pendingStatus = List.of(ScoreCardActionStatus.PENDING, ScoreCardActionStatus.PROCESSING);
		long pendingActionCount = actions.stream().filter(a -> pendingStatus.contains(a.getStatus())).count();
		return pendingActionCount > 0;
	}
}

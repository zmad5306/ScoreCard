package us.zacharymaddox.scorecard.api.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

import us.zacharymaddox.scorecard.domain.ScoreCardActionStatus;

public class ScoreCard implements Serializable {

	private static final long serialVersionUID = -5256738519302423925L;
	private String type;
	@JsonProperty("score_card_id")
	private Long scoreCardId;
	@JsonProperty("transaction_id")
	private Long transactionId;
	@JsonProperty("transaction_name")
	private String transactionName;
	private List<Action> actions;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getScoreCardId() {
		return scoreCardId;
	}
	public void setScoreCardId(Long scoreCardId) {
		this.scoreCardId = scoreCardId;
	}
	public Long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	public List<Action> getActions() {
		return actions;
	}
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
	public Action getAction(String name) {
		Optional<Action> action = actions.stream().filter(a -> name.equalsIgnoreCase(a.getName())).findFirst();
		return action.get();
	}
	public Action getAction(Long id) {
		Optional<Action> action = actions.stream().filter(a -> id == a.getActionId()).findFirst();
		return action.get();
	}
	public boolean hadFailedActions() {
		Long failedActionCount = actions.stream().filter(a -> ScoreCardActionStatus.FAILED.equals(a.getStatus())).count();
		return failedActionCount > 0;
	}
	public String getTransactionName() {
		return transactionName;
	}
	public void setTransactionName(String transactionName) {
		this.transactionName = transactionName;
	}
}

package us.zacharymaddox.scorecard.api.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import us.zacharymaddox.scorecard.common.domain.ScoreCardStatus;

public class ScoreCard implements Serializable {

	private static final long serialVersionUID = -5256738519302423925L;
	private String type;
	@JsonProperty("score_card_id")
	private Long scoreCardId;
	@JsonProperty("transaction_id")
	private Long transactionId;
	@JsonProperty("start_timestamp")
	private LocalDateTime startTimestamp;
	@JsonProperty("end_timestamp")
	private LocalDateTime endTimestamp;
	@JsonProperty("score_card_status")
	private ScoreCardStatus scoreCardStatus;
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
	public LocalDateTime getStartTimestamp() {
		return startTimestamp;
	}
	public void setStartTimestamp(LocalDateTime startTimestamp) {
		this.startTimestamp = startTimestamp;
	}
	public LocalDateTime getEndTimestamp() {
		return endTimestamp;
	}
	public void setEndTimestamp(LocalDateTime endTimestamp) {
		this.endTimestamp = endTimestamp;
	}
	public ScoreCardStatus getScoreCardStatus() {
		return scoreCardStatus;
	}
	public void setScoreCardStatus(ScoreCardStatus scoreCardStatus) {
		this.scoreCardStatus = scoreCardStatus;
	}
	public List<Action> getActions() {
		return actions;
	}
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
	
}

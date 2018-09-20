package us.zacharymaddox.scorecard.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScoreCard extends DomainObject implements Serializable {

	private static final long serialVersionUID = 3392668963392107413L;

	public ScoreCard() {
		super("score_card");
	}
	
	@Id
	@JsonProperty("score_card_id")
	private String scoreCardId;
	@JsonProperty("transaction_id")
	private String transactionId;
	@JsonProperty("start_timestamp")
	private LocalDateTime startTimestamp;
	@JsonProperty("end_timestamp")
	private LocalDateTime endTimestamp;
	private List<ScoreCardAction> actions;

	public String getScoreCardId() {
		return scoreCardId;
	}
	public void setScoreCardId(String scoreCardId) {
		this.scoreCardId = scoreCardId;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
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
	public List<ScoreCardAction> getActions() {
		return actions;
	}
	public void setActions(List<ScoreCardAction> actions) {
		this.actions = actions;
	}
	
}

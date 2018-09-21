package us.zacharymaddox.scorecard.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateRequest implements Serializable {
	
	private static final long serialVersionUID = -451969971803193333L;
	@JsonProperty("score_card_id")
	private Long scoreCardId;
	@JsonProperty("score_card_action_id")
	private Long scoreCardactionId;
	private ScoreCardActionStatus status;
	
	public Long getScoreCardId() {
		return scoreCardId;
	}
	public void setScoreCardId(Long scoreCardId) {
		this.scoreCardId = scoreCardId;
	}
	public Long getScoreCardactionId() {
		return scoreCardactionId;
	}
	public void setScoreCardactionId(Long scoreCardactionId) {
		this.scoreCardactionId = scoreCardactionId;
	}
	public ScoreCardActionStatus getStatus() {
		return status;
	}
	public void setStatus(ScoreCardActionStatus status) {
		this.status = status;
	}

}

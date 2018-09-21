package us.zacharymaddox.scorecard.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizationRequest implements Serializable {
	
	private static final long serialVersionUID = -978370798490525967L;
	@JsonProperty("score_card_id")
	private Long scoreCardId;
	@JsonProperty("score_card_action_id")
	private Long scoreCardActionId;
	public Long getScoreCardId() {
		return scoreCardId;
	}
	public void setScoreCardId(Long scoreCardId) {
		this.scoreCardId = scoreCardId;
	}
	public Long getScoreCardActionId() {
		return scoreCardActionId;
	}
	public void setScoreCardActionId(Long scoreCardActionId) {
		this.scoreCardActionId = scoreCardActionId;
	}

}

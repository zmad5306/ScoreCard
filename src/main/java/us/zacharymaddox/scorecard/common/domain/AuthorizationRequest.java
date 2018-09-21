package us.zacharymaddox.scorecard.common.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizationRequest implements Serializable {
	
	private static final long serialVersionUID = -978370798490525967L;
	@JsonProperty("score_card_id")
	private Long scoreCardId;
	@JsonProperty("action_id")
	private Long actionId;
	public Long getScoreCardId() {
		return scoreCardId;
	}
	public void setScoreCardId(Long scoreCardId) {
		this.scoreCardId = scoreCardId;
	}
	public Long getActionId() {
		return actionId;
	}
	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}

}

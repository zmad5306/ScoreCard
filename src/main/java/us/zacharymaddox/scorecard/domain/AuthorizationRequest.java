package us.zacharymaddox.scorecard.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizationRequest implements Serializable {
	
	private static final long serialVersionUID = -978370798490525967L;
	@JsonProperty("score_card_id")
	private String scoreCardId;
	@JsonProperty("action_id")
	private String actionId;
	
	public String getScoreCardId() {
		return scoreCardId;
	}
	public void setScoreCardId(String scoreCardId) {
		this.scoreCardId = scoreCardId;
	}
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	
	

}

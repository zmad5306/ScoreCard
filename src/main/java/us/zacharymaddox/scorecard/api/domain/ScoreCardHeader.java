package us.zacharymaddox.scorecard.api.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ScoreCardHeader implements Serializable {

	private static final long serialVersionUID = -7526764743694248208L;
	@JsonProperty("score_card_id")
	private Long scoreCardId;
	@JsonProperty("action_id")
	private Long actionId;
	@JsonIgnore
	private String path;
	
	public ScoreCardHeader() {
		super();
	}

	public ScoreCardHeader(Long scoreCardId, Long actionId, String path) {
		super();
		this.scoreCardId = scoreCardId;
		this.actionId = actionId;
		this.path = path;
	}

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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
}

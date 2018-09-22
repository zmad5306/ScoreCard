package us.zacharymaddox.scorecard.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateRequest implements Serializable {
	
	private static final long serialVersionUID = -451969971803193333L;
	@JsonProperty("score_card_id")
	private Long scoreCardId;
	@JsonProperty("action_id")
	private Long actionId;
	private ScoreCardActionStatus status;
	private Map<String, String> metadata;
	
	public UpdateRequest() {
		super();
	}
	public UpdateRequest(Long scoreCardId, Long actionId, ScoreCardActionStatus status) {
		super();
		this.scoreCardId = scoreCardId;
		this.actionId = actionId;
		this.status = status;
	}
	public UpdateRequest(Long scoreCardId, Long actionId, ScoreCardActionStatus status, Map<String, String> metadata) {
		super();
		this.scoreCardId = scoreCardId;
		this.actionId = actionId;
		this.status = status;
		this.metadata = metadata;
	}
	public Long getScoreCardId() {
		return scoreCardId;
	}
	public void setScoreCardId(Long scoreCardId) {
		this.scoreCardId = scoreCardId;
	}
	public ScoreCardActionStatus getStatus() {
		return status;
	}
	public void setStatus(ScoreCardActionStatus status) {
		this.status = status;
	}
	public Long getActionId() {
		return actionId;
	}
	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}
	public Map<String, String> getMetadata() {
		return metadata;
	}
	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}
	public void addMetaData(String name, String value) {
		if (null == metadata) {
			metadata = new HashMap<>();
		}
		metadata.put(name, value);
	}
	
}

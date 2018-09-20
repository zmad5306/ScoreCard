package us.zacharymaddox.scorecard.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class ScoreCardAction implements Serializable {

	private static final long serialVersionUID = 7351293958881594081L;

	private String actionId;
	private ScoreCardActionStatus status;
	private LocalDateTime startTimestamp;
	private LocalDateTime endTimestamp;
	private List<String> dependencies;
	
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	public ScoreCardActionStatus getStatus() {
		return status;
	}
	public void setStatus(ScoreCardActionStatus status) {
		this.status = status;
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
	public List<String> getDependencies() {
		return dependencies;
	}
	public void setDependencies(List<String> dependencies) {
		this.dependencies = dependencies;
	}
	
}

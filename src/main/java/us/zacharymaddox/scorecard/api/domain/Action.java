package us.zacharymaddox.scorecard.api.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import us.zacharymaddox.scorecard.domain.Method;
import us.zacharymaddox.scorecard.domain.ScoreCardActionStatus;

public class Action implements Serializable {
	
	private static final long serialVersionUID = -5834592838132576186L;
	@JsonProperty("action_id")
	@NotNull
	private Long actionId;
	private String type;
	@NotNull
	@NotEmpty
	private String name;
	@NotNull
	@NotEmpty
	private String path;
	@NotNull
	private Method method;
	private Service service;
	private ScoreCardActionStatus status;
	@JsonProperty("start_timestamp")
	private LocalDateTime startTimestamp;
	@JsonProperty("end_timestamp")
	private LocalDateTime endTimestamp;
	@JsonProperty("depends_on")
	private List<Long> dependsOn;
	private Map<String, String> metadata;
	
	public Long getActionId() {
		return actionId;
	}
	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public Service getService() {
		return service;
	}
	public void setService(Service service) {
		this.service = service;
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
	public List<Long> getDependsOn() {
		return dependsOn;
	}
	public void setDependsOn(List<Long> dependsOn) {
		this.dependsOn = dependsOn;
	}
	public ScoreCardActionStatus getStatus() {
		return status;
	}
	public void setStatus(ScoreCardActionStatus status) {
		this.status = status;
	}
	public Map<String, String> getMetadata() {
		return metadata;
	}
	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}
	
}

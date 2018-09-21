package us.zacharymaddox.scorecard.api.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Action implements Serializable {
	
	private static final long serialVersionUID = -5834592838132576186L;
	@JsonProperty("action_id")
	private Long actionId;
	private String type;
	private String name;
	private String path;
	private String method;
	private Service service;
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
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Service getService() {
		return service;
	}
	public void setService(Service service) {
		this.service = service;
	}
	
}

package us.zacharymaddox.scorecard.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

public class Action extends BaseDomain implements Serializable {

	private static final long serialVersionUID = -3686654230565046107L;
	
	@Id
	private String actionId;
	private String name;
	private String serviceId;
	private String path;
	private Method method;

	public Action() {
		super("action");
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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
	
}

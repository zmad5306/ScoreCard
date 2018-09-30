package us.zacharymaddox.scorecard.portal.domain;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class NewAction implements Serializable {
	
	public interface Step1 {}
	public interface Step2 {}
	
	private static final long serialVersionUID = 2698532145615776984L;
	@NotNull(groups= {Step1.class, Step2.class}, message="must select service")
	private Long serviceId;
	
	@NotNull(groups= {Step2.class}, message="must select action")
	private Long actionId;

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public Long getActionId() {
		return actionId;
	}

	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}
	
}

package dev.zachmaddox.scorecard.portal.domain;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewAction implements Serializable {
	
	public interface Step1 {}
	public interface Step2 {}
	
	@NotNull(groups= {Step1.class, Step2.class}, message="must select service")
	private Long serviceId;
	
	@NotNull(groups= {Step2.class}, message="must select action")
	private Long actionId;

}

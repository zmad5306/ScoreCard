package dev.zachmaddox.scorecard.lib.domain;

import java.io.Serializable;
import java.util.List;

import dev.zachmaddox.scorecard.common.domain.Transport;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Service implements Serializable {
	
	@JsonProperty("service_id")
	private Long serviceId;
	private String type;
	@NotNull
	@NotEmpty
	private String name;
	@NotNull
	private Transport transport;
	@NotNull
	@NotEmpty
	private String path;
	private List<Action> actions;

}

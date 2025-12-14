package dev.zachmaddox.scorecard.lib.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import dev.zachmaddox.scorecard.common.domain.Method;
import dev.zachmaddox.scorecard.common.domain.ScoreCardActionStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Action implements Serializable {

    @JsonProperty("action_id")
    @EqualsAndHashCode.Include
	private Long actionId;
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

}

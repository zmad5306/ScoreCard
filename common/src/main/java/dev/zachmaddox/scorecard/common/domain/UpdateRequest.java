package dev.zachmaddox.scorecard.common.domain;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequest implements Serializable {
	
	@Schema(description = "Score card identifier")
	@JsonProperty("score_card_id")
	private Long scoreCardId;
	@Schema(description = "Action identifier")
	@JsonProperty("action_id")
	private Long actionId;
	@Schema(description = "New action status")
	private ScoreCardActionStatus status;
	@Schema(description = "Optional metadata per action")
	private Map<String, String> metadata;
	
	public UpdateRequest(Long scoreCardId, Long actionId, ScoreCardActionStatus status) {
		super();
		this.scoreCardId = scoreCardId;
		this.actionId = actionId;
		this.status = status;
	}

}

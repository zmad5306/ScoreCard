package dev.zachmaddox.scorecard.lib.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScoreCardHeader implements Serializable {

	@JsonProperty("score_card_id")
	private Long scoreCardId;
	@JsonProperty("action_id")
	private Long actionId;
	@JsonIgnore
	private String path;

    public ScoreCardHeader(Long scoreCardId, Long actionId, String path) {
		super();
		this.scoreCardId = scoreCardId;
		this.actionId = actionId;
		this.path = path;
	}
	
}

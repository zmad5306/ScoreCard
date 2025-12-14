package dev.zachmaddox.scorecard.api.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ScoreCardId implements Serializable {

	@JsonProperty("score_card_id")
    @EqualsAndHashCode.Include
	private Long scoreCardId;
	
}

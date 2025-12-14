package dev.zachmaddox.scorecard.lib.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ScoreCardId implements Serializable {
    @JsonProperty("score_card_id")
    private Long scoreCardId;
}

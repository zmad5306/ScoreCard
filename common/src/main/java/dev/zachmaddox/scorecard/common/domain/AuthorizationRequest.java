package dev.zachmaddox.scorecard.common.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to authorize an action for a score card")
public record AuthorizationRequest(
        @Schema(description = "Score card identifier") @JsonProperty("score_card_id") Long scoreCardId,
        @Schema(description = "Action identifier") @JsonProperty("action_id") Long actionId
) implements Serializable {
}

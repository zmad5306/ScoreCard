package dev.zachmaddox.scorecard.common.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to create a new score card record for a transaction")
public record CreateRequest(
        @Schema(description = "Score card identifier") @JsonProperty("score_card_id") Long scoreCardId,
        @Schema(description = "Transaction identifier") @JsonProperty("transaction_id") Long transactionId
) implements Serializable {

    public CreateRequest(Long scoreCardId, Long transactionId) {
        this.scoreCardId = scoreCardId;
        this.transactionId = transactionId;
    }

}

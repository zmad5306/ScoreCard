package dev.zachmaddox.scorecard.lib.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WaitException extends RuntimeException {
    private final ObjectNode scoreCardHeader;
    private final JsonNode messageBody;
}

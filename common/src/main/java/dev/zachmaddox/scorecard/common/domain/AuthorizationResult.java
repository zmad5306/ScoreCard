package dev.zachmaddox.scorecard.common.domain;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authorization response")
public record AuthorizationResult(
        @Schema(description = "Authorization decision") Authorization authorization
) implements Serializable {}

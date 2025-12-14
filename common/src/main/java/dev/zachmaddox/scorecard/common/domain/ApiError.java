package dev.zachmaddox.scorecard.common.domain;

import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardErrorCode;

import java.io.Serializable;


public record ApiError(ScoreCardErrorCode errorCode, String message) implements Serializable {}

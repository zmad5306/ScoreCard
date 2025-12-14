package dev.zachmaddox.scorecard.common.domain.exception;

public class ScoreCardServerException extends ScoreCardException {
	public ScoreCardServerException(ScoreCardErrorCode error) {
		super(error);
	}
}

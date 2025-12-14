package dev.zachmaddox.scorecard.common.domain.exception;

public class ScoreCardClientException extends ScoreCardException {
	public ScoreCardClientException(ScoreCardErrorCode error) {
		super(error);
	}
}

package dev.zachmaddox.scorecard.common.domain.exception;

import lombok.Getter;

@Getter
public class ScoreCardException extends RuntimeException {

	private final ScoreCardErrorCode error;
	
	public ScoreCardException(ScoreCardErrorCode error) {
		super(error.getMessage());
		this.error = error;
	}

}

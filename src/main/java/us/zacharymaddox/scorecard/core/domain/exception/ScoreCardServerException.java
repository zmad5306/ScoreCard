package us.zacharymaddox.scorecard.core.domain.exception;

public class ScoreCardServerException extends ScoreCardException {

	private static final long serialVersionUID = 2180407041263026831L;

	public ScoreCardServerException(ScoreCardErrorCode error) {
		super(error);
	}

}

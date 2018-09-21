package us.zacharymaddox.scorecard.core.domain.exception;

public class ScoreCardException extends RuntimeException {

	private static final long serialVersionUID = 8475265857106559943L;
	private ScoreCardErrorCode error;
	
	public ScoreCardException(ScoreCardErrorCode error) {
		super(error.getMessage());
		this.error = error;
	}

	public ScoreCardErrorCode getError() {
		return error;
	}

	public void setError(ScoreCardErrorCode error) {
		this.error = error;
	}
	
}
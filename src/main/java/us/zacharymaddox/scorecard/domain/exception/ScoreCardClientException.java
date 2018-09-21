package us.zacharymaddox.scorecard.domain.exception;

public class ScoreCardClientException extends ScoreCardException {

	private static final long serialVersionUID = 4463695044629232194L;
	
	public ScoreCardClientException(ScoreCardErrorCode error) {
		super(error);
	}

}

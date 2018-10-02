package us.zacharymaddox.scorecard.api.domain.exception;

import us.zacharymaddox.scorecard.domain.exception.ScoreCardClientException;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardErrorCode;

public class ActionInUseException extends ScoreCardClientException {

	private static final long serialVersionUID = 400751977184550388L;
	
	public ActionInUseException(ScoreCardErrorCode error) {
		super(error);
	}

}

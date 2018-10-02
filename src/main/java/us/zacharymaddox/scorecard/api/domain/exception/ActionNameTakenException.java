package us.zacharymaddox.scorecard.api.domain.exception;

import us.zacharymaddox.scorecard.domain.exception.ScoreCardClientException;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardErrorCode;

public class ActionNameTakenException extends ScoreCardClientException {

	private static final long serialVersionUID = -155280208821211654L;

	public ActionNameTakenException() {
		super(ScoreCardErrorCode.ACTION_NAME_TAKEN);
	}
}

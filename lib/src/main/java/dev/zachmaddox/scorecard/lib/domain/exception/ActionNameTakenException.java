package dev.zachmaddox.scorecard.lib.domain.exception;

import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardClientException;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardErrorCode;

public class ActionNameTakenException extends ScoreCardClientException {
	public ActionNameTakenException() {
		super(ScoreCardErrorCode.ACTION_NAME_TAKEN);
	}
}

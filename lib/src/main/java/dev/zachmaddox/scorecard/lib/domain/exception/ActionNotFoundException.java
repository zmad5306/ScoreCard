package dev.zachmaddox.scorecard.lib.domain.exception;

import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardClientException;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardErrorCode;

public class ActionNotFoundException extends ScoreCardClientException {
	public ActionNotFoundException() {
		super(ScoreCardErrorCode.ACTION_NOT_FOUND);
	}
}

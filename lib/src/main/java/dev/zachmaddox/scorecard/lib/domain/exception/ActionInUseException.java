package dev.zachmaddox.scorecard.lib.domain.exception;

import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardClientException;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardErrorCode;

public class ActionInUseException extends ScoreCardClientException {
	public ActionInUseException(ScoreCardErrorCode error) {
		super(error);
	}
}

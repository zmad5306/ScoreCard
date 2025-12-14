package dev.zachmaddox.scorecard.lib.domain.exception;

import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardClientException;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardErrorCode;

public class ServiceNameTakenException extends ScoreCardClientException {
	public ServiceNameTakenException() {
		super(ScoreCardErrorCode.SERVICE_NAME_TAKEN);
	}

}

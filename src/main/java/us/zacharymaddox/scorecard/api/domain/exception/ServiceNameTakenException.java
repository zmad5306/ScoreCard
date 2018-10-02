package us.zacharymaddox.scorecard.api.domain.exception;

import us.zacharymaddox.scorecard.domain.exception.ScoreCardClientException;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardErrorCode;

public class ServiceNameTakenException extends ScoreCardClientException {
	
	private static final long serialVersionUID = 1976662281168173850L;

	public ServiceNameTakenException() {
		super(ScoreCardErrorCode.SERVICE_NAME_TAKEN);
	}

}

package us.zacharymaddox.scorecard.api.domain.exception;

import us.zacharymaddox.scorecard.domain.exception.ScoreCardClientException;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardErrorCode;

public class TransactionNameNotUniqueException extends ScoreCardClientException {

	private static final long serialVersionUID = 9208575729455097950L;
	
	public TransactionNameNotUniqueException() {
		super(ScoreCardErrorCode.TRANSACTION_NAME_TAKEN);
	}

}

package dev.zachmaddox.scorecard.lib.domain.exception;

import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardClientException;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardErrorCode;

public class TransactionNameNotUniqueException extends ScoreCardClientException {
	public TransactionNameNotUniqueException() {
		super(ScoreCardErrorCode.TRANSACTION_NAME_TAKEN);
	}

}

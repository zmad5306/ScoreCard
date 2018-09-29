package us.zacharymaddox.scorecard.core.domain.exception;

import java.util.Arrays;
import java.util.Optional;

public enum ScoreCardErrorCode {
	
	TRANSACTION_DNE("SC-0001", "The request Transaction does not exist.", 404),
	SCORE_CARD_DNE("SC-0002", "The requested Score Card does not exist.", 404),
	SCORE_CARD_ACTION_DNE("SC-0003", "The requested Score Card Action does not exist.", 404),
	SCORE_CARD_ACTION_DEPENDENCY_DNE("SC-0004", "The requested Score Card Action Dependency does not exist.", 404),
	ILLEGAL_STATE_CHANGE_NOT_AUTHORIZED("SC-0005", "The requested Score Card Action update was denied, this action was not authorized.", 403),
	ILLEGAL_STATE_CHANGE("SC-0006", "The requested Score Card Action update was not completed, invalid state change.", 400), 
	SERVICE_DNE("SC-0007", "The requested Service does not exist.", 404), 
	ACTION_DNE("SC-0008", "The requested Action does not exist.", 404), 
	SERVICE_NAME_TAKEN("SC-0009", "The requested Service name already exists, select a unique service name.", 400), 
	SERVICE_INVALID("SC-0010", "The requested Service passed was invalid.", 400);
	
	private String errorCode;
	private String message;
	private Integer status;
	
	private ScoreCardErrorCode(String errorCode, String message, Integer status) {
		this.errorCode = errorCode;
		this.message = message;
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public Integer getStatus() {
		return status;
	}
	
	public static ScoreCardErrorCode fromErrorCode(String errorCode) {
		Optional<ScoreCardErrorCode> errCd = Arrays.stream(ScoreCardErrorCode.values()).filter(scec -> scec.errorCode.equals(errorCode)).findFirst();
		if (errCd.isPresent()) {
			return errCd.get();
		} else {
			throw new IllegalArgumentException(String.format("Cannot parse %s as a ScoreCardErrorCode.", errorCode));
		}
	}
	
}

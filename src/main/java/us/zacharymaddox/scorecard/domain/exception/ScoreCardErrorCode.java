package us.zacharymaddox.scorecard.domain.exception;

public enum ScoreCardErrorCode {
	
	CANNOT_GET_ID("SC-0001", "Unable to get next Score Card Id."),
	CREATE_FAILED("SC-0002", "Unable to create Score Card."),
	AUTHORIZATION_FAILED("SC-0003", "Unable to authorize Score Card."),
	ILLEGAL_STATE_CHANGE("SC-0004", "Illegal state change requested."),
	UPDATE_FAILED("SC-0005", "Unable to update Score Card.");
	
	private String errorCode;
	private String message;
	
	private ScoreCardErrorCode(String errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String getErrorCode() {
		return errorCode;
	}
	
}

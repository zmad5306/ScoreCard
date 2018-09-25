package us.zacharymaddox.scorecard.core.domain.exception;

public enum ScoreCardErrorCode {
	
	TRANSACTION_DNE("SC-0001", "The request Transaction does not exist.", 404),
	SCORE_CARD_DNE("SC-0002", "The requested Score Card does not exist.", 404),
	SCORE_CARD_ACTION_DNE("SC-0003", "The requested Score Card Action does not exist.", 404),
	SCORE_CARD_ACTION_DEPENDENCY_DNE("SC-0004", "The requested Score Card Action Dependency does not exist.", 404),
	ILLEGAL_STATE_CHANGE_NOT_AUTHORIZED("SC-0005", "The requested Score Card Action update was denied, this action was not authorized.", 403),
	ILLEGAL_STATE_CHANGE("SC-0006", "The requested Score Card Action update was not completed, invalid state change.", 400), 
	SERVICE_DNE("SC-0007", "The requested Service does not exist.", 404), 
	ACTION_DNE("SC-0008", "The requested Action does not exist.", 404);
	
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
	
}

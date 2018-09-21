package us.zacharymaddox.scorecard.domain.exception;

public enum ScoreCardErrorCode {
	
	TRANSACTION_DNE("SC-0001", "The request Transaction does not exist.", 404),
	SCORE_CARD_DNE("SC-0002", "The requested Score Card does not exist.", 404);
	
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

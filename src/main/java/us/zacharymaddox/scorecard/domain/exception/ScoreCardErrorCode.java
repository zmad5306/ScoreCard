package us.zacharymaddox.scorecard.domain.exception;

public enum ScoreCardErrorCode {
	
	TRANSACTION_DNE("The request Transaction does not exist.", 404),
	SCORE_CARD_DNE("The requested Score Card does not exist.", 404),
	SCORE_CARD_ACTION_DNE("The requested Score Card Action does not exist.", 404),
	SCORE_CARD_ACTION_DEPENDENCY_DNE("The requested Score Card Action Dependency does not exist.", 404),
	ILLEGAL_STATE_CHANGE_NOT_AUTHORIZED("The requested Score Card Action update was denied, this action was not authorized.", 403),
	ILLEGAL_STATE_CHANGE("The requested Score Card Action update was not completed, invalid state change.", 400), 
	SERVICE_DNE("The requested Service does not exist.", 404), 
	ACTION_DNE("The requested Action does not exist.", 404), 
	SERVICE_NAME_TAKEN("The requested Service name already exists, select a unique service name.", 400), 
	SERVICE_INVALID("The requested Service passed was invalid.", 400), 
	ACTION_NAME_TAKEN("The requested Action name alrady exists in this Service, select a unique action name.", 400),
	ACTION_INVALID("The requested Action passed was invalid.", 400), 
	TRANSACTION_SAVE_FAILED_BAD_ACTION("Action reqeusted not found.", 404), 
	TRANSACTION_NAME_TAKEN("The requested Transaction name alrady exists, select a unique transaction name.", 400);
	
	private String message;
	private Integer status;
	
	private ScoreCardErrorCode(String message, Integer status) {
		this.message = message;
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public Integer getStatus() {
		return status;
	}
	
}

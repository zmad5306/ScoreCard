package dev.zachmaddox.scorecard.common.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ScoreCardErrorCode {

    INTERNAL_SERVER_ERROR("An unhandled error occured.", HttpStatus.INTERNAL_SERVER_ERROR),
	TRANSACTION_DNE("The request Transaction does not exist.", HttpStatus.NOT_FOUND),
	SCORE_CARD_DNE("The requested Score Card does not exist.", HttpStatus.NOT_FOUND),
	SCORE_CARD_ACTION_DNE("The requested Score Card Action does not exist.", HttpStatus.NOT_FOUND),
	SCORE_CARD_ACTION_DEPENDENCY_DNE("The requested Score Card Action Dependency does not exist.", HttpStatus.NOT_FOUND),
	ILLEGAL_STATE_CHANGE_NOT_AUTHORIZED("The requested Score Card Action update was denied, this action was not authorized.", HttpStatus.FORBIDDEN),
	ILLEGAL_STATE_CHANGE("The requested Score Card Action update was not completed, invalid state change.", HttpStatus.BAD_REQUEST), 
	SERVICE_DNE("The requested Service does not exist.", HttpStatus.NOT_FOUND), 
	ACTION_DNE("The requested Action does not exist.", HttpStatus.NOT_FOUND), 
	SERVICE_NAME_TAKEN("The requested Service name already exists, select a unique service name.", HttpStatus.BAD_REQUEST), 
	SERVICE_INVALID("The requested Service passed was invalid.", HttpStatus.BAD_REQUEST), 
	ACTION_NAME_TAKEN("The requested Action name already exists in this Service, select a unique action name.", HttpStatus.BAD_REQUEST),
	ACTION_INVALID("The requested Action passed was invalid.", HttpStatus.BAD_REQUEST), 
	TRANSACTION_SAVE_FAILED_BAD_ACTION("Action requested not found.", HttpStatus.NOT_FOUND),
	TRANSACTION_NAME_TAKEN("The requested Transaction name already exists, select a unique transaction name.", HttpStatus.BAD_REQUEST),
	CANNOT_DELETE_SERVICE_ACTION_IN_USE("This service cannot be deleted, it has actions that are currently used by transactions.", HttpStatus.BAD_REQUEST), 
	CANNOT_DELETE_ACTION_IN_USE("This action cannot be deleted, its is currently used by transactions.", HttpStatus.BAD_REQUEST),
    ACTION_NOT_FOUND("Action requested not found.", HttpStatus.NOT_FOUND);
	
	private final String message;
	private final HttpStatus status;

    ScoreCardErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

}

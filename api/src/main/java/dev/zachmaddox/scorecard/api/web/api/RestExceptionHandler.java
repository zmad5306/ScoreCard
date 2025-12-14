package dev.zachmaddox.scorecard.api.web.api;

import dev.zachmaddox.scorecard.common.domain.ApiError;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardClientException;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardErrorCode;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler({ScoreCardClientException.class})
    public ResponseEntity<Object> handleClientError(ScoreCardClientException e, WebRequest request) {
        log.error("Handling client error: ", e);
		return handleExceptionInternal(e, new ApiError(e.getError(), e.getError().getMessage()), new HttpHeaders(), e.getError().getStatus(), request);
	}
	
	@ExceptionHandler({ScoreCardServerException.class})
    public ResponseEntity<Object> handleClientError(ScoreCardServerException e, WebRequest request) {
        log.error("Handling server error: ", e);
		return handleExceptionInternal(e, new ApiError(e.getError(), e.getError().getMessage()), new HttpHeaders(), e.getError().getStatus(), request);
	}

    @ExceptionHandler
    public ResponseEntity<Object> handleServerError(Exception e, WebRequest request) {
        log.error("Handling server error: ", e);
        return handleExceptionInternal(e, new ApiError(ScoreCardErrorCode.INTERNAL_SERVER_ERROR, e.getMessage()), new HttpHeaders(), ScoreCardErrorCode.INTERNAL_SERVER_ERROR.getStatus(), request);
    }

}

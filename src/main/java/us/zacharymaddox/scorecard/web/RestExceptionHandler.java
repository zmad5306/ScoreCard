package us.zacharymaddox.scorecard.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import us.zacharymaddox.scorecard.domain.exception.ApiError;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardClientException;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardServerException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler({ScoreCardClientException.class})
	protected ResponseEntity<Object> handleClientError(ScoreCardClientException e, WebRequest request) {
		return handleExceptionInternal(e, new ApiError(e.getError().getStatus(), e.getError().getErrorCode(), e.getError().getMessage()), null, HttpStatus.valueOf(e.getError().getStatus()), request);
	}
	
	@ExceptionHandler({ScoreCardServerException.class})
	protected ResponseEntity<Object> handleClientError(ScoreCardServerException e, WebRequest request) {
		return handleExceptionInternal(e, new ApiError(e.getError().getStatus(), e.getError().getErrorCode(), e.getError().getMessage()), null, HttpStatus.valueOf(e.getError().getStatus()), request);
	}

}

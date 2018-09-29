package us.zacharymaddox.scorecard.api.service.rest;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import com.fasterxml.jackson.databind.ObjectMapper;

import us.zacharymaddox.scorecard.core.domain.exception.ScoreCardClientException;
import us.zacharymaddox.scorecard.core.domain.exception.ScoreCardErrorCode;
import us.zacharymaddox.scorecard.core.domain.exception.ScoreCardServerException;
import us.zacharymaddox.scorecard.domain.ApiError;

public class RestErrorHandler implements ResponseErrorHandler {
	
	private ObjectMapper mapper;
	
	public RestErrorHandler(ObjectMapper mapper) {
		super();
		this.mapper = mapper;
	}

	@Override
	public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
		return (httpResponse.getStatusCode().series() == CLIENT_ERROR 
		          || httpResponse.getStatusCode().series() == SERVER_ERROR);
	}

	@Override
	public void handleError(ClientHttpResponse httpResponse) throws IOException {
		if (httpResponse.getStatusCode().series() == CLIENT_ERROR) {
			ApiError apiError = mapper.readValue(httpResponse.getBody(), ApiError.class);
			throw new ScoreCardClientException(ScoreCardErrorCode.fromErrorCode(apiError.getErrorCode()));
		} else if (httpResponse.getStatusCode().series() == SERVER_ERROR) {
			ApiError apiError = mapper.readValue(httpResponse.getBody(), ApiError.class);
			throw new ScoreCardServerException(ScoreCardErrorCode.fromErrorCode(apiError.getErrorCode()));
		} else {
			throw new UnknownHttpStatusCodeException(
					httpResponse.getRawStatusCode(), 
					httpResponse.getStatusText(), 
					httpResponse.getHeaders(), 
					IOUtils.toString(httpResponse.getBody(), "utf-8").getBytes(),
					Charset.forName("utf-8")
				);
		}
		
	}
}
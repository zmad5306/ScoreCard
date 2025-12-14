package dev.zachmaddox.scorecard.lib.service.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zachmaddox.scorecard.common.domain.ApiError;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardClientException;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardServerException;
import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RestErrorHandler implements ResponseErrorHandler {
	
	private final ObjectMapper mapper;
	
	public RestErrorHandler(ObjectMapper mapper) {
		super();
		this.mapper = mapper;
	}

	@Override
	public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
		return (httpResponse.getStatusCode().is4xxClientError()
		          || httpResponse.getStatusCode().is5xxServerError());
	}

	@Override
	public void handleError(ClientHttpResponse httpResponse) throws IOException {
		if (httpResponse.getStatusCode().is4xxClientError()) {
			ApiError apiError = mapper.readValue(httpResponse.getBody(), ApiError.class);
			throw new ScoreCardClientException(apiError.errorCode());
		} else if (httpResponse.getStatusCode().is5xxServerError()) {
			ApiError apiError = mapper.readValue(httpResponse.getBody(), ApiError.class);
			throw new ScoreCardServerException(apiError.errorCode());
		} else {
			throw new UnknownHttpStatusCodeException(
					httpResponse.getStatusCode().value(),
					httpResponse.getStatusText(), 
					httpResponse.getHeaders(), 
					IOUtils.toString(httpResponse.getBody(), StandardCharsets.UTF_8).getBytes(),
                    StandardCharsets.UTF_8
				);
		}
		
	}

}

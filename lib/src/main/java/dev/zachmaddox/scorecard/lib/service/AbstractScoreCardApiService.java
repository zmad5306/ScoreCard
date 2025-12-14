package dev.zachmaddox.scorecard.lib.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zachmaddox.scorecard.common.domain.*;
import dev.zachmaddox.scorecard.lib.domain.ScoreCard;
import dev.zachmaddox.scorecard.lib.domain.ScoreCardHeader;
import dev.zachmaddox.scorecard.lib.domain.ScoreCardId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Getter
public abstract class AbstractScoreCardApiService implements ScoreCardApiService {

	private final RestTemplate restTemplate;
	private final ObjectMapper mapper;

	@Value("${scorecard.api.baseurl}")
	private String baseUrl;
	
	public ScoreCardHeader convertHeader(String value) {
		try {
			return mapper.readValue(value, ScoreCardHeader.class);
		} catch (IOException e) {
			throw new IllegalArgumentException("Could not parse Score Card Header.");
		}
	}
	public Authorization authorize(String scoreCardHeader) {
		ScoreCardHeader sch = convertHeader(scoreCardHeader);
		return authorize(sch);
	}
	
	public Authorization authorize(ScoreCardHeader scoreCardHeader) {
		AuthorizationRequest req = new AuthorizationRequest(scoreCardHeader.getScoreCardId(), scoreCardHeader.getActionId());
		AuthorizationResult authorization = restTemplate.postForObject(baseUrl + "/scorecard", req, AuthorizationResult.class);
        assert authorization != null;
        return authorization.authorization();
	}
	
	protected ScoreCardId getScoreCardId() {
        return restTemplate.getForObject(baseUrl + "/scorecard/id", ScoreCardId.class);
	}
	
	public DataPage<ScoreCard> getScoreCards(Integer rows, Integer page) {
		ResponseEntity<DataPage<ScoreCard>> response = restTemplate.exchange(
				baseUrl + "/scorecard?rows={rows}&page={page}", 
				HttpMethod.GET,
				null, //requestEntity
				new ParameterizedTypeReference<>() {},
				rows, page
			);
		return response.getBody();
	}
	
	public ScoreCard getScoreCard(Long scoreCardId) {
        return restTemplate.getForObject(baseUrl + "/scorecard/{score_card_id}", ScoreCard.class, scoreCardId);
	}
	
	public DataPage<ScoreCard> getScoreCards(String transactionName, Integer rows, Integer page) {
		ResponseEntity<DataPage<ScoreCard>> response = restTemplate.exchange(
				baseUrl + "/scorecard/filter?transaction_name={transactionName}&rows={rows}&page={page}", 
				HttpMethod.GET, 
				null, //requestEntity
				new ParameterizedTypeReference<>() {},
				transactionName, rows, page
			);
		return response.getBody();
	}
	
	public List<ScoreCard> getScoreCards(ScoreCardActionStatus status, String transactionName, Integer rows, Integer page) {
		ResponseEntity<List<ScoreCard>> response = restTemplate.exchange(
				baseUrl + "/scorecard/status?score_card_action_status={status}&transaction_name={transactionName}&rows={rows}&page={page}", 
				HttpMethod.GET, 
				null, //requestEntity
				new ParameterizedTypeReference<>() {},
				status, transactionName, rows, page
			);
		return response.getBody();
	}
	
	@Override
	public void updateStatus(ScoreCardHeader scoreCardHeader, ScoreCardActionStatus status) {
		updateStatus(scoreCardHeader, status, null);
	}
	
	@Override
	public void updateStatus(String scoreCardHeader, ScoreCardActionStatus status, Map<String, String> metadata) {
		updateStatus(convertHeader(scoreCardHeader), status, metadata);
	}
	
	@Override
	public void updateStatus(String scoreCardHeader, ScoreCardActionStatus status) {
		updateStatus(convertHeader(scoreCardHeader), status, null);
	}

}

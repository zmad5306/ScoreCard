package us.zacharymaddox.scorecard.api.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import us.zacharymaddox.scorecard.api.domain.ScoreCard;
import us.zacharymaddox.scorecard.api.domain.ScoreCardHeader;
import us.zacharymaddox.scorecard.domain.Authorization;
import us.zacharymaddox.scorecard.domain.AuthorizationRequest;
import us.zacharymaddox.scorecard.domain.AuthorizationResult;
import us.zacharymaddox.scorecard.domain.DataPage;
import us.zacharymaddox.scorecard.domain.ScoreCardActionStatus;
import us.zacharymaddox.scorecard.domain.ScoreCardId;

public abstract class AbstractScoreCardApiService implements ScoreCardApiService {
	
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ObjectMapper mapper;
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
		AuthorizationRequest req = new AuthorizationRequest();
		req.setActionId(scoreCardHeader.getActionId());
		req.setScoreCardId(scoreCardHeader.getScoreCardId());
		ResponseEntity<AuthorizationResult> result = restTemplate.postForEntity(baseUrl + "/scorecard", req, AuthorizationResult.class);
		AuthorizationResult aResult = result.getBody();
		return aResult.getAuthorization();
	}
	
	protected ScoreCardId getScoreCardId() {
        ScoreCardId id = restTemplate.getForObject(baseUrl + "/scorecard/id", ScoreCardId.class);
        return id;
	}
	
	public DataPage<ScoreCard> getScoreCards(Integer rows, Integer page) {
		ResponseEntity<DataPage<ScoreCard>> response = restTemplate.exchange(
				baseUrl + "/scorecard?rows={rows}&page={page}", 
				HttpMethod.GET, 
				null, //requestEntity
				new ParameterizedTypeReference<DataPage<ScoreCard>>() {},
				rows, page
			);
		return response.getBody();
	}
	
	public ScoreCard getScoreCard(Long scoreCardId) {
		ScoreCard scoreCard = restTemplate.getForObject(baseUrl + "/scorecard/{score_card_id}", ScoreCard.class, scoreCardId);
		return scoreCard;
	}
	
	public DataPage<ScoreCard> getScoreCards(String transactionName, Integer rows, Integer page) {
		ResponseEntity<DataPage<ScoreCard>> response = restTemplate.exchange(
				baseUrl + "/scorecard/filter?transaction_name={transactionName}&rows={rows}&page={page}", 
				HttpMethod.GET, 
				null, //requestEntity
				new ParameterizedTypeReference<DataPage<ScoreCard>>() {},
				transactionName, rows, page
			);
		return response.getBody();
	}
	
	public List<ScoreCard> getScoreCards(ScoreCardActionStatus status, String transactionName, Integer rows, Integer page) {
		ResponseEntity<List<ScoreCard>> response = restTemplate.exchange(
				baseUrl + "/scorecard/status?score_card_action_status={status}&transaction_name={transactionName}&rows={rows}&page={page}", 
				HttpMethod.GET, 
				null, //requestEntity
				new ParameterizedTypeReference<List<ScoreCard>>() {},
				status, transactionName, rows, page
			);
		return response.getBody();
	}
	
	public String getBaseUrl() {
		return baseUrl;
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

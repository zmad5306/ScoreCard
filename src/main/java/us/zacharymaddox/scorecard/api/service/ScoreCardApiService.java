package us.zacharymaddox.scorecard.api.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import us.zacharymaddox.scorecard.api.domain.ScoreCardHeader;
import us.zacharymaddox.scorecard.common.domain.Authorization;
import us.zacharymaddox.scorecard.common.domain.AuthorizationRequest;
import us.zacharymaddox.scorecard.common.domain.AuthorizationResult;
import us.zacharymaddox.scorecard.common.domain.ScoreCardActionStatus;
import us.zacharymaddox.scorecard.common.domain.ScoreCardId;
import us.zacharymaddox.scorecard.api.domain.ScoreCard;
import us.zacharymaddox.scorecard.common.domain.ScoreCardStatus;
import us.zacharymaddox.scorecard.common.domain.UpdateRequest;

@Service
public class ScoreCardApiService {
	
	@Value("${scorecard.api.baseurl}")
	private String baseUrl;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private ObjectMapper mapper;
	
	private ScoreCardHeader convertHeader(String value) {
		try {
			return mapper.readValue(value, ScoreCardHeader.class);
		} catch (IOException e) {
			throw new IllegalArgumentException("Could not parse Score Card Header.");
		}
	}
	
	public Authorization authorize(String scoreCardHeader) {
		ScoreCardHeader sch = convertHeader(scoreCardHeader);
		AuthorizationRequest req = new AuthorizationRequest();
		req.setActionId(sch.getActionId());
		req.setScoreCardId(sch.getScoreCardId());
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<AuthorizationResult> result = restTemplate.postForEntity(baseUrl + "/scorecard", req, AuthorizationResult.class);
		AuthorizationResult aResult = result.getBody();
		return aResult.getAuthorization();
	}
	
	public void updateStatus(String scoreCardHeader, ScoreCardActionStatus status) {
		ScoreCardHeader sch = convertHeader(scoreCardHeader);
		UpdateRequest request = new UpdateRequest(sch.getScoreCardId(), sch.getActionId(), status);
		jmsTemplate.convertAndSend("scorecard", request, new MessageSelectorPostProcessor("UPDATE"));
	}
	
	public ScoreCardId getScoreCardId() {
		RestTemplate restTemplate = new RestTemplate();
        ScoreCardId id = restTemplate.getForObject(baseUrl + "/scorecard/id", ScoreCardId.class);
        return id;
	}
	
	public List<ScoreCard> getScoreCards(ScoreCardStatus scoreCardStatus, Integer rows, Integer page) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<ScoreCard>> response = restTemplate.exchange(
				baseUrl + "/scorecard?score_card_status={status}&rows={rows}&page={page}", 
				HttpMethod.GET, 
				null, //requestEntity
				new ParameterizedTypeReference<List<ScoreCard>>() {},
				scoreCardStatus, rows, page
			);
		return response.getBody();
	}

}

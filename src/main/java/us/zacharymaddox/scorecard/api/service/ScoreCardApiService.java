package us.zacharymaddox.scorecard.api.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import us.zacharymaddox.scorecard.api.domain.Action;
import us.zacharymaddox.scorecard.api.domain.ScoreCard;
import us.zacharymaddox.scorecard.api.domain.ScoreCardHeader;
import us.zacharymaddox.scorecard.api.domain.Transaction;
import us.zacharymaddox.scorecard.domain.Authorization;
import us.zacharymaddox.scorecard.domain.AuthorizationRequest;
import us.zacharymaddox.scorecard.domain.AuthorizationResult;
import us.zacharymaddox.scorecard.domain.CreateRequest;
import us.zacharymaddox.scorecard.domain.ScoreCardActionStatus;
import us.zacharymaddox.scorecard.domain.ScoreCardId;
import us.zacharymaddox.scorecard.domain.ScoreCardStatus;
import us.zacharymaddox.scorecard.domain.Transport;
import us.zacharymaddox.scorecard.domain.UpdateRequest;

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
		updateStatus(convertHeader(scoreCardHeader), status, null);
	}
	
	public void updateStatus(ScoreCardHeader scoreCardHeader, ScoreCardActionStatus status) {
		updateStatus(scoreCardHeader, status, null);
	}
	
	public void updateStatus(String scoreCardHeader, ScoreCardActionStatus status, Map<String, String> metadata) {
		updateStatus(convertHeader(scoreCardHeader), status, metadata);
	}
	
	public void updateStatus(ScoreCardHeader scoreCardHeader, ScoreCardActionStatus status, Map<String, String> metadata) {
		UpdateRequest request = new UpdateRequest(scoreCardHeader.getScoreCardId(), scoreCardHeader.getActionId(), status, metadata);
		jmsTemplate.convertAndSend("scorecard", request, new MessageSelectorPostProcessor("UPDATE"));
	}
	
	public void updateStatus(String scoreCardHeader, ScoreCardActionStatus status, boolean useHttp) {
		updateStatus(convertHeader(scoreCardHeader), status, null, useHttp);
	}
	
	public void updateStatus(String scoreCardHeader, ScoreCardActionStatus status, Map<String, String> metadata, boolean useHttp) {
		updateStatus(convertHeader(scoreCardHeader), status, metadata, useHttp);
	}
	
	public void updateStatus(ScoreCardHeader scoreCardHeader, ScoreCardActionStatus status, Map<String, String> metadata, boolean useHttp) {
		if (!useHttp) updateStatus(scoreCardHeader, status, metadata);
		else {
			UpdateRequest request = new UpdateRequest(scoreCardHeader.getScoreCardId(), scoreCardHeader.getActionId(), status, metadata);
			RestTemplate restTemplate = new RestTemplate();
			URI uri;
			
			try {
				uri = new URI(baseUrl + "/scorecard/" + scoreCardHeader.getScoreCardId());
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
			
			RequestEntity<UpdateRequest> requestEntity = RequestEntity.post(uri).body(request);
			restTemplate.exchange(requestEntity, new ParameterizedTypeReference<Object>() {});
		}
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
	
	public List<ScoreCard> getFailedScoreCards(String transactionName, Integer rows, Integer page) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<ScoreCard>> response = restTemplate.exchange(
				baseUrl + "/scorecard/failed?transaction_name={transactionName}&rows={rows}&page={page}", 
				HttpMethod.GET, 
				null, //requestEntity
				new ParameterizedTypeReference<List<ScoreCard>>() {},
				transactionName, rows, page
			);
		return response.getBody();
	}
	
	public ScoreCardId getScoreCardId(Transaction transaction) {
		ScoreCardId id = getScoreCardId();
		return createScoreCard(id, transaction);
	}

	public ScoreCardId createScoreCard(ScoreCardId id, Transaction transaction) {
		jmsTemplate.convertAndSend("scorecard", new CreateRequest(id.getScoreCardId(), transaction.getTransactionId()), new MessageSelectorPostProcessor("CREATE"));
		return id;
	}
	
	public ScoreCardId createScoreCard(Transaction transaction, boolean useHttp) {
		ScoreCardId id = getScoreCardId();
		return createScoreCard(id, transaction, useHttp);
	}
	
	public ScoreCardId createScoreCard(ScoreCardId id, Transaction transaction, boolean useHttp) {
		if (!useHttp) createScoreCard(id, transaction);
		else {
			RestTemplate restTemplate = new RestTemplate();
			CreateRequest request = new CreateRequest(id.getScoreCardId(), transaction.getTransactionId());
			
			restTemplate.put(baseUrl + "/scorecard", request);
		}
		return id;
	}
	
	public void wrapAndSend(ScoreCardId id, Transaction transaction, Action action, Object message) {
		if (Transport.QUEUE.equals(action.getService().getTransport())) {
			jmsTemplate.convertAndSend(
					action.getService().getPath(),
					message,
					new ScoreCardPostProcessor(new ScoreCardHeader(id.getScoreCardId(), action.getActionId(), action.getPath()), mapper)
				);
		}
		else {
			// TODO implement HTTP
			throw new IllegalArgumentException(String.format("Unsupported transport %s", action.getService().getTransport()));
		}
	}

}

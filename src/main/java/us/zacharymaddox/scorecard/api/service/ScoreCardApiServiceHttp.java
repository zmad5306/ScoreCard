package us.zacharymaddox.scorecard.api.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
//import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import us.zacharymaddox.scorecard.api.domain.Action;
import us.zacharymaddox.scorecard.api.domain.ScoreCardHeader;
import us.zacharymaddox.scorecard.api.domain.Transaction;
import us.zacharymaddox.scorecard.domain.CreateRequest;
import us.zacharymaddox.scorecard.domain.ScoreCardActionStatus;
import us.zacharymaddox.scorecard.domain.ScoreCardId;
import us.zacharymaddox.scorecard.domain.UpdateRequest;

//@Service
public class ScoreCardApiServiceHttp extends AbstractScoreCardApiService implements ScoreCardApiService {

	@Override
	public void updateStatus(ScoreCardHeader scoreCardHeader, ScoreCardActionStatus status, Map<String, String> metadata) {
		UpdateRequest request = new UpdateRequest(scoreCardHeader.getScoreCardId(), scoreCardHeader.getActionId(), status, metadata);
		RestTemplate restTemplate = new RestTemplate();
		URI uri;
		
		try {
			uri = new URI(getBaseUrl() + "/scorecard/" + scoreCardHeader.getScoreCardId());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		
		RequestEntity<UpdateRequest> requestEntity = RequestEntity.post(uri).body(request);
		restTemplate.exchange(requestEntity, new ParameterizedTypeReference<Object>() {});
	}
	
	@Override
	public ScoreCardId createScoreCard(Transaction transaction) {
		ScoreCardId scoreCardId = getScoreCardId();
		RestTemplate restTemplate = new RestTemplate();
		CreateRequest request = new CreateRequest(scoreCardId.getScoreCardId(), transaction.getTransactionId());
		
		restTemplate.put(getBaseUrl() + "/scorecard", request);
		return scoreCardId;
	}

	@Override
	public void wrapAndSend(ScoreCardId id, Transaction transaction, Action action, Object message) {
		throw new UnsupportedOperationException("not implemented yet");
	}
}

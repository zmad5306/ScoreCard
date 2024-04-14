package us.zacharymaddox.scorecard.api.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
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
@Profile({"api"})
public class ScoreCardApiServiceHttp extends AbstractScoreCardApiService implements ScoreCardApiService {
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public void updateStatus(ScoreCardHeader scoreCardHeader, ScoreCardActionStatus status, Map<String, String> metadata) {
		UpdateRequest request = new UpdateRequest(scoreCardHeader.getScoreCardId(), scoreCardHeader.getActionId(), status, metadata);
		URI uri;
		
		try {
			uri = new URI(getBaseUrl() + "/scorecard/" + scoreCardHeader.getScoreCardId());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		
		RequestEntity<UpdateRequest> requestEntity = RequestEntity.post(uri).body(request);
		restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {});
	}
	
	@Override
	public ScoreCardId createScoreCard(Transaction transaction) {
		ScoreCardId scoreCardId = getScoreCardId();
		CreateRequest request = new CreateRequest(scoreCardId.getScoreCardId(), transaction.getTransactionId());
		
		restTemplate.put(getBaseUrl() + "/scorecard", request);
		return scoreCardId;
	}

	@Override
	public void wrapAndSend(ScoreCardId id, Transaction transaction, Action action, Object message) {
		throw new UnsupportedOperationException("not implemented yet");
	}
}

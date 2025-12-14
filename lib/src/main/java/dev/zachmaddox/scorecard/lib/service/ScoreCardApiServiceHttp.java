package dev.zachmaddox.scorecard.lib.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zachmaddox.scorecard.common.domain.CreateRequest;
import dev.zachmaddox.scorecard.common.domain.ScoreCardActionStatus;
import dev.zachmaddox.scorecard.common.domain.UpdateRequest;
import dev.zachmaddox.scorecard.lib.domain.Action;
import dev.zachmaddox.scorecard.lib.domain.ScoreCardHeader;
import dev.zachmaddox.scorecard.lib.domain.ScoreCardId;
import dev.zachmaddox.scorecard.lib.domain.Transaction;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Service
public class ScoreCardApiServiceHttp extends AbstractScoreCardApiService implements ScoreCardApiService {

    public ScoreCardApiServiceHttp(RestTemplate restTemplate, ObjectMapper mapper) {
        super(restTemplate, mapper);
    }

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
		getRestTemplate().exchange(requestEntity, new ParameterizedTypeReference<>() {});
	}
	
	@Override
	public ScoreCardId createScoreCard(Transaction transaction) {
		ScoreCardId scoreCardId = getScoreCardId();
		CreateRequest request = new CreateRequest(scoreCardId.getScoreCardId(), transaction.getTransactionId());

        getRestTemplate().put(getBaseUrl() + "/scorecard", request);
		return scoreCardId;
	}

	@Override
	public void wrapAndSend(ScoreCardId id, Transaction transaction, Action action, Object message) {
		throw new UnsupportedOperationException("not implemented yet");
	}
}

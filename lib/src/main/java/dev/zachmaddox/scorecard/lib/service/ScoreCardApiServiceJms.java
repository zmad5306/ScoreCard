package dev.zachmaddox.scorecard.lib.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zachmaddox.scorecard.common.domain.CreateRequest;
import dev.zachmaddox.scorecard.common.domain.ScoreCardActionStatus;
import dev.zachmaddox.scorecard.common.domain.UpdateRequest;
import dev.zachmaddox.scorecard.lib.domain.Action;
import dev.zachmaddox.scorecard.lib.domain.ScoreCardHeader;
import dev.zachmaddox.scorecard.lib.domain.ScoreCardId;
import dev.zachmaddox.scorecard.lib.domain.Transaction;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ScoreCardApiServiceJms extends AbstractScoreCardApiService implements ScoreCardApiService {

	private final JmsTemplate jmsTemplate;

    public ScoreCardApiServiceJms(RestTemplate restTemplate, ObjectMapper mapper, JmsTemplate jmsTemplate) {
        super(restTemplate, mapper);
        this.jmsTemplate = jmsTemplate;
    }

    @Override
	public void updateStatus(ScoreCardHeader scoreCardHeader, ScoreCardActionStatus status, Map<String, String> metadata) {
		UpdateRequest request = new UpdateRequest(scoreCardHeader.getScoreCardId(), scoreCardHeader.getActionId(), status, metadata);
		jmsTemplate.convertAndSend("scorecard", request, new MessageSelectorPostProcessor("UPDATE"));
	}
	
	@Override
	public ScoreCardId createScoreCard(Transaction transaction) {
		ScoreCardId scoreCardId = getScoreCardId();
		jmsTemplate.convertAndSend("scorecard", new CreateRequest(scoreCardId.getScoreCardId(), transaction.getTransactionId()), new MessageSelectorPostProcessor("CREATE"));
		return scoreCardId;
	}
	
	@Override
	public void wrapAndSend(ScoreCardId id, Transaction transaction, Action action, Object message) {
		jmsTemplate.convertAndSend(
				action.getService().getPath(),
				message,
				new ScoreCardPostProcessor(new ScoreCardHeader(id.getScoreCardId(), action.getActionId(), action.getPath()), getMapper())
			);
	}

}

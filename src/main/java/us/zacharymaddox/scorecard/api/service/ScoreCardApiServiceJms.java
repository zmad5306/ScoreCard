package us.zacharymaddox.scorecard.api.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import us.zacharymaddox.scorecard.api.domain.Action;
import us.zacharymaddox.scorecard.api.domain.ScoreCardHeader;
import us.zacharymaddox.scorecard.api.domain.Transaction;
import us.zacharymaddox.scorecard.domain.CreateRequest;
import us.zacharymaddox.scorecard.domain.ScoreCardActionStatus;
import us.zacharymaddox.scorecard.domain.ScoreCardId;
import us.zacharymaddox.scorecard.domain.UpdateRequest;

@Service
public class ScoreCardApiServiceJms extends AbstractScoreCardApiService implements ScoreCardApiService {
	
	@Value("${scorecard.api.baseurl}")
	private String baseUrl;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private ObjectMapper mapper;
	
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
				new ScoreCardPostProcessor(new ScoreCardHeader(id.getScoreCardId(), action.getActionId(), action.getPath()), mapper)
			);
	}

}

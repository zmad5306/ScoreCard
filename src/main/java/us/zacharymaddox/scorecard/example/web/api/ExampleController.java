package us.zacharymaddox.scorecard.example.web.api;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.databind.ObjectMapper;

import us.zacharymaddox.scorecard.api.domain.Action;
import us.zacharymaddox.scorecard.api.domain.ScoreCardHeader;
import us.zacharymaddox.scorecard.api.domain.Transaction;
import us.zacharymaddox.scorecard.api.service.MessageSelectorPostProcessor;
import us.zacharymaddox.scorecard.api.service.ScoreCardApiService;
import us.zacharymaddox.scorecard.api.service.ScoreCardPostProcessor;
import us.zacharymaddox.scorecard.api.service.TransactionApiService;
import us.zacharymaddox.scorecard.common.domain.CreateRequest;
import us.zacharymaddox.scorecard.common.domain.ScoreCardId;

@RestController
@RequestMapping("/app/example")
@Profile({"test-app"})
public class ExampleController {
	
	@Autowired
	private ScoreCardApiService scoreCardApiService;
	@Autowired
	private TransactionApiService transactionApiService;
	private final String transactionName = "transaction1";
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private ObjectMapper mapper;
	
	@GetMapping
	public void startExampleFlow() throws RestClientException, URISyntaxException {
		Transaction transaction = transactionApiService.getTransactionByName(transactionName);
		ScoreCardId id = scoreCardApiService.getScoreCardId();
        jmsTemplate.convertAndSend("scorecard", new CreateRequest(id.getScoreCardId(), transaction.getTransactionId()), new MessageSelectorPostProcessor("CREATE"));
        for (Action action : transaction.getActions()) {
        	jmsTemplate.convertAndSend(action.getService().getPath(), "Hello world!", new ScoreCardPostProcessor(new ScoreCardHeader(id.getScoreCardId(), action.getActionId(), action.getPath()), mapper));
        }
	}

}

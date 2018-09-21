package us.zacharymaddox.scorecard.example.web;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import us.zacharymaddox.scorecard.domain.CreateRequest;
import us.zacharymaddox.scorecard.domain.ScoreCardId;
import us.zacharymaddox.scorecard.example.domain.ApiAction;
import us.zacharymaddox.scorecard.example.domain.ApiTransaction;
import us.zacharymaddox.scorecard.example.service.MessageSelectorPostProcessor;
import us.zacharymaddox.scorecard.example.service.ScoreCardHeader;
import us.zacharymaddox.scorecard.example.service.ScoreCardPostProcessor;

@RestController
@RequestMapping("/example")
@Profile({"test-app"})
public class ExampleController {
	
	private final Long transactionId = 5L;
	private Logger logger = LoggerFactory.getLogger(ExampleController.class);
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private ObjectMapper mapper;
	
	@GetMapping
	public void startExampleFlow() throws RestClientException, URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();
        ScoreCardId id = restTemplate.getForObject(new URI("http://localhost:8080/api/v1/scorecard"), ScoreCardId.class);
        logger.info("got Score Card Id {}", id.getScoreCardId());
        jmsTemplate.convertAndSend("scorecard", new CreateRequest(id.getScoreCardId(), transactionId), new MessageSelectorPostProcessor("CREATE"));
        
        ApiTransaction transaction = restTemplate.getForObject("http://localhost:8080/api/v1/transaction/" + transactionId, ApiTransaction.class);
        logger.info("got Transaction Id {}", transaction.getTransactionId());
        
        for (ApiAction action : transaction.getActions()) {
        	jmsTemplate.convertAndSend(action.getService().getPath(), "", new ScoreCardPostProcessor(new ScoreCardHeader(id.getScoreCardId(), action.getActionId(), action.getPath()), mapper));
        }
	}

}

package us.zacharymaddox.scorecard.example.web;

import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import us.zacharymaddox.scorecard.api.domain.ApiAction;
import us.zacharymaddox.scorecard.api.domain.ApiTransaction;
import us.zacharymaddox.scorecard.api.domain.ScoreCardHeader;
import us.zacharymaddox.scorecard.api.service.MessageSelectorPostProcessor;
import us.zacharymaddox.scorecard.api.service.ScoreCardPostProcessor;
import us.zacharymaddox.scorecard.common.domain.CreateRequest;
import us.zacharymaddox.scorecard.common.domain.ScoreCardId;

@RestController
@RequestMapping("/app/example")
@Profile({"test-app"})
public class ExampleController {
	
	@Value("${scorecard.example.baseurl}")
	private String baseUrl;
	private final Long transactionId = 5L;
	private Logger logger = LoggerFactory.getLogger(ExampleController.class);
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private ObjectMapper mapper;
	
	@GetMapping
	public void startExampleFlow() throws RestClientException, URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();
        ScoreCardId id = restTemplate.getForObject(baseUrl + "/scorecard", ScoreCardId.class);
        logger.info("got Score Card Id {}", id.getScoreCardId());
        jmsTemplate.convertAndSend("scorecard", new CreateRequest(id.getScoreCardId(), transactionId), new MessageSelectorPostProcessor("CREATE"));
        
        ApiTransaction transaction = restTemplate.getForObject(baseUrl + "/transaction/" + transactionId, ApiTransaction.class);
        logger.info("got Transaction Id {}", transaction.getTransactionId());
        
        for (ApiAction action : transaction.getActions()) {
        	jmsTemplate.convertAndSend(action.getService().getPath(), "", new ScoreCardPostProcessor(new ScoreCardHeader(id.getScoreCardId(), action.getActionId(), action.getPath()), mapper));
        }
	}

}

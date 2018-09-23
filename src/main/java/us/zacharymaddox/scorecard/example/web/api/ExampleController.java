package us.zacharymaddox.scorecard.example.web.api;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import us.zacharymaddox.scorecard.api.domain.Action;
import us.zacharymaddox.scorecard.api.domain.Transaction;
import us.zacharymaddox.scorecard.api.service.ScoreCardApiService;
import us.zacharymaddox.scorecard.api.service.TransactionApiService;
import us.zacharymaddox.scorecard.domain.ScoreCardId;

@RestController
@RequestMapping("/app/example")
@Profile({"test-app"})
public class ExampleController {
	
	@Autowired
	private ScoreCardApiService scoreCardApiService;
	@Autowired
	private TransactionApiService transactionApiService;
	
	private final String transactionName = "transaction1";
	
	@GetMapping(produces="application/json")
	public ScoreCardId startExampleFlow() throws RestClientException, URISyntaxException {
		Transaction transaction = transactionApiService.getTransactionByName(transactionName);
        ScoreCardId id = scoreCardApiService.getScoreCardId(transaction);
        for (Action action : transaction.getActions()) {
        	String message = String.format("Hello world from %s, %s!", action.getService().getName(), action.getName());
        	scoreCardApiService.wrapAndSend(id, transaction, action, message);
        }
        return id;
	}

}

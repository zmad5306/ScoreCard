package dev.zachmaddox.scorecard.example.basic.web.api;

import dev.zachmaddox.scorecard.lib.domain.Action;
import dev.zachmaddox.scorecard.lib.domain.ScoreCardId;
import dev.zachmaddox.scorecard.lib.domain.Transaction;
import dev.zachmaddox.scorecard.lib.service.ScoreCardApiService;
import dev.zachmaddox.scorecard.lib.service.TransactionApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

@RestController
@RequestMapping("/app/example")
@Tag(name = "Example Flow", description = "Triggers a demo transaction and returns the score card id")
public class ExampleController {
	
	private final ScoreCardApiService scoreCardApiService;
	private final TransactionApiService transactionApiService;

    public ExampleController(@Qualifier("scoreCardApiServiceJms") ScoreCardApiService scoreCardApiService, TransactionApiService transactionApiService) {
        this.scoreCardApiService = scoreCardApiService;
        this.transactionApiService = transactionApiService;
    }

    @GetMapping(produces="application/json")
	@Operation(summary = "Start demo flow", description = "Starts transaction1, enqueues messages for each action, and returns the created score card id")
	public ScoreCardId startExampleFlow() throws RestClientException {
        Transaction transaction = transactionApiService.getTransactionByName("transaction1");
        ScoreCardId id = scoreCardApiService.createScoreCard(transaction);
        for (Action action : transaction.getActions()) {
        	String message = String.format("Hello world from %s, %s!", action.getService().getName(), action.getName());
        	scoreCardApiService.wrapAndSend(id, transaction, action, message);
        }
        return id;
	}

}

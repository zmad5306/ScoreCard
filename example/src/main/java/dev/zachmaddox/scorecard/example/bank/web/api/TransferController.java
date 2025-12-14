package dev.zachmaddox.scorecard.example.bank.web.api;

import dev.zachmaddox.scorecard.example.bank.domain.CreditRequest;
import dev.zachmaddox.scorecard.example.bank.domain.DebitRequest;
import dev.zachmaddox.scorecard.lib.domain.Action;
import dev.zachmaddox.scorecard.lib.domain.ScoreCardId;
import dev.zachmaddox.scorecard.lib.domain.Transaction;
import dev.zachmaddox.scorecard.lib.service.ScoreCardApiService;
import dev.zachmaddox.scorecard.lib.service.TransactionApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/example/bank/transfer")
@Tag(name = "Bank Transfer", description = "Simulate a two-step bank transfer with debit/credit actions")
public class TransferController {
	
	private final ScoreCardApiService scoreCardApiService;
	private final TransactionApiService transactionApiService;

    public TransferController(@Qualifier("scoreCardApiServiceJms") ScoreCardApiService scoreCardApiService, TransactionApiService transactionApiService) {
        this.scoreCardApiService = scoreCardApiService;
        this.transactionApiService = transactionApiService;
    }

    @GetMapping(produces="application/json")
	@Operation(summary = "Execute a demo transfer", description = "Creates a score card for the bank-transfer transaction and enqueues debit/credit messages", responses = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Score card created", content = @Content(schema = @Schema(implementation = ScoreCardId.class)))
	})
	public ScoreCardId transfer(
			@RequestParam(value="from_account_id") Long fromAccountId,
			@RequestParam(value="to_account_id") Long toAccountId,
			@RequestParam(value="amount") BigDecimal amount
		) {
        DebitRequest debitRequest = new DebitRequest(fromAccountId, amount);
        CreditRequest creditRequest = new CreditRequest(toAccountId, amount);

        String transferTransactionName = "bank-transfer";
        Transaction transaction = transactionApiService.getTransactionByName(transferTransactionName);
        Action debitAction = transaction.getAction("debit");
        Action creditAction = transaction.getAction("credit");
        
        ScoreCardId id = scoreCardApiService.createScoreCard(transaction);
        scoreCardApiService.wrapAndSend(id, transaction, debitAction, debitRequest);
        scoreCardApiService.wrapAndSend(id, transaction, creditAction, creditRequest);

        return id;
	}
	
}

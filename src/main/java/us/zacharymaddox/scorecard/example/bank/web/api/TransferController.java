package us.zacharymaddox.scorecard.example.bank.web.api;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import us.zacharymaddox.scorecard.api.domain.Action;
import us.zacharymaddox.scorecard.api.domain.Transaction;
import us.zacharymaddox.scorecard.api.service.ScoreCardApiService;
import us.zacharymaddox.scorecard.api.service.TransactionApiService;
import us.zacharymaddox.scorecard.domain.ScoreCardId;
import us.zacharymaddox.scorecard.example.bank.domain.CreditRequest;
import us.zacharymaddox.scorecard.example.bank.domain.DebitRequest;

@RestController
@RequestMapping("/app/bank/transfer")
@Profile({"test-app"})
public class TransferController {
	
	@Autowired
	private ScoreCardApiService scoreCardApiService;
	@Autowired
	private TransactionApiService transactionApiService;
	
	private final String transferTransactionName = "bank-transfer";

	@GetMapping(produces="application/json")
	public ScoreCardId transfer(
			@RequestParam(value="from_account_id", required=true) Long fromAccountId, 
			@RequestParam(value="to_account_id", required=true) Long toAccountId, 
			@RequestParam(required=true) BigDecimal amount
		) {
        DebitRequest debitRequest = new DebitRequest(fromAccountId, amount);
        CreditRequest creditRequest = new CreditRequest(toAccountId, amount);
        
        Transaction transaction = transactionApiService.getTransactionByName(transferTransactionName);
        Action debitAction = transaction.getAction("debit");
        Action creditAction = transaction.getAction("credit");
        
        ScoreCardId id = scoreCardApiService.createScoreCard(transaction);
        scoreCardApiService.wrapAndSend(id, transaction, debitAction, debitRequest);
        scoreCardApiService.wrapAndSend(id, transaction, creditAction, creditRequest);

        return id;
	}
	
}

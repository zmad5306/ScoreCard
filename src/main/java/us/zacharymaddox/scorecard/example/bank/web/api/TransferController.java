package us.zacharymaddox.scorecard.example.bank.web.api;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import us.zacharymaddox.scorecard.api.domain.Action;
import us.zacharymaddox.scorecard.api.domain.ScoreCard;
import us.zacharymaddox.scorecard.api.domain.ScoreCardHeader;
import us.zacharymaddox.scorecard.api.domain.Transaction;
import us.zacharymaddox.scorecard.api.service.ScoreCardApiService;
import us.zacharymaddox.scorecard.api.service.TransactionApiService;
import us.zacharymaddox.scorecard.domain.ScoreCardActionStatus;
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
	
	private final String transactionName = "bank-transfer";

	@GetMapping
	public ScoreCardId transfer(
			@RequestParam(value="from_account_id", required=true) Long fromAccountId, 
			@RequestParam(value="to_account_id", required=true) Long toAccountId, 
			@RequestParam(required=true) BigDecimal amount
		) {
		Transaction transaction = transactionApiService.getTransactionByName(transactionName);
        ScoreCardId id = scoreCardApiService.createScoreCard(transaction);
        
        Action debitAction = transaction.getAction("debit");
        DebitRequest debitRequest = new DebitRequest(fromAccountId, amount);
        scoreCardApiService.wrapAndSend(id, transaction, debitAction, debitRequest);
        
        Action creditAction = transaction.getAction("credit");
        CreditRequest creditRequest = new CreditRequest(toAccountId, amount);
        scoreCardApiService.wrapAndSend(id, transaction, creditAction, creditRequest);

        return id;
	}
	
	private void cancelCredit(ScoreCard scoreCard, Action action) {
		if (ScoreCardActionStatus.PENDING.equals(action.getStatus())) {
			ScoreCardHeader scoreCardHeader = new ScoreCardHeader(scoreCard.getScoreCardId(), action.getActionId(), action.getPath());
			scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.CANCELLED);	
		}
	}
	
	@Scheduled(fixedDelay=10000)
	public void repairFailedTransfers() {
		List<ScoreCard> scoreCards = scoreCardApiService.getFailedScoreCards(transactionName, 100, 0);
		for (ScoreCard scoreCard : scoreCards) {
			for (Action action : scoreCard.getActions()) {
				if (ScoreCardActionStatus.FAILED.equals(action.getStatus()) && "debit".equals(action.getName())) {
					// debit failed, mark credit action as cancelled
					cancelCredit(scoreCard, scoreCard.getAction("credit"));
				}
//				fix failed credit here
			}
		}
	}
	
}

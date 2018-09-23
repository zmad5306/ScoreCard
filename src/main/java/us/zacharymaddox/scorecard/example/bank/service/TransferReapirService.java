package us.zacharymaddox.scorecard.example.bank.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import us.zacharymaddox.scorecard.api.domain.Action;
import us.zacharymaddox.scorecard.api.domain.ScoreCard;
import us.zacharymaddox.scorecard.api.domain.ScoreCardHeader;
import us.zacharymaddox.scorecard.api.domain.Transaction;
import us.zacharymaddox.scorecard.api.service.ScoreCardApiService;
import us.zacharymaddox.scorecard.api.service.TransactionApiService;
import us.zacharymaddox.scorecard.domain.ScoreCardActionStatus;
import us.zacharymaddox.scorecard.domain.ScoreCardId;
import us.zacharymaddox.scorecard.example.bank.domain.BankTransaction;
import us.zacharymaddox.scorecard.example.bank.domain.CreditRequest;

@Service
@Profile({"test-app"})
public class TransferReapirService {
	
	@Autowired
	private ScoreCardApiService scoreCardApiService;
	@Autowired
	private TransactionApiService transactionApiService;
	@Autowired
	private BankTransactionService bankTransactionService;
	
	private Logger logger = LoggerFactory.getLogger(TransferReapirService.class);
	private final String creditTransactionName = "bank-credit";
	private final String transactionIdKey = "transaction_id";
	private final String transferTransactionName = "bank-transfer";
	
	private void cancelCredit(ScoreCard scoreCard, Action action) {
		if (ScoreCardActionStatus.PENDING.equals(action.getStatus())) {
			ScoreCardHeader scoreCardHeader = new ScoreCardHeader(scoreCard.getScoreCardId(), action.getActionId(), action.getPath());
			scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.CANCELLED);	
		}
	}
	
	private void reverseDebit(ScoreCard scoreCard, Action action) {
		Transaction transaction = transactionApiService.getTransactionByName(creditTransactionName);
		ScoreCardId id = scoreCardApiService.getScoreCardId(transaction);
		logger.warn("reversing failed transaction with Score Card Id {}", id.getScoreCardId());
		Action creditAction = transaction.getAction("credit");
		Long transactionId = Long.valueOf(action.getMetadata().get(transactionIdKey));
		Optional<BankTransaction> bt = bankTransactionService.getBankTransactionById(transactionId);
		if (bt.isPresent()) {
			BankTransaction bankTransaction = bt.get();
			CreditRequest creditRequest = new CreditRequest(bankTransaction.getAccount().getAccountId(), bankTransaction.getAmount());
			scoreCardApiService.wrapAndSend(id, transaction, creditAction, creditRequest);	
		}
	}
	
	@Scheduled(fixedDelay=60000)
	@Transactional
	public void repairFailedTransfers() {
		List<ScoreCard> scoreCards = scoreCardApiService.getFailedScoreCards(transferTransactionName, 100, 0);
		for (ScoreCard scoreCard : scoreCards) {
			for (Action action : scoreCard.getActions()) {
				if (ScoreCardActionStatus.FAILED.equals(action.getStatus()) && "debit".equals(action.getName())) {
					// debit failed, mark credit action as cancelled
					cancelCredit(scoreCard, scoreCard.getAction("credit"));
				}
				else if (ScoreCardActionStatus.FAILED.equals(action.getStatus()) && "credit".equals(action.getName())) {
					//credit failed, reverse debit (using score card transaction), mark credit as cancelled
					reverseDebit(scoreCard, scoreCard.getAction("debit"));
					cancelCredit(scoreCard, scoreCard.getAction("credit"));

					ScoreCardHeader scoreCardHeader = new ScoreCardHeader(scoreCard.getScoreCardId(), action.getActionId(), action.getPath());
					scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.CANCELLED);
				}
			}
		}
	}

}

package dev.zachmaddox.scorecard.example.bank.service;

import java.util.List;
import java.util.Optional;

import dev.zachmaddox.scorecard.common.domain.ScoreCardActionStatus;
import dev.zachmaddox.scorecard.lib.domain.*;
import dev.zachmaddox.scorecard.lib.service.ScoreCardApiService;
import dev.zachmaddox.scorecard.lib.service.TransactionApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.zachmaddox.scorecard.example.bank.domain.BankTransaction;
import dev.zachmaddox.scorecard.example.bank.domain.CreditRequest;

@Slf4j
@Service
public class TransferRepairService {

    private final ScoreCardApiService scoreCardApiService;
	private final TransactionApiService transactionApiService;
	private final BankTransactionService bankTransactionService;

    public TransferRepairService(@Qualifier("scoreCardApiServiceJms") ScoreCardApiService scoreCardApiService, TransactionApiService transactionApiService, BankTransactionService bankTransactionService) {
        this.scoreCardApiService = scoreCardApiService;
        this.transactionApiService = transactionApiService;
        this.bankTransactionService = bankTransactionService;
    }

    private void cancelCredit(ScoreCard scoreCard, Action action) {
		if (ScoreCardActionStatus.PENDING.equals(action.getStatus())) {
			ScoreCardHeader scoreCardHeader = new ScoreCardHeader(scoreCard.getScoreCardId(), action.getActionId(), action.getPath());
			scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.CANCELLED);	
		}
	}
	
	private void cancelDebit(ScoreCard scoreCard, Action action) {
		if (ScoreCardActionStatus.FAILED.equals(action.getStatus())) {
			ScoreCardHeader scoreCardHeader = new ScoreCardHeader(scoreCard.getScoreCardId(), action.getActionId(), action.getPath());
			scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.CANCELLED);
		}
	}
	
	private void reverseDebit(Action action) {
        String creditTransactionName = "bank-credit";
        Transaction transaction = transactionApiService.getTransactionByName(creditTransactionName);
		ScoreCardId id = scoreCardApiService.createScoreCard(transaction);
		log.warn("reversing failed transaction with Score Card Id {}", id.getScoreCardId());
		Action creditAction = transaction.getAction("credit");
        String transactionIdKey = "transaction_id";
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
        String transferTransactionName = "bank-transfer";
        List<ScoreCard> scoreCards = scoreCardApiService.getScoreCards(ScoreCardActionStatus.FAILED, transferTransactionName, 100, 0);
		for (ScoreCard scoreCard : scoreCards) {
			for (Action action : scoreCard.getActions()) {
				if (ScoreCardActionStatus.FAILED.equals(action.getStatus()) && "debit".equals(action.getName())) {
					// debit failed, mark credit & debit action as cancelled
					cancelCredit(scoreCard, scoreCard.getAction("credit"));
					cancelDebit(scoreCard, action);
				}
				else if (ScoreCardActionStatus.FAILED.equals(action.getStatus()) && "credit".equals(action.getName())) {
					//credit failed, reverse debit (using scorecard transaction), mark credit as cancelled
					reverseDebit(scoreCard.getAction("debit"));
					cancelCredit(scoreCard, scoreCard.getAction("credit"));

					ScoreCardHeader scoreCardHeader = new ScoreCardHeader(scoreCard.getScoreCardId(), action.getActionId(), action.getPath());
					scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.CANCELLED);
				}
			}
		}
	}

}

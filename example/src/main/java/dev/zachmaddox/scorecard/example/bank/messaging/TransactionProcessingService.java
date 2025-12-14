package dev.zachmaddox.scorecard.example.bank.messaging;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import dev.zachmaddox.scorecard.common.domain.Authorization;
import dev.zachmaddox.scorecard.common.domain.ScoreCardActionStatus;
import dev.zachmaddox.scorecard.lib.domain.WaitException;
import dev.zachmaddox.scorecard.lib.service.ScoreCardApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.zachmaddox.scorecard.example.bank.domain.Account;
import dev.zachmaddox.scorecard.example.bank.domain.BankErrorCode;
import dev.zachmaddox.scorecard.example.bank.domain.CreditRequest;
import dev.zachmaddox.scorecard.example.bank.domain.DebitRequest;
import dev.zachmaddox.scorecard.example.bank.service.AccountService;

@Slf4j
@Component
public class TransactionProcessingService {

    private final ScoreCardApiService scoreCardApiService;
	private final AccountService accountService;

	private final String errorKey = "error_code";

    public TransactionProcessingService(@Qualifier("scoreCardApiServiceJms") ScoreCardApiService scoreCardApiService, AccountService accountService) {
        this.scoreCardApiService = scoreCardApiService;
        this.accountService = accountService;
    }

    private void nsf(String scoreCardHeader) {
		Map<String, String> metadata = new HashMap<>();
		metadata.put(errorKey, BankErrorCode.NSF.name());
		scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.FAILED, metadata);
	}
	
	private void accountDne(String scoreCardHeader) {
		Map<String, String> metadata = new HashMap<>();
		metadata.put(errorKey, BankErrorCode.ACCOUNT_DNE.name());
		scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.FAILED, metadata);
	}
	
	private void performDebit(DebitRequest request, String scoreCardHeader, Account account) {
		Long transactionId = accountService.debitAccount(request, account);
		Map<String, String> metadata = new HashMap<>();
		// set the transaction id in the metadata in case the transaction needs reversed later
        String transactionIdKey = "transaction_id";
        metadata.put(transactionIdKey, transactionId.toString());
		scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.COMPLETED, metadata);
	}
	
	@JmsListener(destination="account", selector="ACTION='debit'")
	@Transactional
	public void debit(DebitRequest request, @Header("SCORE_CARD") String scoreCardHeader) {
		log.info("processing debit request");
		Authorization auth = scoreCardApiService.authorize(scoreCardHeader);
		switch (auth) {
			case CANCEL:
				scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.CANCELLED);
				break;
			case PROCESS:
				Optional<Account> a = accountService.getAccount(request.getAccountId());
				if (a.isPresent()) {
					Account account = a.get();
					if (0 > account.getBalance().compareTo(request.getAmount())) {
						nsf(scoreCardHeader);
					} else {
						performDebit(request, scoreCardHeader, account);
					}
				} else {
					accountDne(scoreCardHeader);					
				}
				break;
			case SKIP:
				break;
			case WAIT:
				throw new WaitException();
		}
	}
	
	private void performCredit(CreditRequest request, String scoreCardHeader, Account account) {
		accountService.creditAccount(request, account);
		scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.COMPLETED);
	}

	@JmsListener(destination="account", selector="ACTION='credit'")
	@Transactional
	public void credit(CreditRequest request, @Header("SCORE_CARD") String scoreCardHeader) {
		log.info("processing credit request");
		Authorization auth = scoreCardApiService.authorize(scoreCardHeader);
		switch (auth) {
			case CANCEL:
				scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.CANCELLED);
				break;
			case PROCESS:
				Optional<Account> a = accountService.getAccount(request.getAccountId());
				if (a.isPresent()) {
					performCredit(request, scoreCardHeader, a.get());					
				} else {
					accountDne(scoreCardHeader);
				}
				break;
			case SKIP:
				break;
			case WAIT:
				throw new WaitException();
		}
		
	}
	
}

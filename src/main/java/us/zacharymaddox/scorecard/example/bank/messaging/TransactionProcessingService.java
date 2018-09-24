package us.zacharymaddox.scorecard.example.bank.messaging;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import us.zacharymaddox.scorecard.api.domain.WaitException;
import us.zacharymaddox.scorecard.api.service.ScoreCardApiService;
import us.zacharymaddox.scorecard.domain.Authorization;
import us.zacharymaddox.scorecard.domain.ScoreCardActionStatus;
import us.zacharymaddox.scorecard.example.bank.domain.Account;
import us.zacharymaddox.scorecard.example.bank.domain.BankErrorCode;
import us.zacharymaddox.scorecard.example.bank.domain.CreditRequest;
import us.zacharymaddox.scorecard.example.bank.domain.DebitRequest;
import us.zacharymaddox.scorecard.example.bank.service.AccountService;

@Component
@Profile({"example"})
public class TransactionProcessingService {
	
	@Autowired
	private ScoreCardApiService scoreCardApiService;
	@Autowired
	private AccountService accountService;
	private Logger logger = LoggerFactory.getLogger(TransactionProcessingService.class);
	private String errorKey = "error_code";
	private String transactionIdKey = "transaction_id";
	
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
		// set the transaction id in the metadata in case it needs reversed later
		metadata.put(transactionIdKey, transactionId.toString());
		scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.COMPLETED, metadata);
	}
	
	@JmsListener(destination="account", selector="ACTION='debit'", containerFactory="myFactory")
	@Transactional
	public void debit(DebitRequest request, @Header("SCORE_CARD") String scoreCardHeader) {
		logger.info("processing debit reqeust");
		Authorization auth = scoreCardApiService.authorize(scoreCardHeader);
		switch (auth) {
			case CANCEL:
				break;
			case PROCESS:
				Optional<Account> a = accountService.getAccount(request.getAccountId());
				if (a.isPresent()) {
					Account account = a.get();
					if (-1 == account.getBalance().compareTo(request.getAmount())) {
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

	@JmsListener(destination="account", selector="ACTION='credit'", containerFactory="myFactory")
	@Transactional
	public void credit(CreditRequest request, @Header("SCORE_CARD") String scoreCardHeader) {
		logger.info("processing credit reqeust");
		Authorization auth = scoreCardApiService.authorize(scoreCardHeader);
		switch (auth) {
			case CANCEL:
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

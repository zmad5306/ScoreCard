package us.zacharymaddox.scorecard.example.bank.messaging;

import java.time.LocalDateTime;
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
import us.zacharymaddox.scorecard.example.bank.domain.BankTransaction;
import us.zacharymaddox.scorecard.example.bank.domain.CreditRequest;
import us.zacharymaddox.scorecard.example.bank.domain.DebitRequest;
import us.zacharymaddox.scorecard.example.bank.domain.TransactionType;
import us.zacharymaddox.scorecard.example.bank.repository.AccountRepository;
import us.zacharymaddox.scorecard.example.bank.repository.BankTransactionRepository;

@Component
@Profile({"test-app"})
public class TransactionProcessingService {
	
	@Autowired
	private ScoreCardApiService scoreCardApiService;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private BankTransactionRepository bankTransactionRepository;
	private Logger logger = LoggerFactory.getLogger(TransactionProcessingService.class);
	
	@JmsListener(destination="account", selector="ACTION='debit'", containerFactory="myFactory")
	@Transactional
	public void debit(DebitRequest request, @Header("SCORE_CARD") String scoreCardHeader) {
		logger.info("processing debit reqeust");
		Authorization auth = scoreCardApiService.authorize(scoreCardHeader);
		switch (auth) {
			case CANCEL:
				break;
			case PROCESS:
				Optional<Account> a = accountRepository.findById(request.getAccountId());
				if (a.isPresent()) {
					Account account = a.get();
					if (-1 == account.getBalance().compareTo(request.getAmount())) {
						Map<String, String> metadata = new HashMap<>();
						metadata.put("error_code", BankErrorCode.NSF.name());
						scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.FAILED, metadata);
					} else {
						BankTransaction bankTransaction = new BankTransaction();
						bankTransaction.setAccount(account);
						bankTransaction.setAmount(request.getAmount());
						bankTransaction.setTimestamp(LocalDateTime.now());
						bankTransaction.setTransactionType(TransactionType.DEBIT);
						bankTransactionRepository.save(bankTransaction);
						
						account.setBalance(account.getBalance().subtract(request.getAmount()));
						accountRepository.save(account);
						
						scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.COMPLETED);
					}
				} else {
					Map<String, String> metadata = new HashMap<>();
					metadata.put("error_code", BankErrorCode.ACCOUNT_DNE.name());
					scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.FAILED, metadata);					
				}
				break;
			case SKIP:
				break;
			case WAIT:
				throw new WaitException();
		}
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
				Optional<Account> a = accountRepository.findById(request.getAccountId());
				if (a.isPresent()) {
					Account account = a.get();
					BankTransaction bankTransaction = new BankTransaction();
					bankTransaction.setAccount(account);
					bankTransaction.setAmount(request.getAmount());
					bankTransaction.setTimestamp(LocalDateTime.now());
					bankTransaction.setTransactionType(TransactionType.CREDIT);
					bankTransactionRepository.save(bankTransaction);
					
					account.setBalance(account.getBalance().add(request.getAmount()));
					accountRepository.save(account);
					
					scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.COMPLETED);						
				} else {
					Map<String, String> metadata = new HashMap<>();
					metadata.put("error_code", BankErrorCode.ACCOUNT_DNE.name());
					scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.FAILED, metadata);
				}
				break;
			case SKIP:
				break;
			case WAIT:
				throw new WaitException();
		}
		
	}

}

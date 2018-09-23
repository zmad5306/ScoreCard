package us.zacharymaddox.scorecard.example.bank.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import us.zacharymaddox.scorecard.example.bank.domain.Account;
import us.zacharymaddox.scorecard.example.bank.domain.BankTransaction;
import us.zacharymaddox.scorecard.example.bank.domain.CreditRequest;
import us.zacharymaddox.scorecard.example.bank.domain.DebitRequest;
import us.zacharymaddox.scorecard.example.bank.domain.TransactionType;
import us.zacharymaddox.scorecard.example.bank.repository.AccountRepository;
import us.zacharymaddox.scorecard.example.bank.repository.BankTransactionRepository;

@Service
@Profile({"test-app"})
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private BankTransactionRepository bankTransactionRepository;
	
	@Transactional(readOnly=true)
	public Optional<Account> getAccount(Long accountId) {
		return accountRepository.findById(accountId);
	}
	
	@Transactional
	public Long debitAccount(DebitRequest request, Account account) {
		BankTransaction bankTransaction = new BankTransaction();
		bankTransaction.setAccount(account);
		bankTransaction.setAmount(request.getAmount());
		bankTransaction.setTimestamp(LocalDateTime.now());
		bankTransaction.setTransactionType(TransactionType.DEBIT);
		bankTransaction = bankTransactionRepository.save(bankTransaction);
		
		account.setBalance(account.getBalance().subtract(request.getAmount()));
		accountRepository.save(account);
		
		return bankTransaction.getTransactionId();
	}
	
	@Transactional
	public void creditAccount(CreditRequest request, Account account) {
		BankTransaction bankTransaction = new BankTransaction();
		bankTransaction.setAccount(account);
		bankTransaction.setAmount(request.getAmount());
		bankTransaction.setTimestamp(LocalDateTime.now());
		bankTransaction.setTransactionType(TransactionType.CREDIT);
		bankTransactionRepository.save(bankTransaction);
		
		account.setBalance(account.getBalance().add(request.getAmount()));
		accountRepository.save(account);
	}
	
}

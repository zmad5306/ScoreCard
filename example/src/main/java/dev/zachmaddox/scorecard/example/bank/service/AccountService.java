package dev.zachmaddox.scorecard.example.bank.service;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.zachmaddox.scorecard.example.bank.domain.Account;
import dev.zachmaddox.scorecard.example.bank.domain.BankTransaction;
import dev.zachmaddox.scorecard.example.bank.domain.CreditRequest;
import dev.zachmaddox.scorecard.example.bank.domain.DebitRequest;
import dev.zachmaddox.scorecard.example.bank.domain.TransactionType;
import dev.zachmaddox.scorecard.example.bank.repository.AccountRepository;
import dev.zachmaddox.scorecard.example.bank.repository.BankTransactionRepository;

@RequiredArgsConstructor
@Service
public class AccountService {

	private final AccountRepository accountRepository;
	private final BankTransactionRepository bankTransactionRepository;
	
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

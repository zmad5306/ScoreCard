package us.zacharymaddox.scorecard.core.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import us.zacharymaddox.scorecard.core.domain.Transaction;
import us.zacharymaddox.scorecard.core.repository.TransactionRepository;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardClientException;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardErrorCode;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;
	
	@Transactional(readOnly=true)
	public List<Transaction> getTransactions() {
		return transactionRepository.findAll();
	}
	
	@Transactional(readOnly=true)
	public Transaction getTransaction(Long transactionId) {
		Optional<Transaction> t = transactionRepository.findById(transactionId);
		if (!t.isPresent()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.TRANSACTION_DNE);
		}
		return t.get();
	}
	
	@Transactional(readOnly=true)
	public Transaction getTransactionByName(String name) {
		Optional<Transaction> t = transactionRepository.findByName(name);
		if (!t.isPresent()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.TRANSACTION_DNE);
		}
		
		return t.get();
	}
	
}

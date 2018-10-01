package us.zacharymaddox.scorecard.core.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import us.zacharymaddox.scorecard.core.domain.Action;
import us.zacharymaddox.scorecard.core.domain.Transaction;
import us.zacharymaddox.scorecard.core.domain.TransactionAction;
import us.zacharymaddox.scorecard.core.repository.ActionRepository;
import us.zacharymaddox.scorecard.core.repository.TransactionRepository;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardClientException;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardErrorCode;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardServerException;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private ActionRepository actionRepository;
	
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

	@Transactional
	public Transaction save(Transaction transaction) {
		Optional<Transaction> existing = transactionRepository.findByName(transaction.getName());
		if (existing.isPresent()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.TRANSACTION_NAME_TAKEN);
		}
		for (TransactionAction a : transaction.getActions()) {
			Optional<Action> action = actionRepository.findById(a.getAction().getActionId());
			if (!action.isPresent()) {
				throw new ScoreCardServerException(ScoreCardErrorCode.TRANSACTION_SAVE_FAILED_BAD_ACTION);
			} else {
				a.setAction(action.get());
			}
		}
		
		return transactionRepository.save(transaction);
	}
	
}

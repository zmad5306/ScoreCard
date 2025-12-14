package dev.zachmaddox.scorecard.api.service;

import java.util.List;

import dev.zachmaddox.scorecard.api.domain.Action;
import dev.zachmaddox.scorecard.api.domain.Transaction;
import dev.zachmaddox.scorecard.api.domain.TransactionAction;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardClientException;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardErrorCode;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardServerException;
import dev.zachmaddox.scorecard.api.repository.ActionRepository;
import dev.zachmaddox.scorecard.api.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final ActionRepository actionRepository;
	
	@Transactional(readOnly=true)
	public List<Transaction> getTransactions() {
		return transactionRepository.findAll();
	}
	
	@Transactional(readOnly=true)
	public Transaction getTransaction(Long transactionId) {
		return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ScoreCardClientException(ScoreCardErrorCode.TRANSACTION_DNE));
	}
	
	@Transactional(readOnly=true)
	public Transaction getTransactionByName(String name) {
		return transactionRepository.findByName(name)
                .orElseThrow(() -> new ScoreCardClientException(ScoreCardErrorCode.TRANSACTION_DNE));
	}

	@Transactional
	public Transaction save(Transaction transaction) {
		transactionRepository.findByName(transaction.getName())
                .ifPresent(t -> {
                    throw new ScoreCardClientException(ScoreCardErrorCode.TRANSACTION_NAME_IN_USE);
                });

		for (TransactionAction a : transaction.getActions()) {
			Action action = actionRepository.findById(a.getAction().getActionId())
                    .orElseThrow(() -> new ScoreCardServerException(ScoreCardErrorCode.TRANSACTION_SAVE_FAILED_BAD_ACTION));
            a.setAction(action);
		}
		
		return transactionRepository.save(transaction);
	}
	
	@Transactional
	public void delete(Long transactionId) {
		transactionRepository.deleteById(transactionId);
	}
	
}

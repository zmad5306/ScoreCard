package dev.zachmaddox.scorecard.portal.web;

import java.util.ArrayList;
import java.util.List;

import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardClientException;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardErrorCode;
import dev.zachmaddox.scorecard.lib.domain.Action;
import dev.zachmaddox.scorecard.lib.domain.Service;
import dev.zachmaddox.scorecard.lib.domain.Transaction;
import dev.zachmaddox.scorecard.lib.domain.exception.TransactionNameNotUniqueException;
import dev.zachmaddox.scorecard.lib.service.ActionApiService;
import dev.zachmaddox.scorecard.lib.service.ServiceApiService;
import dev.zachmaddox.scorecard.lib.service.TransactionApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import dev.zachmaddox.scorecard.portal.domain.NewAction;

@SuppressWarnings("unused") // methods are called from pages
@RequiredArgsConstructor
@Component
public class TransactionHandler {
	
	private final TransactionApiService transactionApiService;
	private final ActionApiService actionApiService;
	private final ServiceApiService serviceApiService;

	public Transaction init() {
		Transaction transaction = new Transaction();
		transaction.setActions(new ArrayList<>());
		
		return transaction;
	}
	
	public Transaction lookup(Long transactionId) {
		return transactionApiService.getTransaction(transactionId);
	}
	
	public NewAction startAddAction() {
		return new NewAction();
	}
	
	public List<Service> loadServices() {
		return serviceApiService.getServices();
	}
	
	public List<Action> loadActions(NewAction action) {
		return actionApiService.getActionByServiceId(action.getServiceId());
	}
	
	public void addActionToTransaction(Transaction transaction, NewAction action) {
		Action a = actionApiService.getAction(action.getActionId());
		a.setDependsOn(new ArrayList<>());
		transaction.getActions().add(a);
	}
	
	public Action selectAction(Transaction transaction, Integer selectedActionIndex) {
		return transaction.getActions().get(selectedActionIndex);
	}
	
	public Transaction addDependencies(Transaction transaction, Action action, Integer selectedActionIndex) {
		transaction.getActions().get(selectedActionIndex).setDependsOn(action.getDependsOn());
		return transaction;
	}
	public void removeAction(Transaction transaction, Integer selectedActionIndex) {
		Action removedAction = transaction.getActions().remove(selectedActionIndex.intValue());
		for (Action action : transaction.getActions()) {
			if (null != action.getDependsOn()) {
                action.getDependsOn().remove(removedAction.getActionId());
			}
		}
	}
	
	public Long save(Transaction transaction) {
		Transaction t;
		try {
			t = transactionApiService.save(transaction);
		} catch (ScoreCardClientException e) {
			if (ScoreCardErrorCode.TRANSACTION_NAME_TAKEN.equals(e.getError())) {
				throw new TransactionNameNotUniqueException();
			} else {
				throw e;
			}
		}
		return t.getTransactionId();
	}
	
	public void delete(Long transactionId) {
		transactionApiService.delete(transactionId);
	}
	
}

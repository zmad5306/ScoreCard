package us.zacharymaddox.scorecard.portal.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.zacharymaddox.scorecard.api.domain.Action;
import us.zacharymaddox.scorecard.api.domain.Service;
import us.zacharymaddox.scorecard.api.domain.Transaction;
import us.zacharymaddox.scorecard.api.domain.exception.TransactionNameNotUniqueException;
import us.zacharymaddox.scorecard.api.service.ActionApiService;
import us.zacharymaddox.scorecard.api.service.ServiceApiService;
import us.zacharymaddox.scorecard.api.service.TransactionApiService;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardClientException;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardErrorCode;
import us.zacharymaddox.scorecard.portal.domain.NewAction;

// https://github.com/vakho10/Java-Spring-Boot-with-Web-Flow-and-Thymeleaf/blob/master/src/main/java/com/example/demo/handlers/RegisterHandler.java

@Component
public class TransactionHandler {
	
	@Autowired
	private TransactionApiService transactionApiService;
	@Autowired
	private ActionApiService actionApiService;
	@Autowired
	private ServiceApiService serviceApiService;

	public Transaction init() {
		Transaction transaction = new Transaction();
		transaction.setActions(new ArrayList<>());
		
		return transaction;
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
	
	public Long save(Transaction transaction) {
		Transaction t = null;
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
	
}

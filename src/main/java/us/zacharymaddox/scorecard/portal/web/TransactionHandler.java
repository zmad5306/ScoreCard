package us.zacharymaddox.scorecard.portal.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.zacharymaddox.scorecard.api.domain.Action;
import us.zacharymaddox.scorecard.api.domain.Transaction;
import us.zacharymaddox.scorecard.api.service.ActionApiService;
import us.zacharymaddox.scorecard.api.service.TransactionApiService;

// https://github.com/vakho10/Java-Spring-Boot-with-Web-Flow-and-Thymeleaf/blob/master/src/main/java/com/example/demo/handlers/RegisterHandler.java

@Component
public class TransactionHandler {
	
	@Autowired
	private TransactionApiService transactionApiService;
	@Autowired
	private ActionApiService actionApiService;

	public Transaction init() {
		System.out.println("called init...");
		
		Transaction transaction = new Transaction();
		transaction.setActions(new ArrayList<>());
		
		return transaction;
	}
	
	public Action createAction() {
		System.out.println("called createAction...");
		return new Action();
	}
	
	public List<Action> loadActions() {
		return actionApiService.getAllActions();
	}
	
	public void addAction(Transaction transaction, Action action) {
		System.out.println("called addAction...");
		Action actn = actionApiService.getAction(action.getActionId());
		transaction.getActions().add(actn);
	}
	
	public void save(Transaction transaction) {
		System.out.println("called save...");
	}
	
}

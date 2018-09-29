package us.zacharymaddox.scorecard.portal.web;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.zacharymaddox.scorecard.api.domain.Action;
import us.zacharymaddox.scorecard.api.domain.Transaction;
import us.zacharymaddox.scorecard.api.service.TransactionApiService;

// https://github.com/vakho10/Java-Spring-Boot-with-Web-Flow-and-Thymeleaf/blob/master/src/main/java/com/example/demo/handlers/RegisterHandler.java

@Component
public class TransactionHandler {
	
	@Autowired
	private TransactionApiService transactionApiService;

	public Transaction init() {
		System.out.println("called init...");
		Transaction transaction = new Transaction();
		transaction.setActions(new ArrayList<>());
		return transaction;
	}
	
	public void addTransaction(Transaction transaction, Transaction formData) {
		System.out.println("called addTransaction...");
		transaction.setName(formData.getName());
	}
	
	public void addAction(Transaction transaction, Action action) {
		System.out.println("called addAction...");
		transaction.getActions().add(action);
	}
	
	public void save(Transaction transaction) {
		System.out.println("called save...");
	}
	
}

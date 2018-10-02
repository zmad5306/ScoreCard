package us.zacharymaddox.scorecard.portal.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.zacharymaddox.scorecard.api.domain.Transaction;
import us.zacharymaddox.scorecard.api.service.TransactionApiService;

@Component
public class TransactionsHandler {
	
	@Autowired
	private TransactionApiService transactionApiService;

	public List<Transaction> loadTransactions() {
		return transactionApiService.getTransactions();
	}
	
}

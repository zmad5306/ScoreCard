package dev.zachmaddox.scorecard.portal.web;

import java.util.List;

import dev.zachmaddox.scorecard.lib.domain.Transaction;
import dev.zachmaddox.scorecard.lib.service.TransactionApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused") // methods are called from pages
@RequiredArgsConstructor
@Component
public class TransactionsHandler {
	
	private final TransactionApiService transactionApiService;

	public List<Transaction> loadTransactions() {
		return transactionApiService.getTransactions();
	}
	
}

package us.zacharymaddox.scorecard.core.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import us.zacharymaddox.scorecard.core.domain.Transaction;
import us.zacharymaddox.scorecard.core.service.TransactionService;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	
	@GetMapping(produces="application/json")
	public Transaction getTransaction(@RequestParam(name="name", required=true) String name) {
		return transactionService.getTransactionByName(name);
	}
	
	@GetMapping(value="/{transaction_id}", produces="application/json")
	public Transaction getTransaction(@PathVariable("transaction_id") Long transactionId) {
		return transactionService.getTransaction(transactionId);
	}
	

}

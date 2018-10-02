package us.zacharymaddox.scorecard.core.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@GetMapping(value="/list", produces="application/json")
	public List<Transaction> getTransactions() {
		return transactionService.getTransactions();
	}
	
	@GetMapping(produces="application/json")
	public Transaction getTransaction(@RequestParam(name="name", required=true) String name) {
		return transactionService.getTransactionByName(name);
	}
	
	@GetMapping(value="/{transaction_id}", produces="application/json")
	public Transaction getTransaction(@PathVariable("transaction_id") Long transactionId) {
		return transactionService.getTransaction(transactionId);
	}
	
	@PostMapping(consumes="application/json", produces="application/json")
	public Transaction save(@RequestBody Transaction transaction) {
		Transaction t = 
				transactionService.save(transaction);
		return t;
	}
	
	@DeleteMapping(value="/{transaction_id}")
	public void deleteTransaction(@PathVariable("transaction_id") Long transactionId) {
		transactionService.delete(transactionId);
	}

}

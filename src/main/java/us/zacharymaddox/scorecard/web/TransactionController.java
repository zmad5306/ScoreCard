package us.zacharymaddox.scorecard.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import us.zacharymaddox.scorecard.domain.Transaction;
import us.zacharymaddox.scorecard.service.TransactionService;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	
	@GetMapping(value="/{transaction_id}", produces="application/json")
	public Transaction getTransaction(@PathVariable("transaction_id") Long transactionId) {
		return transactionService.getTransaction(transactionId);
	}
	

}

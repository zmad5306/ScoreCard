package dev.zachmaddox.scorecard.api.web.api;

import java.util.List;

import dev.zachmaddox.scorecard.api.domain.Transaction;
import dev.zachmaddox.scorecard.api.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
@Tag(name = "Transaction", description = "Endpoints for managing transactions")
public class TransactionController {
	
	private final TransactionService transactionService;
	
	@GetMapping(value="/list", produces="application/json")
	@Operation(summary = "List all transactions", responses = {
			@ApiResponse(responseCode = "200", description = "Transactions returned", content = @Content(schema = @Schema(implementation = Transaction.class)))
	})
	public List<Transaction> getTransactions() {
		return transactionService.getTransactions();
	}
	
	@GetMapping(produces="application/json")
	@Operation(summary = "Get transaction by name", responses = {
			@ApiResponse(responseCode = "200", description = "Transaction found", content = @Content(schema = @Schema(implementation = Transaction.class))),
			@ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content)
	})
	public Transaction getTransaction(@RequestParam(name="name") String name) {
		return transactionService.getTransactionByName(name);
	}
	
	@GetMapping(value="/{transaction_id}", produces="application/json")
	@Operation(summary = "Get transaction by id", responses = {
			@ApiResponse(responseCode = "200", description = "Transaction found", content = @Content(schema = @Schema(implementation = Transaction.class))),
			@ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content)
	})
	public Transaction getTransaction(@PathVariable("transaction_id") Long transactionId) {
		return transactionService.getTransaction(transactionId);
	}
	
	@PostMapping(consumes="application/json", produces="application/json")
	@Operation(summary = "Create or update a transaction", responses = {
			@ApiResponse(responseCode = "200", description = "Transaction saved", content = @Content(schema = @Schema(implementation = Transaction.class))),
			@ApiResponse(responseCode = "400", description = "Invalid transaction payload", content = @Content)
	})
	public Transaction save(@RequestBody Transaction transaction) {
        return transactionService.save(transaction);
	}
	
	@DeleteMapping(value="/{transaction_id}")
	@Operation(summary = "Delete a transaction", responses = {
			@ApiResponse(responseCode = "204", description = "Deleted"),
			@ApiResponse(responseCode = "400", description = "Cannot delete due to dependency or invalid request", content = @Content)
	})
	public void deleteTransaction(@PathVariable("transaction_id") Long transactionId) {
		transactionService.delete(transactionId);
	}

}

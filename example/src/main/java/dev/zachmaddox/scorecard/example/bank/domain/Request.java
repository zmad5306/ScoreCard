package dev.zachmaddox.scorecard.example.bank.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public abstract class Request implements Serializable {

	@Schema(description = "Account identifier")
	private Long accountId;
	@Schema(description = "Transaction type")
	private TransactionType transactionType;
	@Schema(description = "Amount of the transaction")
	private BigDecimal amount;
	
	public Request(Long accountId, TransactionType transactionType, BigDecimal amount) {
		super();
		this.accountId = accountId;
		this.transactionType = transactionType;
		this.amount = amount;
	}

}

package dev.zachmaddox.scorecard.example.bank.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@NoArgsConstructor
@Schema(description = "Credit request payload")
public class CreditRequest extends Request {
	public CreditRequest(Long accountId, BigDecimal amount) {
		super(accountId, TransactionType.CREDIT, amount);
	}
}

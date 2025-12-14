package dev.zachmaddox.scorecard.example.bank.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@NoArgsConstructor
@Schema(description = "Debit request payload")
public class DebitRequest extends Request {
	public DebitRequest(Long accountId, BigDecimal amount) {
		super(accountId, TransactionType.DEBIT, amount);
	}
}

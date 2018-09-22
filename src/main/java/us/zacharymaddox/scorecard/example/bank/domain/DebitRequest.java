package us.zacharymaddox.scorecard.example.bank.domain;

import java.math.BigDecimal;

public class DebitRequest extends Request {
	
	private static final long serialVersionUID = 7820864835268744059L;

	public DebitRequest() {
		super(TransactionType.DEBIT);
	}
	
	public DebitRequest(Long accountId, BigDecimal amount) {
		super(accountId, TransactionType.DEBIT, amount);
	}

}

package us.zacharymaddox.scorecard.example.bank.domain;

import java.math.BigDecimal;

public class CreditRequest extends Request {
	
	private static final long serialVersionUID = 7017849769089084227L;

	public CreditRequest() {
		super(TransactionType.CREDIT);
	}
	
	public CreditRequest(Long accountId, BigDecimal amount) {
		super(accountId, TransactionType.CREDIT, amount);
	}

}

package us.zacharymaddox.scorecard.example.bank.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public abstract class Request implements Serializable {

	private static final long serialVersionUID = 6572345404426682896L;
	private Long accountId;
	private TransactionType transactionType;
	private BigDecimal amount;
	
	public Request(TransactionType transactionType) {
		super();
		this.transactionType = transactionType;
	}

	public Request(Long accountId, TransactionType transactionType, BigDecimal amount) {
		super();
		this.accountId = accountId;
		this.transactionType = transactionType;
		this.amount = amount;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
}

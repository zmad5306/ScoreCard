package us.zacharymaddox.scorecard.example.bank.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name="BANK_TRANSACTION", schema="BANK")
public class BankTransaction implements Serializable {
	
	private static final long serialVersionUID = -4270192501505779866L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long transactionId;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ACCOUNT_ID")
	private Account account;
	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;
	private LocalDateTime timestamp;
	private BigDecimal amount;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof BankTransaction) {
			BankTransaction other = (BankTransaction) obj;
			return new EqualsBuilder().append(transactionId, other.getTransactionId()).build();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(transactionId).build();
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
}

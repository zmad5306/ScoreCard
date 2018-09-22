package us.zacharymaddox.scorecard.core.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name="TRANSACTION", schema="SCORE_CARD")
public class Transaction extends DomainObject implements Serializable {

	private static final long serialVersionUID = -7061621744060205125L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonProperty("transaction_id")
	private Long transactionId;
	@Column(unique=true)
	private String name;
	@OneToMany(mappedBy="transaction")
	@JsonSerialize(using=TransactionActionListSerializer.class)
	private List<TransactionAction> actions;
	
	public Transaction() {
		super("transaction");
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof Transaction) {
			Transaction other = (Transaction) obj;
			return new EqualsBuilder().append(this.transactionId, other.getTransactionId()).build();
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<TransactionAction> getActions() {
		return actions;
	}
	public void setActions(List<TransactionAction> actions) {
		this.actions = actions;
	}
	
}

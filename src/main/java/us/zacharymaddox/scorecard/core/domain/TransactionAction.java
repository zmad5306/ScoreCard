package us.zacharymaddox.scorecard.core.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="TRANSACTION_ACTION", schema="SCORE_CARD")
public class TransactionAction extends DomainObject implements Serializable {

	private static final long serialVersionUID = -561620117019671908L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonProperty("transaction_action_id")
	private Long transactionActionId;
	@ManyToOne
	@JoinColumn(name="TRANSACTION_ID")
	private Transaction transaction;
	@ManyToOne
	@JoinColumn(name="ACTION_ID")
	private Action action;
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="TRANSACTION_ACTION_DEPENDENCY",
		schema="SCORE_CARD",
		joinColumns={@JoinColumn(name="TRANSACTION_ACTION_ID")},
		inverseJoinColumns={@JoinColumn(name="DEPENDS_ON_TRANSACTION_ACTION_ID")})
	private Set<TransactionAction> dependsOn;
	@ManyToMany(mappedBy="dependsOn")
	private Set<TransactionAction> dependencyOf = new HashSet<TransactionAction>();
	
	public TransactionAction() {
		super("transaction_action");
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof TransactionAction) {
			TransactionAction other = (TransactionAction) obj;
			return new EqualsBuilder().append(this.transactionActionId, other.getTransactionActionId()).build();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(transactionActionId).build();
	}
	
	public Transaction getTransaction() {
		return transaction;
	}
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	public Set<TransactionAction> getDependsOn() {
		return dependsOn;
	}
	public void setDependsOn(Set<TransactionAction> dependsOn) {
		this.dependsOn = dependsOn;
	}
	public Set<TransactionAction> getDependencyOf() {
		return dependencyOf;
	}
	public void setDependencyOf(Set<TransactionAction> dependencyOf) {
		this.dependencyOf = dependencyOf;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	public Long getTransactionActionId() {
		return transactionActionId;
	}
	public void setTransactionActionId(Long transactionActionId) {
		this.transactionActionId = transactionActionId;
	}
	
}

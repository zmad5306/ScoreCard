package us.zacharymaddox.scorecard.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaction extends DomainObject implements Serializable {

	private static final long serialVersionUID = -7061621744060205125L;
	
	public Transaction() {
		super("transaction");
	}
	
	@Id
	@JsonProperty("transaction_id")
	private String transactionId;
	private String name;
	private List<TransactionAction> actions;
	
	public void addAction(Action action, Action... dependencies) {
		if (actions == null) {
			actions = new ArrayList<>();
		}
		List<String> depActionIds = Stream.of(dependencies).map(a -> a.getActionId()).collect(Collectors.toList());
		TransactionAction a = new TransactionAction();
		a.setActionId(action.getActionId());
		a.setDependencies(depActionIds);
		actions.add(a);
	}

	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
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

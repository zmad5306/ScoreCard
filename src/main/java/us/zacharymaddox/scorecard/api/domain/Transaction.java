package us.zacharymaddox.scorecard.api.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaction implements Serializable {
	
	private static final long serialVersionUID = 5234809023328851117L;
	@JsonProperty("transaction_id")
	private Long transactionId;
	private String type;
	private String name;
	private List<Action> actions;
	public Long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Action> getActions() {
		return actions;
	}
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
	public Action getAction(String name) {
		Optional<Action> action = actions.stream().filter(a -> name.equalsIgnoreCase(a.getName())).findFirst();
		return action.get();
	}
	
}

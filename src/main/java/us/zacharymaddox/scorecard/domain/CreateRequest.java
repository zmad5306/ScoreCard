package us.zacharymaddox.scorecard.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateRequest implements Serializable {
	
	private static final long serialVersionUID = 1130517004726960608L;
	@JsonProperty("transaction_id")
	private Long transactionId;

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	
}

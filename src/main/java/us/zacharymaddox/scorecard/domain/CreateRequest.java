package us.zacharymaddox.scorecard.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateRequest implements Serializable {
	
	private static final long serialVersionUID = 1130517004726960608L;
	
	@JsonProperty("score_card_id")
	private Long scoreCardId;
	@JsonProperty("transaction_id")
	private Long transactionId;
	
	public CreateRequest() {
		super();
	}

	public CreateRequest(Long scoreCardId, Long transactionId) {
		super();
		this.scoreCardId = scoreCardId;
		this.transactionId = transactionId;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Long getScoreCardId() {
		return scoreCardId;
	}

	public void setScoreCardId(Long scoreCardId) {
		this.scoreCardId = scoreCardId;
	}
	
}

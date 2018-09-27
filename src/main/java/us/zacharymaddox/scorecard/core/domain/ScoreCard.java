package us.zacharymaddox.scorecard.core.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="SCORE_CARD", schema="SCORE_CARD", indexes= {
		@Index(columnList="transactionName")
})
public class ScoreCard extends DomainObject implements Serializable {

	private static final long serialVersionUID = 3392668963392107413L;
	
	@Id
	@JsonProperty("score_card_id")
	private Long scoreCardId;
	@JsonProperty("transaction_id")
	private Long transactionId;
	@JsonProperty("transaction_name")
	private String transactionName;
	@OneToMany(mappedBy="scoreCard", fetch=FetchType.EAGER)
	private List<ScoreCardAction> actions;
	
	public ScoreCard() {
		super("score_card");
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof ScoreCard) {
			ScoreCard other = (ScoreCard) obj;
			return new EqualsBuilder().append(this.scoreCardId, other.getScoreCardId()).build();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(scoreCardId).build();
	}

	public Long getScoreCardId() {
		return scoreCardId;
	}
	public void setScoreCardId(Long scoreCardId) {
		this.scoreCardId = scoreCardId;
	}
	public List<ScoreCardAction> getActions() {
		return actions;
	}
	public void setActions(List<ScoreCardAction> actions) {
		this.actions = actions;
	}
	public Long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	public String getTransactionName() {
		return transactionName;
	}
	public void setTransactionName(String transactionName) {
		this.transactionName = transactionName;
	}
	
}

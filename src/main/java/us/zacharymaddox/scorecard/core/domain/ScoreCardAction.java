package us.zacharymaddox.scorecard.core.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import us.zacharymaddox.scorecard.common.domain.ScoreCardActionStatus;

@Entity
@Table(name="SCORE_CARD_ACTION")
public class ScoreCardAction extends DomainObject implements Serializable {

	private static final long serialVersionUID = 7351293958881594081L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
//	@JsonProperty("score_card_action_id")
	@JsonIgnore
	private Long scoreCardActionId;
	@ManyToOne
	@JoinColumn(name="SCORE_CARD_ID")
	@JsonIgnore
	private ScoreCard scoreCard;
	@ManyToOne
	@JsonProperty("action_id")
	@JsonSerialize(using = ActionSerializer.class)
	private Action action;	
	@Enumerated(EnumType.STRING)
	private ScoreCardActionStatus status;
	@JsonProperty("start_timestamp")
	private LocalDateTime startTimestamp;
	@JsonProperty("end_timestamp")
	private LocalDateTime endTimestamp;
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="SCORE_CARD_ACTION_DEPENDENCY",
		joinColumns={@JoinColumn(name="SCORE_CARD_ACTION_ID")},
		inverseJoinColumns={@JoinColumn(name="DEPENDS_ON_SCORE_CARD_ACTION_ID")})
	@JsonSerialize(using = DependencySerializer.class)
	@JsonProperty("depends_on")
	private Set<ScoreCardAction> dependsOn;
	@ManyToMany(mappedBy="dependsOn")
	@JsonIgnore
	private Set<ScoreCardAction> dependencyOf = new HashSet<ScoreCardAction>();
	
	public ScoreCardAction() {
		super("score_card_action");
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof ScoreCardAction) {
			ScoreCardAction other = (ScoreCardAction) obj;
			return new EqualsBuilder().append(this.scoreCardActionId, other.getScoreCardActionId()).build();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(scoreCardActionId).build();
	}
	
	public ScoreCardActionStatus getStatus() {
		return status;
	}
	public void setStatus(ScoreCardActionStatus status) {
		this.status = status;
	}
	public LocalDateTime getStartTimestamp() {
		return startTimestamp;
	}
	public void setStartTimestamp(LocalDateTime startTimestamp) {
		this.startTimestamp = startTimestamp;
	}
	public LocalDateTime getEndTimestamp() {
		return endTimestamp;
	}
	public void setEndTimestamp(LocalDateTime endTimestamp) {
		this.endTimestamp = endTimestamp;
	}
	public Long getScoreCardActionId() {
		return scoreCardActionId;
	}
	public void setScoreCardActionId(Long scoreCardActionId) {
		this.scoreCardActionId = scoreCardActionId;
	}
	public ScoreCard getScoreCard() {
		return scoreCard;
	}
	public void setScoreCard(ScoreCard scoreCard) {
		this.scoreCard = scoreCard;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	public Set<ScoreCardAction> getDependsOn() {
		return dependsOn;
	}
	public void setDependsOn(Set<ScoreCardAction> dependsOn) {
		this.dependsOn = dependsOn;
	}
	public Set<ScoreCardAction> getDependencyOf() {
		return dependencyOf;
	}
	public void setDependencyOf(Set<ScoreCardAction> dependencyOf) {
		this.dependencyOf = dependencyOf;
	}
	
}

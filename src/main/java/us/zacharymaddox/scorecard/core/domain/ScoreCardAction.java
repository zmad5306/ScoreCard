package us.zacharymaddox.scorecard.core.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import us.zacharymaddox.scorecard.domain.ScoreCardActionStatus;

@Entity
@Table(name="SCORE_CARD_ACTION", schema="SCORE_CARD")
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
	@JsonProperty("action_id")
	private Long actionId;
	private String name;
	private String path;
	@Enumerated(EnumType.STRING)
	private Method method;
	@Enumerated(EnumType.STRING)
	private ScoreCardActionStatus status;
	@JsonProperty("start_timestamp")
	private LocalDateTime startTimestamp;
	@JsonProperty("end_timestamp")
	private LocalDateTime endTimestamp;
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="SCORE_CARD_ACTION_DEPENDENCY",
		schema="SCORE_CARD",
		joinColumns={@JoinColumn(name="SCORE_CARD_ACTION_ID")},
		inverseJoinColumns={@JoinColumn(name="DEPENDS_ON_SCORE_CARD_ACTION_ID")})
	@JsonSerialize(using = DependencySerializer.class)
	@JsonProperty("depends_on")
	private Set<ScoreCardAction> dependsOn;
	@ManyToMany(mappedBy="dependsOn")
	@JsonIgnore
	private Set<ScoreCardAction> dependencyOf = new HashSet<ScoreCardAction>();
	@ElementCollection(targetClass=String.class, fetch=FetchType.EAGER)
	@CollectionTable(name="SCORE_CARD_ACTION_METADATA", schema="SCORE_CARD")
	@MapKeyColumn(name="name")
	@Column(name="value")
	private Map<String, String> metadata;
	
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
	public Map<String, String> getMetadata() {
		return metadata;
	}
	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	public Long getActionId() {
		return actionId;
	}

	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}
	
}

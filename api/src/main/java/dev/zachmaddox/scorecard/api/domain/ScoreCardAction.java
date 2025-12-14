package dev.zachmaddox.scorecard.api.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import dev.zachmaddox.scorecard.common.domain.Method;
import dev.zachmaddox.scorecard.common.domain.ScoreCardActionStatus;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="SCORE_CARD_ACTION", schema="SCORE_CARD", indexes= {
		@Index(columnList="STATUS")
})
public class ScoreCardAction implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonIgnore
    @EqualsAndHashCode.Include
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
	private Set<ScoreCardAction> dependencyOf = new HashSet<>();
	@ElementCollection(targetClass=String.class, fetch=FetchType.EAGER)
	@CollectionTable(
			name="SCORE_CARD_ACTION_METADATA",
			schema="SCORE_CARD",
			joinColumns = @JoinColumn(name = "SCORE_CARD_ACTION_ID")
	)
	@MapKeyColumn(name="NAME")
	@Column(name="METADATA_VALUE")
	private Map<String, String> metadata;
	
}

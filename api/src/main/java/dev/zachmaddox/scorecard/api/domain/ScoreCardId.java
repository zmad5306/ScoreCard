package dev.zachmaddox.scorecard.api.domain;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity // this is only an entity to generate the sequence
@Table(name="SCORE_CARD_ID", schema="SCORE_CARD")
public class ScoreCardId implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="score_card_id_generator")
	@SequenceGenerator(name="score_card_id_generator", sequenceName="score_card_id_sequence", allocationSize=1)
	@JsonProperty("score_card_id")
    @EqualsAndHashCode.Include
	private Long scoreCardId;
	
}

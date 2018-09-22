package us.zacharymaddox.scorecard.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity // this is only an entity to generate the sequence
@Table(name="SCORE_CARD_ID", schema="SCORE_CARD")
public class ScoreCardId implements Serializable {
	
	private static final long serialVersionUID = 631834385163738001L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="score_card_id_generator")
	@SequenceGenerator(name="score_card_id_generator", sequenceName="score_card_id_sequence", allocationSize=1, initialValue=1)
	@JsonProperty("score_card_id")
	private Long scoreCardId;
	
	public ScoreCardId() {
		super();
	}

	public ScoreCardId(Long scoreCardId) {
		super();
		this.scoreCardId = scoreCardId;
	}

	public Long getScoreCardId() {
		return scoreCardId;
	}

	public void setScoreCardId(Long scoreCardId) {
		this.scoreCardId = scoreCardId;
	}
	
	
	
}

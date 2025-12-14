package dev.zachmaddox.scorecard.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="SCORE_CARD", schema="SCORE_CARD", indexes= {
		@Index(columnList="transactionName")
})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Schema(description = "Score card instance tracking transaction execution")
public class ScoreCard  implements Serializable {

	@Id
	@JsonProperty("score_card_id")
    @EqualsAndHashCode.Include
	@Schema(description = "Score card identifier")
	private Long scoreCardId;
	@JsonProperty("transaction_id")
	@Schema(description = "Transaction identifier")
	private Long transactionId;
	@JsonProperty("transaction_name")
	@Schema(description = "Transaction name")
	private String transactionName;
	@OneToMany(mappedBy="scoreCard", fetch=FetchType.EAGER)
	@Schema(description = "Actions under this score card")
	private List<ScoreCardAction> actions;
	
}

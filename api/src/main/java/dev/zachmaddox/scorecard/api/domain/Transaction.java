package dev.zachmaddox.scorecard.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="TRANSACTION", schema="SCORE_CARD", indexes = {
		@Index(columnList="NAME", unique=true)
})
@Schema(description = "Transaction definition composed of actions")
public class Transaction implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonProperty("transaction_id")
    @EqualsAndHashCode.Include
	@Schema(description = "Transaction identifier")
	private Long transactionId;
	@Column(unique=true)
	@NotEmpty
	@NotNull
	@Schema(description = "Transaction name (unique)")
	private String name;
	@OneToMany(mappedBy="transaction", cascade=CascadeType.ALL)
	@JsonSerialize(using=TransactionActionListSerializer.class)
	@JsonDeserialize(using=TransactionActionListDeserializer.class)
	@Schema(description = "Ordered list of actions in this transaction")
	private List<TransactionAction> actions;

}

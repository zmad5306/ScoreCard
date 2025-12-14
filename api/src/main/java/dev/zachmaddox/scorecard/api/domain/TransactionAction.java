package dev.zachmaddox.scorecard.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="TRANSACTION_ACTION", schema="SCORE_CARD")
@Schema(description = "Join between transaction and action, including dependencies")
public class TransactionAction implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonProperty("transaction_action_id")
    @EqualsAndHashCode.Include
	@Schema(description = "Transaction action identifier")
	private Long transactionActionId;
	@ManyToOne
	@JoinColumn(name="TRANSACTION_ID")
	@Schema(description = "Owning transaction")
	private Transaction transaction;
	@ManyToOne
	@JoinColumn(name="ACTION_ID")
	@NotNull
	@Schema(description = "Referenced action")
	private Action action;
	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name= "TRANSACTION_ACTION_DEPENDENCY",
		schema="SCORE_CARD",
		joinColumns={@JoinColumn(name= "TRANSACTION_ACTION_ID")},
		inverseJoinColumns={@JoinColumn(name= "DEPENDS_ON_TRANSACTION_ACTION_ID")})
	@Schema(description = "Dependencies for this transaction action")
	private Set<TransactionAction> dependsOn;
	@ManyToMany(mappedBy="dependsOn")
	@Schema(description = "Reverse dependencies")
	private Set<TransactionAction> dependencyOf = new HashSet<>();
	
}

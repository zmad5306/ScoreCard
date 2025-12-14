package dev.zachmaddox.scorecard.example.bank.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@Table(name="ACCOUNT", schema="BANK")
@Schema(description = "Demo bank account for example app")
public class Account implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
	@Schema(description = "Account identifier")
	private Long accountId;
	@OneToMany(mappedBy="account")
	@Schema(description = "Transactions for this account")
	private List<BankTransaction> transactions;
	@Schema(description = "Current balance")
	private BigDecimal balance;

}

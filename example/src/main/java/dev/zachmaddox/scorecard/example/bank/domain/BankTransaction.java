package dev.zachmaddox.scorecard.example.bank.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@Table(name="BANK_TRANSACTION", schema="BANK")
@Schema(description = "Demo bank transaction for example app")
public class BankTransaction implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
	@Schema(description = "Transaction identifier")
	private Long transactionId;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ACCOUNT_ID")
	@Schema(description = "Associated account")
	private Account account;
	@Enumerated(EnumType.STRING)
	@Schema(description = "Transaction type")
	private TransactionType transactionType;
	@Schema(description = "Timestamp of the transaction")
	private LocalDateTime timestamp;
	@Schema(description = "Transaction amount")
	private BigDecimal amount;
	
}

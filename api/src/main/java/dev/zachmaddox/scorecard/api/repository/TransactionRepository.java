package dev.zachmaddox.scorecard.api.repository;

import java.util.Optional;

import dev.zachmaddox.scorecard.api.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	Optional<Transaction> findByName(String name);
	
}

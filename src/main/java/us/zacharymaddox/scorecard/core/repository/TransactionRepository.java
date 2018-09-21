package us.zacharymaddox.scorecard.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import us.zacharymaddox.scorecard.core.domain.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}

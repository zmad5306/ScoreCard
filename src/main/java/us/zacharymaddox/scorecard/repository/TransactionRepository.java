package us.zacharymaddox.scorecard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import us.zacharymaddox.scorecard.domain.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}

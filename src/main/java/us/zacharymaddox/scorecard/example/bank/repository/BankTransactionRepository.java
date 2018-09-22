package us.zacharymaddox.scorecard.example.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import us.zacharymaddox.scorecard.example.bank.domain.BankTransaction;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long>{

}

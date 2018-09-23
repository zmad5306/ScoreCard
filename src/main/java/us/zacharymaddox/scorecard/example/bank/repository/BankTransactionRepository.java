package us.zacharymaddox.scorecard.example.bank.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import us.zacharymaddox.scorecard.example.bank.domain.BankTransaction;

@Profile({"test-app"})
public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long>{

}

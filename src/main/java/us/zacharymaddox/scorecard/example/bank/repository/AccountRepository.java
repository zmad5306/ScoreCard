package us.zacharymaddox.scorecard.example.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import us.zacharymaddox.scorecard.example.bank.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}

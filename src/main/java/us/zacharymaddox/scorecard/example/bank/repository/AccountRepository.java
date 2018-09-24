package us.zacharymaddox.scorecard.example.bank.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import us.zacharymaddox.scorecard.example.bank.domain.Account;

@Profile({"example"})
public interface AccountRepository extends JpaRepository<Account, Long> {

}

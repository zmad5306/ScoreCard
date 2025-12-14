package dev.zachmaddox.scorecard.example.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.zachmaddox.scorecard.example.bank.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {}

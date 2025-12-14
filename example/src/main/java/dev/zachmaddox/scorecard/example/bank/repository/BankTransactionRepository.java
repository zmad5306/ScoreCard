package dev.zachmaddox.scorecard.example.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.zachmaddox.scorecard.example.bank.domain.BankTransaction;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long>{}

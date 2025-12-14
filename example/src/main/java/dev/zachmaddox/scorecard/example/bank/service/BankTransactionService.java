package dev.zachmaddox.scorecard.example.bank.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.zachmaddox.scorecard.example.bank.domain.BankTransaction;
import dev.zachmaddox.scorecard.example.bank.repository.BankTransactionRepository;

@RequiredArgsConstructor
@Service
public class BankTransactionService {
	
	private final BankTransactionRepository bankTransactionRepository;
	
	@Transactional(readOnly=true)
	public Optional<BankTransaction> getBankTransactionById(Long bankTransactionId) {
		return bankTransactionRepository.findById(bankTransactionId);
	}
	

}

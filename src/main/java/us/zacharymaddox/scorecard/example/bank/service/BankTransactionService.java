package us.zacharymaddox.scorecard.example.bank.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import us.zacharymaddox.scorecard.example.bank.domain.BankTransaction;
import us.zacharymaddox.scorecard.example.bank.repository.BankTransactionRepository;

@Service
@Profile({"test-app"})
public class BankTransactionService {
	
	@Autowired
	private BankTransactionRepository bankTransactionRepository;
	
	@Transactional(readOnly=true)
	public Optional<BankTransaction> getBankTransactionById(Long bankTransactionId) {
		return bankTransactionRepository.findById(bankTransactionId);
	}
	

}

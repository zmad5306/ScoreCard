package us.zacharymaddox.scorecard.example.bank.service;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import us.zacharymaddox.scorecard.example.bank.domain.Account;
import us.zacharymaddox.scorecard.example.bank.repository.AccountRepository;

@Service
@Profile({"example"})
public class BankDataSetupService {
	
	@Autowired
	private AccountRepository acccountRepository;
	
	@PostConstruct
	public void init() {
		Account a1 = new Account();
		a1.setBalance(new BigDecimal("1000.00"));
		
		acccountRepository.save(a1);
		
		Account a2 = new Account();
		a2.setBalance(new BigDecimal("1000.00"));
		
		acccountRepository.save(a2);
	}

}

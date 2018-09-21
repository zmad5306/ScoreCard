package us.zacharymaddox.scorecard.example.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import us.zacharymaddox.scorecard.domain.Action;
import us.zacharymaddox.scorecard.domain.Method;
import us.zacharymaddox.scorecard.domain.Service;
import us.zacharymaddox.scorecard.domain.Transaction;
import us.zacharymaddox.scorecard.domain.Transport;
import us.zacharymaddox.scorecard.repository.ActionRepository;
import us.zacharymaddox.scorecard.repository.ServiceRepository;
import us.zacharymaddox.scorecard.repository.TransactionRepository;

@Component
@Profile({"test-app"})
public class DataSetupService {
	
	@Autowired
	private ServiceRepository serviceRepository;
	
	@Autowired
	private ActionRepository actionRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@PostConstruct
	public void init() {
		System.out.println("Setting up test transaction...");
		Service s1 = new Service();
		s1.setName("service1");
		s1.setTransport(Transport.QUEUE);
		
		s1 = serviceRepository.save(s1);
		
		Action a1 = new Action();
		a1.setMethod(Method.POST);
		a1.setName("action1");
		a1.setPath("action1");
		a1.setServiceId(s1.getServiceId());
		
		a1 = actionRepository.save(a1);
		
		Action a2 = new Action();
		a2.setMethod(Method.POST);
		a2.setName("action2");
		a2.setPath("action2");
		a2.setServiceId(s1.getServiceId());
		
		a2 = actionRepository.save(a2);
		
		Action a3 = new Action();
		a3.setMethod(Method.POST);
		a3.setName("action3");
		a3.setPath("action3");
		a3.setServiceId(s1.getServiceId());
		
		a3 = actionRepository.save(a3);
		
		Transaction t1 = new Transaction();
		t1.setName("transaction1");
		t1.addAction(a1);
		t1.addAction(a2, a1);
		t1.addAction(a3, a1);
		t1.setTransactionId("1");
		
		t1 = transactionRepository.save(t1);
	}

}

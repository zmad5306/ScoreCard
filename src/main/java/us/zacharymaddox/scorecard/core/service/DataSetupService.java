package us.zacharymaddox.scorecard.core.service;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import us.zacharymaddox.scorecard.core.domain.Action;
import us.zacharymaddox.scorecard.core.domain.Method;
import us.zacharymaddox.scorecard.core.domain.Service;
import us.zacharymaddox.scorecard.core.domain.Transaction;
import us.zacharymaddox.scorecard.core.domain.TransactionAction;
import us.zacharymaddox.scorecard.core.domain.Transport;
import us.zacharymaddox.scorecard.core.repository.ActionRepository;
import us.zacharymaddox.scorecard.core.repository.ServiceRepository;
import us.zacharymaddox.scorecard.core.repository.TransactionActionRepository;
import us.zacharymaddox.scorecard.core.repository.TransactionRepository;

@Component
@Profile({"test-app"})
public class DataSetupService {
	
	@Autowired
	private ServiceRepository serviceRepository;
	
	@Autowired
	private ActionRepository actionRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private TransactionActionRepository transactionActionRepository;
	
	@PostConstruct
	@Transactional
	public void init() {
		System.out.println("Setting up test transaction...");
		Service s1 = new Service();
		s1.setName("service1");
		s1.setPath("service1");
		s1.setTransport(Transport.QUEUE);
		
		s1 = serviceRepository.save(s1);
		
		Action a1 = new Action();
		a1.setMethod(Method.POST);
		a1.setName("action1");
		a1.setPath("action1");
		a1.setService(s1);
		
		a1 = actionRepository.save(a1);
		
		Action a2 = new Action();
		a2.setMethod(Method.POST);
		a2.setName("action2");
		a2.setPath("action2");
		a2.setService(s1);
		
		a2 = actionRepository.save(a2);
		
		Action a3 = new Action();
		a3.setMethod(Method.POST);
		a3.setName("action3");
		a3.setPath("action3");
		a3.setService(s1);
		
		a3 = actionRepository.save(a3);
		
		Transaction t1 = new Transaction();
		t1.setName("transaction1");
		
		t1 = transactionRepository.save(t1);
		
		TransactionAction ta1 = new TransactionAction();
		ta1.setAction(a1);
		ta1.setDependsOn(null);
		ta1.setTransaction(t1);
		
		ta1 = transactionActionRepository.save(ta1);
		
		Set<TransactionAction> dependsOn = new HashSet<>();
		dependsOn.add(ta1);
		
		TransactionAction ta2 = new TransactionAction();
		ta2.setAction(a2);
		ta2.setDependsOn(dependsOn);
		ta2.setTransaction(t1);
		
		ta2 = transactionActionRepository.save(ta2);
		
		TransactionAction ta3 = new TransactionAction();
		ta3.setAction(a3);
		ta3.setDependsOn(dependsOn);
		ta3.setTransaction(t1);
		
		ta3 = transactionActionRepository.save(ta3);
	}

}

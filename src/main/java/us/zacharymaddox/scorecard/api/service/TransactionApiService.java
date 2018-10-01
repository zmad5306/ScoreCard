package us.zacharymaddox.scorecard.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import us.zacharymaddox.scorecard.api.domain.Transaction;

@Service
@Profile({"api"})
public class TransactionApiService {
	
	@Autowired
	private RestTemplate restTemplate;
	@Value("${scorecard.api.baseurl}")
	private String baseUrl;
	
	public List<Transaction> getTransactions() {
		ResponseEntity<List<Transaction>> response = restTemplate.exchange(
				baseUrl + "/transaction/list", 
				HttpMethod.GET, 
				null, //requestEntity
				new ParameterizedTypeReference<List<Transaction>>() {}
			);
		return response.getBody();
	}
	
	public Transaction getTransaction(Long transactionId) {
		Transaction transaction = restTemplate.getForObject(baseUrl + "/transaction/{transaction_id}", Transaction.class, transactionId);
		return transaction;
	}
	
	public Transaction getTransactionByName(String name) {
		Transaction transaction = restTemplate.getForObject(baseUrl + "/transaction/?name={name}", Transaction.class, name);
		return transaction;
	}
	
	public Transaction save(Transaction transaction) {
		return restTemplate.postForObject(baseUrl + "/transaction", transaction, Transaction.class);
	}

}

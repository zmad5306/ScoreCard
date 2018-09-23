package us.zacharymaddox.scorecard.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import us.zacharymaddox.scorecard.api.domain.Transaction;

@Service
public class TransactionApiService {
	
	@Value("${scorecard.api.baseurl}")
	private String baseUrl;
	
	public List<Transaction> getTransactions() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<Transaction>> response = restTemplate.exchange(
				baseUrl + "/transaction/list", 
				HttpMethod.GET, 
				null, //requestEntity
				new ParameterizedTypeReference<List<Transaction>>() {}
			);
		return response.getBody();
	}
	
	public Transaction getTransaction(Long transactionId) {
		RestTemplate restTemplate = new RestTemplate();
		Transaction transaction = restTemplate.getForObject(baseUrl + "/transaction/{transaction_id}" + transactionId, Transaction.class, transactionId);
		return transaction;
	}
	
	public Transaction getTransactionByName(String name) {
		RestTemplate restTemplate = new RestTemplate();
		Transaction transaction = restTemplate.getForObject(baseUrl + "/transaction/?name={name}", Transaction.class, name);
		return transaction;
	}

}

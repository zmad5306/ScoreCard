package dev.zachmaddox.scorecard.lib.service;

import java.util.List;

import dev.zachmaddox.scorecard.lib.domain.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TransactionApiService {
	
	private final RestTemplate restTemplate;

	@Value("${scorecard.api.baseurl}")
	private String baseUrl;
	
	public List<Transaction> getTransactions() {
		ResponseEntity<List<Transaction>> response = restTemplate.exchange(
				baseUrl + "/transaction/list", 
				HttpMethod.GET,
				null, //requestEntity
				new ParameterizedTypeReference<>() {}
			);
		return response.getBody();
	}
	
	public Transaction getTransaction(Long transactionId) {
        return restTemplate.getForObject(baseUrl + "/transaction/{transaction_id}", Transaction.class, transactionId);
	}
	
	public Transaction getTransactionByName(String name) {
        return restTemplate.getForObject(baseUrl + "/transaction?name={name}", Transaction.class, name);
	}
	
	public Transaction save(Transaction transaction) {
		return restTemplate.postForObject(baseUrl + "/transaction", transaction, Transaction.class);
	}
	
	public void delete(Long transactionId) {
		this.restTemplate.delete(baseUrl + "/transaction/{transaction_id}", transactionId);
	}

}

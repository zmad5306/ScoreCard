package us.zacharymaddox.scorecard.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import us.zacharymaddox.scorecard.api.domain.Transaction;

@Service
public class TransactionApiService {
	
	@Value("${scorecard.api.baseurl}")
	private String baseUrl;
	
	public Transaction getTransaction(Long transactionId) {
		RestTemplate restTemplate = new RestTemplate();
		Transaction transaction = restTemplate.getForObject(baseUrl + "/transaction/" + transactionId, Transaction.class);
		return transaction;
	}

}

package us.zacharymaddox.scorecard.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import us.zacharymaddox.scorecard.api.domain.ApiTransaction;

@Service
public class TransactionApiService {
	
	@Value("${scorecard.api.baseurl}")
	private String baseUrl;
	
	public ApiTransaction getTransaction(Long transactionId) {
		RestTemplate restTemplate = new RestTemplate();
		ApiTransaction transaction = restTemplate.getForObject(baseUrl + "/transaction/" + transactionId, ApiTransaction.class);
		return transaction;
	}

}

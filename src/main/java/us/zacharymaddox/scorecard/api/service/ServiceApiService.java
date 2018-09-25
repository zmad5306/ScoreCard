package us.zacharymaddox.scorecard.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import us.zacharymaddox.scorecard.api.domain.Service;

@org.springframework.stereotype.Service
@Profile({"api"})
public class ServiceApiService {
	
	@Value("${scorecard.api.baseurl}")
	private String baseUrl;
	
	public List<Service> getServices() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<Service>> response = restTemplate.exchange(
				baseUrl + "/service/list", 
				HttpMethod.GET, 
				null, //requestEntity
				new ParameterizedTypeReference<List<Service>>() {}
			);
		return response.getBody();
	}

	public Service getService(Long serviceId) {
		RestTemplate restTemplate = new RestTemplate();
		Service service = restTemplate.getForObject(baseUrl + "/service/{service_id}", Service.class, serviceId);
		return service;
	}
	
	public Service getServicenByName(String name) {
		RestTemplate restTemplate = new RestTemplate();
		Service service = restTemplate.getForObject(baseUrl + "/service/?name={name}", Service.class, name);
		return service;
	}

}

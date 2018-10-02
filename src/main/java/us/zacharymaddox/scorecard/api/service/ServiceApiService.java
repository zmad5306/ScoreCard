package us.zacharymaddox.scorecard.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import us.zacharymaddox.scorecard.api.domain.Service;
import us.zacharymaddox.scorecard.api.domain.exception.ActionInUseException;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardClientException;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardErrorCode;

@org.springframework.stereotype.Service
@Profile({"api"})
public class ServiceApiService {
	
	@Value("${scorecard.api.baseurl}")
	private String baseUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public List<Service> getServices() {
		ResponseEntity<List<Service>> response = restTemplate.exchange(
				baseUrl + "/service/list", 
				HttpMethod.GET, 
				null, //requestEntity
				new ParameterizedTypeReference<List<Service>>() {}
			);
		return response.getBody();
	}

	public Service getService(Long serviceId) {
		Service service = restTemplate.getForObject(baseUrl + "/service/{service_id}", Service.class, serviceId);
		return service;
	}
	
	public Service getServicenByName(String name) {
		Service service = restTemplate.getForObject(baseUrl + "/service/?name={name}", Service.class, name);
		return service;
	}
	
	public Service saveService(Service service) {
		return restTemplate.postForObject(baseUrl + "/service", service, Service.class);
	}
	
	public void delete(Long serviceId) {
		try {
			this.restTemplate.delete(baseUrl + "/service/{service_id}", serviceId);
		} catch (ScoreCardClientException e) {
			if (ScoreCardErrorCode.CANNOT_DELETE_SERVICE_ACTION_IN_USE.equals(e.getError())) {
				throw new ActionInUseException(e.getError());
			} else {
				throw e;
			}
		}
		
	}

}

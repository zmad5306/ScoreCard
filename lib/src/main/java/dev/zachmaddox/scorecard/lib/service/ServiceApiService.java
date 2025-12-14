package dev.zachmaddox.scorecard.lib.service;

import java.util.List;

import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardClientException;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardErrorCode;
import dev.zachmaddox.scorecard.lib.domain.Service;
import dev.zachmaddox.scorecard.lib.domain.exception.ActionInUseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceApiService {

    private final RestTemplate restTemplate;

	@Value("${scorecard.api.baseurl}")
	private String baseUrl;
	
	public List<Service> getServices() {
		ResponseEntity<List<Service>> response = restTemplate.exchange(
				baseUrl + "/service/list", 
				HttpMethod.GET,
				null, //requestEntity
				new ParameterizedTypeReference<>() {}
			);
		return response.getBody();
	}

	public Service getService(Long serviceId) {
        return restTemplate.getForObject(baseUrl + "/service/{service_id}", Service.class, serviceId);
	}
	
    public Service getServiceByName(String name) {
        return restTemplate.getForObject(baseUrl + "/service/?name={name}", Service.class, name);
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

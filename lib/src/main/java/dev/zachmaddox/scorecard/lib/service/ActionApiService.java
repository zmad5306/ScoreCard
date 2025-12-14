package dev.zachmaddox.scorecard.lib.service;

import java.util.List;

import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardClientException;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardErrorCode;
import dev.zachmaddox.scorecard.lib.domain.Action;
import dev.zachmaddox.scorecard.lib.domain.exception.ActionInUseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ActionApiService {
	
	private final RestTemplate restTemplate;
	
	@Value("${scorecard.api.baseurl}")
	private String baseUrl;
	
	public Action getAction(Long actionId) {
        return restTemplate.getForObject(baseUrl + "/action/{action_id}", Action.class, actionId);
	}
	
	public List<Action> getAllActions() {
		ResponseEntity<List<Action>> response = restTemplate.exchange(
				baseUrl + "/action/list", 
				HttpMethod.GET,
				null, //requestEntity
				new ParameterizedTypeReference<>() {}
			);
		return response.getBody();
	}
	
	public Action getActionByName(String name) {
        return restTemplate.getForObject(baseUrl + "/action/?name={name}", Action.class, name);
	}

	public Action saveAction(Action action) {
		return restTemplate.postForObject(baseUrl + "/action", action, Action.class);
	}

	public List<Action> getActionByServiceId(Long serviceId) {
		ResponseEntity<List<Action>> response = restTemplate.exchange(
				baseUrl + "/action/list/{service_id}", 
				HttpMethod.GET, 
				null, //requestEntity
				new ParameterizedTypeReference<>() {},
				serviceId
			);
		return response.getBody();
	}
	
	public void delete(Long actionId) {
		try {
			this.restTemplate.delete(baseUrl + "/action/{action_id}", actionId);
		} catch (ScoreCardClientException e) {
			if (ScoreCardErrorCode.CANNOT_DELETE_ACTION_IN_USE.equals(e.getError())) {
				throw new ActionInUseException(e.getError());
			} else {
				throw e;
			}
		}
	}

}

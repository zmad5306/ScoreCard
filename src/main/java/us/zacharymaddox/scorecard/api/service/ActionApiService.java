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

import us.zacharymaddox.scorecard.api.domain.Action;
import us.zacharymaddox.scorecard.api.domain.exception.ActionInUseException;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardClientException;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardErrorCode;

@Service
@Profile({"api"})
public class ActionApiService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${scorecard.api.baseurl}")
	private String baseUrl;
	
	public Action getAction(Long actionId) {
		Action action = restTemplate.getForObject(baseUrl + "/action/{action_id}", Action.class, actionId);
		return action;
	}
	
	public List<Action> getAllActions() {
		ResponseEntity<List<Action>> response = restTemplate.exchange(
				baseUrl + "/action/list", 
				HttpMethod.GET, 
				null, //requestEntity
				new ParameterizedTypeReference<List<Action>>() {}
			);
		return response.getBody();
	}
	
	public Action getActionByName(String name) {
		Action action = restTemplate.getForObject(baseUrl + "/action/?name={name}", Action.class, name);
		return action;
	}

	public Action saveAction(Action action) {
		ResponseEntity<Action> svc = restTemplate.postForEntity(baseUrl + "/action", action, Action.class);
		return svc.getBody();
	}

	public List<Action> getActionByServiceId(Long serviceId) {
		ResponseEntity<List<Action>> response = restTemplate.exchange(
				baseUrl + "/action/list/{service_id}", 
				HttpMethod.GET, 
				null, //requestEntity
				new ParameterizedTypeReference<List<Action>>() {},
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

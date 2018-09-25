package us.zacharymaddox.scorecard.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import us.zacharymaddox.scorecard.api.domain.Action;

@Service
@Profile({"api"})
public class ActionApiService {
	
	@Value("${scorecard.api.baseurl}")
	private String baseUrl;
	
	public Action getAction(Long actionId) {
		RestTemplate restTemplate = new RestTemplate();
		Action action = restTemplate.getForObject(baseUrl + "/action/{action_id}", Action.class, actionId);
		return action;
	}
	
	public Action getActionByName(String name) {
		RestTemplate restTemplate = new RestTemplate();
		Action action = restTemplate.getForObject(baseUrl + "/action/?name={name}", Action.class, name);
		return action;
	}

}

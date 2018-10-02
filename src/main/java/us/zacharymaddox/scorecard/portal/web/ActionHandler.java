package us.zacharymaddox.scorecard.portal.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.zacharymaddox.scorecard.api.domain.Action;
import us.zacharymaddox.scorecard.api.service.ActionApiService;

@Component
public class ActionHandler {
	
	@Autowired
	private ActionApiService actionApiService;

	public Action lookup(Long actionId) {
		return actionApiService.getAction(actionId);
	}
	
	public Long delete(Long actionId) {
		Action action = actionApiService.getAction(actionId);
		Long serviceId = action.getService().getServiceId();
		actionApiService.delete(actionId);
		return serviceId;
	}
	
}

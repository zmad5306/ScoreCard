package us.zacharymaddox.scorecard.portal.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.zacharymaddox.scorecard.api.domain.Action;
import us.zacharymaddox.scorecard.api.domain.exception.ActionNameTakenException;
import us.zacharymaddox.scorecard.api.service.ActionApiService;
import us.zacharymaddox.scorecard.api.service.ServiceApiService;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardClientException;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardErrorCode;

@Component
public class ActionHandler {
	
	@Autowired
	private ServiceApiService serviceApiService;
	
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
	
	public Action newAction(Long serviceId) {
		Action action = new Action();
		action.setService(serviceApiService.getService(serviceId));
		return action;
	}
	
	public void save(Action action) {
		try {
			actionApiService.saveAction(action);
		} catch (ScoreCardClientException e) {
			if (ScoreCardErrorCode.ACTION_NAME_TAKEN.equals(e.getError())) {
				throw new ActionNameTakenException();
			} else {
				throw e;
			}
		}
	}
	
}

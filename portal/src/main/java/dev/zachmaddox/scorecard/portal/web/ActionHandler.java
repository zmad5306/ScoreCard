package dev.zachmaddox.scorecard.portal.web;

import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardClientException;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardErrorCode;
import dev.zachmaddox.scorecard.lib.domain.Action;
import dev.zachmaddox.scorecard.lib.domain.exception.ActionNameTakenException;
import dev.zachmaddox.scorecard.lib.service.ActionApiService;
import dev.zachmaddox.scorecard.lib.service.ServiceApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused") // methods are called from pages
@RequiredArgsConstructor
@Component
public class ActionHandler {
	
	private final ServiceApiService serviceApiService;
	private final ActionApiService actionApiService;

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
			if (ScoreCardErrorCode.ACTION_NAME_IN_USE.equals(e.getError())) {
				throw new ActionNameTakenException();
			} else {
				throw e;
			}
		}
	}
	
}

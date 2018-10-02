package us.zacharymaddox.scorecard.core.web.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import us.zacharymaddox.scorecard.core.domain.Action;
import us.zacharymaddox.scorecard.core.service.ActionService;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardClientException;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardErrorCode;

@RestController
@RequestMapping("/api/v1/action")
public class ActionController {
	
	@Autowired
	private ActionService actionService;
	
	@GetMapping(produces="application/json")
	public Action getAction(@RequestParam(name="name", required=true) String name) {
		return actionService.getActionByName(name);
	}
	
	@GetMapping(value="/list", produces="application/json")
	public List<Action> getActions() {
		return actionService.getAllActions();
	}
	
	@GetMapping(value="/list/{service_id}", produces="application/json")
	public List<Action> getActions(@PathVariable("service_id") Long serviceId) {
		return actionService.getActions(serviceId);
	}
	
	@GetMapping(value="/{action_id}", produces="application/json")
	public Action getAction(@PathVariable("action_id") Long actionId) {
		return actionService.getAction(actionId);
	}
	
	@PostMapping(consumes="application/json", produces="application/json")
	public Action saveAction(@RequestBody @Valid Action action, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.ACTION_INVALID);
		} else {
			return actionService.saveAction(action);
		}
	}
	
	@DeleteMapping(value="/{action_id}")
	public void deleteAction(@PathVariable("action_id") Long actionId) {
		actionService.delete(actionId);
	}

}

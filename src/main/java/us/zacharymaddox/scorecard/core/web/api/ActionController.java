package us.zacharymaddox.scorecard.core.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import us.zacharymaddox.scorecard.core.domain.Action;
import us.zacharymaddox.scorecard.core.service.ActionService;

@RestController
@RequestMapping("/api/v1/action")
public class ActionController {
	
	@Autowired
	private ActionService actionService;
	
	@GetMapping(produces="application/json")
	public Action getAction(@RequestParam(name="name", required=true) String name) {
		return actionService.getActionByName(name);
	}
	
	@GetMapping(value="/{action_id}", produces="application/json")
	public Action getAction(@PathVariable("action_id") Long actionId) {
		return actionService.getAction(actionId);
	}
	

}

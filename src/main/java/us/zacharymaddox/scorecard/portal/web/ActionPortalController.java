package us.zacharymaddox.scorecard.portal.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import us.zacharymaddox.scorecard.api.service.ActionApiService;

@Controller
@RequestMapping("/portal/action")
@Profile({"portal"})
public class ActionPortalController {
	
	@Autowired
	private ActionApiService actionApiService;
	
	@GetMapping("/{action_id}")
	public String details(@PathVariable("action_id") Long actionId, Model model) {
		model.addAttribute("action", actionApiService.getAction(actionId));
		return "portal/action/detail";
	}


}

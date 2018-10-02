package us.zacharymaddox.scorecard.portal.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import us.zacharymaddox.scorecard.api.domain.Action;
import us.zacharymaddox.scorecard.api.service.ActionApiService;
import us.zacharymaddox.scorecard.api.service.ServiceApiService;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardClientException;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardErrorCode;

@Controller
@RequestMapping("/portal/action")
@Profile({"portal"})
public class ActionPortalController {
	
	@Autowired
	private ServiceApiService serviceApiService;
	
	@Autowired
	private ActionApiService actionApiService;
	
	@GetMapping("/{service_id}/add")
	public String add(@PathVariable("service_id") Long serviceId, Model model) {
		Action action = new Action();
		action.setService(serviceApiService.getService(serviceId));
		model.addAttribute("action", action);
		return "portal/action/add"; 
	}
	
	@PostMapping("/{service_id}")
	public String save(@PathVariable("service_id") Long serviceId, @ModelAttribute("action") @Valid Action action, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			action.setService(serviceApiService.getService(serviceId));
			model.addAttribute("action", action);
			return "portal/action/add";
		} else {
			try {
				action.setService(serviceApiService.getService(serviceId));
				actionApiService.saveAction(action);
			} catch (ScoreCardClientException e) {
				if (ScoreCardErrorCode.ACTION_NAME_TAKEN.equals(e.getError())) {
					bindingResult.rejectValue("name", ScoreCardErrorCode.ACTION_NAME_TAKEN.name(), "name in use");
					return "portal/action/add";
				} else {
					throw e;
				}
			}
			return "redirect:/portal/service/detail?service_id=" + serviceId;
		}
	}


}

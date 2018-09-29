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

import us.zacharymaddox.scorecard.api.domain.Service;
import us.zacharymaddox.scorecard.api.service.ServiceApiService;
import us.zacharymaddox.scorecard.core.domain.exception.ScoreCardClientException;
import us.zacharymaddox.scorecard.core.domain.exception.ScoreCardErrorCode;

@Controller
@RequestMapping("/portal/service")
@Profile({"portal"})
public class ServicePortalController {
	
	@Autowired
	private ServiceApiService serviceApiService;
	
	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("services", serviceApiService.getServices());
		return "portal/service/list";
	}
	
	@GetMapping("/{service_id}")
	public String details(@PathVariable("service_id") Long serviceId, Model model) {
		model.addAttribute("service", serviceApiService.getService(serviceId));
		return "portal/service/detail";
	}
	
	@GetMapping("/add")
	public String add(Model model) {
		model.addAttribute("service", new Service());
		return "portal/service/add"; 
	}
	
	@PostMapping
	public String save(@ModelAttribute("service") @Valid Service service, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("service", service);
			return "portal/service/add";
		} else {
			Service savedServcie = null;
			try {
				savedServcie = serviceApiService.saveService(service);
			} catch (ScoreCardClientException e) {
				if (ScoreCardErrorCode.SERVICE_NAME_TAKEN.equals(e.getError())) {
					bindingResult.rejectValue("name", ScoreCardErrorCode.SERVICE_NAME_TAKEN.name(), "name in use");
					return "portal/service/add";
				} else {
					throw e;
				}
			}
			return "redirect:/portal/service/" + savedServcie.getServiceId();
		}
	}

}

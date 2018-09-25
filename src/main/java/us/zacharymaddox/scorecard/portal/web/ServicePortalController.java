package us.zacharymaddox.scorecard.portal.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import us.zacharymaddox.scorecard.api.service.ServiceApiService;

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

}

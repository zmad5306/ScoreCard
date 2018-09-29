package us.zacharymaddox.scorecard.core.web.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import us.zacharymaddox.scorecard.core.domain.Service;
import us.zacharymaddox.scorecard.core.domain.exception.ScoreCardClientException;
import us.zacharymaddox.scorecard.core.domain.exception.ScoreCardErrorCode;
import us.zacharymaddox.scorecard.core.service.ServiceService;

@RestController
@RequestMapping("/api/v1/service")
public class ServiceController {
	
	@Autowired
	private ServiceService serviceService;
	
	@GetMapping(value="/list", produces="application/json")
	public List<Service> getServices() {
		return serviceService.getServices();
	}
	
	@GetMapping(produces="application/json")
	public Service getService(@RequestParam(name="name", required=true) String name) {
		return serviceService.getServiceByName(name);
	}
	
	@GetMapping(value="/{service_id}", produces="application/json")
	public Service getService(@PathVariable("service_id") Long serviceId) {
		return serviceService.getService(serviceId);
	}
	
	@PostMapping(produces="application/json", consumes="application/json")
	public Service saveService(@RequestBody @Valid Service service, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.SERVICE_INVALID);
		}
		return serviceService.saveService(service);
	}

}

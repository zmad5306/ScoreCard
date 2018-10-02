package us.zacharymaddox.scorecard.portal.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.zacharymaddox.scorecard.api.domain.Service;
import us.zacharymaddox.scorecard.api.service.ServiceApiService;

@Component
public class ServiceHandler {
	
	@Autowired
	private ServiceApiService serviceApiService;
	
	public Service lookup(Long serviceId) {
		return serviceApiService.getService(serviceId);
	}
	
	public void delete(Long serviceId) {
		serviceApiService.delete(serviceId);
	}
	
}

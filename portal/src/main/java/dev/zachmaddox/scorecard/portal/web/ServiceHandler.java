package dev.zachmaddox.scorecard.portal.web;

import dev.zachmaddox.scorecard.lib.domain.Service;
import dev.zachmaddox.scorecard.lib.service.ServiceApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused") // methods are called from pages
@RequiredArgsConstructor
@Component
public class ServiceHandler {
	
	private final ServiceApiService serviceApiService;
	
	public Service lookup(Long serviceId) {
		return serviceApiService.getService(serviceId);
	}
	
	public void delete(Long serviceId) {
		serviceApiService.delete(serviceId);
	}
	
}

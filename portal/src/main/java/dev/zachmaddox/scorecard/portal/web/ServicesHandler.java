package dev.zachmaddox.scorecard.portal.web;

import java.util.List;

import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardClientException;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardErrorCode;
import dev.zachmaddox.scorecard.lib.domain.Service;
import dev.zachmaddox.scorecard.lib.domain.exception.ServiceNameTakenException;
import dev.zachmaddox.scorecard.lib.service.ServiceApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused") // methods are called from pages
@RequiredArgsConstructor
@Component
public class ServicesHandler {
	
	private final ServiceApiService serviceApiService;
	
	public List<Service> loadServices() {
		return serviceApiService.getServices();
	}
	
	public Service newService() {
		return new Service();
	}
	
	public Long save(Service service) {
		try {
			Service svc = serviceApiService.saveService(service);
			return svc.getServiceId();
		} catch (ScoreCardClientException e) {
			if (ScoreCardErrorCode.SERVICE_NAME_IN_USE.equals(e.getError())) {
				throw new ServiceNameTakenException();
			} else {
				throw e;
			}
		}
	}
	
}

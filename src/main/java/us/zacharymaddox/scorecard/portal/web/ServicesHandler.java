package us.zacharymaddox.scorecard.portal.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.zacharymaddox.scorecard.api.domain.Service;
import us.zacharymaddox.scorecard.api.domain.exception.ServiceNameTakenException;
import us.zacharymaddox.scorecard.api.service.ServiceApiService;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardClientException;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardErrorCode;

@Component
public class ServicesHandler {
	
	@Autowired
	private ServiceApiService serviceApiService;
	
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
			if (ScoreCardErrorCode.SERVICE_NAME_TAKEN.equals(e.getError())) {
				throw new ServiceNameTakenException();
			} else {
				throw e;
			}
		}
	}
	
}

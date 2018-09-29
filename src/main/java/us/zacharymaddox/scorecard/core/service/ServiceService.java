package us.zacharymaddox.scorecard.core.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import us.zacharymaddox.scorecard.core.domain.Service;
import us.zacharymaddox.scorecard.core.repository.ServiceRepository;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardClientException;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardErrorCode;

@org.springframework.stereotype.Service
public class ServiceService {
	
	@Autowired
	private ServiceRepository serviceRepository;
	
	@Transactional(readOnly=true)
	public List<Service> getServices() {
		return serviceRepository.findAll();
	}
	
	@Transactional(readOnly=true)
	public Service getService(Long serviceId) {
		Optional<Service> s = serviceRepository.findById(serviceId);
		if (!s.isPresent()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.SERVICE_DNE);
		}
		return s.get();
	}
	
	@Transactional(readOnly=true)
	public Service getServiceByName(String name) {
		Optional<Service> s = serviceRepository.findByName(name);
		if (!s.isPresent()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.SERVICE_DNE);
		}
		return s.get();
	}
	
	@Transactional
	public Service saveService(Service service) {
		Optional<Service> svc = serviceRepository.findByName(service.getName());
		if (svc.isPresent()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.SERVICE_NAME_TAKEN);
		}
		return serviceRepository.save(service);
	}

}

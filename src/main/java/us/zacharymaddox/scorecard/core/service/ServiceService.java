package us.zacharymaddox.scorecard.core.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import us.zacharymaddox.scorecard.core.domain.Action;
import us.zacharymaddox.scorecard.core.domain.Service;
import us.zacharymaddox.scorecard.core.domain.TransactionAction;
import us.zacharymaddox.scorecard.core.repository.ActionRepository;
import us.zacharymaddox.scorecard.core.repository.ServiceRepository;
import us.zacharymaddox.scorecard.core.repository.TransactionActionRepository;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardClientException;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardErrorCode;

@org.springframework.stereotype.Service
public class ServiceService {
	
	@Autowired
	private ServiceRepository serviceRepository;
	@Autowired
	private TransactionActionRepository transactionActionRepository;
	@Autowired
	private ActionRepository actionRepository;
	
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
	
	@Transactional
	public void delete(Long serviceId) {
		Optional<Service> s = serviceRepository.findById(serviceId);
		if (!s.isPresent()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.SERVICE_DNE);
		}
		Service service = s.get();
		for (Action action : service.getActions()) {
			List<TransactionAction> tas = transactionActionRepository.findByActionId(action.getActionId());
			if(tas.size() > 0) {
				throw new ScoreCardClientException(ScoreCardErrorCode.CANNOT_DELETE_SERVICE_ACTION_IN_USE);
			}
			// should be save to delete while checking, right? I mean its transactional :)
			actionRepository.delete(action);
		}
		serviceRepository.delete(service);
	}

}

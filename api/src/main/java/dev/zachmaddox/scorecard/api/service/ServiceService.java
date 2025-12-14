package dev.zachmaddox.scorecard.api.service;

import java.util.List;
import java.util.Optional;

import dev.zachmaddox.scorecard.api.domain.Action;
import dev.zachmaddox.scorecard.api.domain.Service;
import dev.zachmaddox.scorecard.api.domain.TransactionAction;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardClientException;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardErrorCode;
import dev.zachmaddox.scorecard.api.repository.ActionRepository;
import dev.zachmaddox.scorecard.api.repository.ServiceRepository;
import dev.zachmaddox.scorecard.api.repository.TransactionActionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {
	
	private final ServiceRepository serviceRepository;
	private final TransactionActionRepository transactionActionRepository;
	private final ActionRepository actionRepository;
	
	@Transactional(readOnly=true)
	public List<Service> getServices() {
		return serviceRepository.findAll();
	}
	
	@Transactional(readOnly=true)
	public Service getService(Long serviceId) {
		return serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ScoreCardClientException(ScoreCardErrorCode.SERVICE_DNE));
	}
	
	@Transactional(readOnly=true)
	public Service getServiceByName(String name) {
		return serviceRepository.findByName(name)
                .orElseThrow(() -> new ScoreCardClientException(ScoreCardErrorCode.SERVICE_DNE));
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
		Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ScoreCardClientException(ScoreCardErrorCode.SERVICE_DNE));

		for (Action action : service.getActions()) {
			List<TransactionAction> tas = transactionActionRepository.findByActionId(action.getActionId());
			if(!tas.isEmpty()) {
				throw new ScoreCardClientException(ScoreCardErrorCode.CANNOT_DELETE_SERVICE_ACTION_IN_USE);
			}
			// should be safe to delete while checking, right? I mean its transactional :)
			actionRepository.delete(action);
		}
		serviceRepository.delete(service);
	}

}

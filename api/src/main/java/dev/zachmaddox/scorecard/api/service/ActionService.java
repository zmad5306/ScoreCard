package dev.zachmaddox.scorecard.api.service;

import java.util.List;
import java.util.Optional;

import dev.zachmaddox.scorecard.api.domain.Action;
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
public class ActionService {

	private final ActionRepository actionRepository;
	private final ServiceRepository serviceRepository;
	private final TransactionActionRepository transactionActionRepository;
	
	@Transactional(readOnly=true)
	public Action getAction(Long actionId) {
		return actionRepository.findById(actionId).orElseThrow(() -> new ScoreCardClientException(ScoreCardErrorCode.TRANSACTION_DNE));
	}
	
	@Transactional(readOnly=true)
	public Action getActionByName(String name) {
		return actionRepository.findByName(name).orElseThrow(() -> new ScoreCardClientException(ScoreCardErrorCode.ACTION_DNE));
	}
	
	@Transactional
	public Action saveAction(Action action) {
		Optional<Action> act = actionRepository.findByServiceAndName(action.getService(), action.getName());
		if (act.isPresent()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.ACTION_NAME_IN_USE);
		} else {
			return actionRepository.save(action);
		}
	}

	@Transactional(readOnly=true)
	public List<Action> getAllActions() {
		return actionRepository.findAll();
	}

	@Transactional(readOnly=true)
	public List<Action> getActions(Long serviceId) {
        return serviceRepository.findById(serviceId)
                .map(actionRepository::findByService)
                .orElseThrow(() -> new ScoreCardClientException(ScoreCardErrorCode.SERVICE_DNE));
	}
	
	@Transactional
	public void delete(Long actionId) {
		List<TransactionAction> tas = transactionActionRepository.findByActionId(actionId);
		if(!tas.isEmpty()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.CANNOT_DELETE_ACTION_IN_USE);
		}
		actionRepository.deleteById(actionId);
	}
	
}

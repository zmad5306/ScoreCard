package dev.zachmaddox.scorecard.api.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import dev.zachmaddox.scorecard.api.domain.*;
import dev.zachmaddox.scorecard.common.domain.Authorization;
import dev.zachmaddox.scorecard.common.domain.AuthorizationResult;
import dev.zachmaddox.scorecard.common.domain.ScoreCardActionStatus;
import dev.zachmaddox.scorecard.common.domain.UpdateRequest;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardClientException;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardErrorCode;
import dev.zachmaddox.scorecard.api.repository.ScoreCardActionRepository;
import dev.zachmaddox.scorecard.api.repository.ScoreCardRepository;
import dev.zachmaddox.scorecard.api.repository.TransactionActionRepository;
import dev.zachmaddox.scorecard.api.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScoreCardService {

	private final ScoreCardRepository scoreCardRepository;
	private final TransactionRepository transactionRepository;
	private final ScoreCardActionRepository scoreCardActionRepository;
	private final TransactionActionRepository transactionActionRepository;
	
	private static final Map<ScoreCardActionStatus, List<ScoreCardActionStatus>> VALID_STATE_CHANGES;
	
	static {
		VALID_STATE_CHANGES = new HashMap<>();
		VALID_STATE_CHANGES.put(ScoreCardActionStatus.PENDING, List.of(new ScoreCardActionStatus[]{ScoreCardActionStatus.CANCELLED, ScoreCardActionStatus.PROCESSING}));
		VALID_STATE_CHANGES.put(ScoreCardActionStatus.CANCELLED, List.of(new ScoreCardActionStatus[0]));
		VALID_STATE_CHANGES.put(ScoreCardActionStatus.PROCESSING, List.of(new ScoreCardActionStatus[]{ScoreCardActionStatus.COMPLETED, ScoreCardActionStatus.UNKNOWN, ScoreCardActionStatus.FAILED}));
		VALID_STATE_CHANGES.put(ScoreCardActionStatus.UNKNOWN, List.of(new ScoreCardActionStatus[]{ScoreCardActionStatus.COMPLETED, ScoreCardActionStatus.FAILED}));
		VALID_STATE_CHANGES.put(ScoreCardActionStatus.FAILED, List.of(new ScoreCardActionStatus[] {ScoreCardActionStatus.CANCELLED}));
		VALID_STATE_CHANGES.put(ScoreCardActionStatus.COMPLETED, List.of(new ScoreCardActionStatus[0]));
	}
	
	private List<ScoreCardAction> createScoreCardActions(List<TransactionAction> actions, ScoreCard scoreCard) {
        return actions.stream().map(a -> {
            ScoreCardAction act = new ScoreCardAction();
            act.setActionId(a.getAction().getActionId());
            act.setMethod(a.getAction().getMethod());
            act.setName(a.getAction().getName());
            act.setPath(a.getAction().getName());
            act.setScoreCard(scoreCard);
            act.setStatus(ScoreCardActionStatus.PENDING);
            return act;
        }).collect(Collectors.toList());
	}
	
	@Transactional
	public ScoreCardId getNextScoreCardId() {
		Long sci = scoreCardRepository.fetchNextScoreCardId();
		return new ScoreCardId(sci);
	}
	
	@Transactional
	public ScoreCard createScoreCard(Long scoreCardId, Long transactionId) {
		Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ScoreCardClientException(ScoreCardErrorCode.TRANSACTION_DNE));

		ScoreCard scoreCard = new ScoreCard();
		scoreCard.setScoreCardId(scoreCardId);
		scoreCard.setTransactionId(transactionId);
		scoreCard.setTransactionName(transaction.getName());
		
		scoreCard = scoreCardRepository.save(scoreCard);
		
		List<ScoreCardAction> actions = createScoreCardActions(transaction.getActions(), scoreCard);
		
		// save the scorecard actions, we need the ids set in order to build the dependencies
		actions = scoreCardActionRepository.saveAll(actions);
		
		scoreCard.setActions(actions);
		
		// build dependencies between scorecard actions
		for (ScoreCardAction sca : actions) {
			Set<ScoreCardAction> dependsOn = new HashSet<>(); 
			List<TransactionAction> tas = transactionActionRepository.findByTransactionIdAndActionId(transaction.getTransactionId(), sca.getActionId());
			for (TransactionAction ta : tas) {
				for (TransactionAction dta : ta.getDependsOn()) {
					Action action = dta.getAction();
					scoreCardActionRepository.findByScoreCardIdAndActionId(scoreCard.getScoreCardId(), action.getActionId())
                            .ifPresent(dependsOn::add);
				}
			}
			sca.setDependsOn(dependsOn);
			scoreCardActionRepository.save(sca);
		}
		
		return scoreCard;
	}
	
	@Transactional(readOnly=true)
	public ScoreCard getScoreCard(Long scoreCardId) {
		return scoreCardRepository.findById(scoreCardId)
                .orElseThrow(() -> new ScoreCardClientException(ScoreCardErrorCode.SCORE_CARD_DNE));
	}
	
	@Transactional(readOnly=true)
	public List<ScoreCard> getScoreCards(String transactionName, Integer page, Integer rows) {
		Pageable pageable = PageRequest.of(page, rows);
		return scoreCardRepository.findByTransactionNameOrderByScoreCardIdDesc(transactionName, pageable);
	}
	
	@Transactional(readOnly=true)
	public Long countAll() {
		return scoreCardRepository.count();
	}
	
	@Transactional(readOnly=true)
	public Long countByTransactionName(String transactionName) {
		return scoreCardRepository.countByTransactionName(transactionName);
	}

	@Transactional(readOnly=true)
	public List<ScoreCard> getScoreCards(Integer page, Integer rows) {
		Pageable pageable = PageRequest.of(page, rows);
		return scoreCardRepository.findAllOrderByScoreCardIdDesc(pageable);
	}
	
	@Transactional(readOnly=true)
	public List<ScoreCard> getScoreCards(ScoreCardActionStatus status, String transactionName, Integer page, Integer rows) {
		Pageable pageable = PageRequest.of(page, rows);
		return scoreCardRepository.findByActionStatusAndTransactionNameOrderByScoreCardIdDesc(status, transactionName, pageable);
	}
	
	@Transactional
	public AuthorizationResult authorize(Long scoreCardId, Long actionId) {
		Optional<ScoreCard> sc = scoreCardRepository.findById(scoreCardId);
		if (sc.isEmpty()) {
			return new AuthorizationResult(Authorization.WAIT);
		}
		
		ScoreCard scoreCard = sc.get();
		
		Optional<ScoreCardAction> a = scoreCardActionRepository.findByScoreCardIdAndActionId(scoreCardId, actionId);
		
		if (a.isEmpty()) {
			return new AuthorizationResult(Authorization.WAIT);
		}
		
		ScoreCardAction sca = a.get();
		
		switch (sca.getStatus()) {
			case PROCESSING:
			case COMPLETED:
			case FAILED:
			case CANCELLED:
				return new AuthorizationResult(Authorization.SKIP);
			case PENDING:
				// action is pending, lets see if its authorized
				List<ScoreCardActionStatus> failedStatus = List.of(new ScoreCardActionStatus[]{ScoreCardActionStatus.CANCELLED, ScoreCardActionStatus.FAILED});
				Long failedActionsInScoreCard = scoreCardActionRepository.countByScoreCardAndStatusIn(scoreCard, failedStatus);
				
				// check for other actions that ended abnormally, cancel all further actions
				/* TODO make failed action authorization outcome extensible
				 	In some use cases it might be appropriate to process the other services and repair the failed steps
				 	later. However, if there are dependencies this might get complicated... **/
				if (failedActionsInScoreCard > 0) {
					return new AuthorizationResult(Authorization.CANCEL);
				} else {
					// check for dependencies status
					Set<ScoreCardAction> dependencies = sca.getDependsOn();
					
					if (dependencies != null && !dependencies.isEmpty()) {
						// count the finished dependencies
						long completedActions = dependencies.stream().filter(dep -> ScoreCardActionStatus.COMPLETED.equals(dep.getStatus())).count();
						
						// check that all dependencies are finished before returning PROCESS
						if (completedActions == dependencies.size()) {							
							updateActionStatus(new UpdateRequest(scoreCardId, sca.getActionId(), ScoreCardActionStatus.PROCESSING), false);
							return new AuthorizationResult(Authorization.PROCESS);
						} 
						// a dependency isn't finished, WAIT on it
						else {
							return new AuthorizationResult(Authorization.WAIT);
						}
					} 
					// there aren't any dependencies so go ahead and process
					else {
						updateActionStatus(new UpdateRequest(scoreCardId, sca.getActionId(), ScoreCardActionStatus.PROCESSING), false);
						return new AuthorizationResult(Authorization.PROCESS);
					}
				}
			default:
				return new AuthorizationResult(Authorization.WAIT);
		}
		
	}
	
	@Transactional
	public void updateActionStatus(UpdateRequest request) {
		updateActionStatus(request, true);
	}
	
	@Transactional
    protected void updateActionStatus(UpdateRequest request, Boolean mustBeProcessing) {
		Long scoreCardId = request.getScoreCardId();
		Long actionId = request.getActionId();
		ScoreCardActionStatus status = request.getStatus();	
		ScoreCardAction sca = scoreCardActionRepository.findByScoreCardIdAndActionId(scoreCardId, actionId)
                .orElseThrow(() -> new ScoreCardClientException(ScoreCardErrorCode.SCORE_CARD_ACTION_DNE));
		
		//calling from the outside world (public overload), the action must be in processing status to complete
		//internal updates are allowed w/o the scorecard being in processing status
		if (mustBeProcessing && ScoreCardActionStatus.COMPLETED.equals(status) && !ScoreCardActionStatus.PROCESSING.equals(sca.getStatus())) {
			// TODO design says to update this to UNKNOWN state, not blow up
			throw new ScoreCardClientException(ScoreCardErrorCode.ILLEGAL_STATE_CHANGE_NOT_AUTHORIZED);
		}
		
		List<ScoreCardActionStatus> valid = ScoreCardService.VALID_STATE_CHANGES.get(sca.getStatus());
		if (!valid.contains(status)) {
			// TODO design says to update this to UNKNOWN state, not blow up
			throw new ScoreCardClientException(ScoreCardErrorCode.ILLEGAL_STATE_CHANGE);
		}
		
		if (ScoreCardActionStatus.PROCESSING.equals(status)) {
			sca.setStartTimestamp(LocalDateTime.now());
		}
		if (ScoreCardActionStatus.CANCELLED.equals(status)
				|| ScoreCardActionStatus.COMPLETED.equals(status)
				|| ScoreCardActionStatus.FAILED.equals(status)) {
			sca.setEndTimestamp(LocalDateTime.now());
		}
		sca.setStatus(status);
		sca.setMetadata(request.getMetadata());
	}

}

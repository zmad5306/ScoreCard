package us.zacharymaddox.scorecard.core.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import us.zacharymaddox.scorecard.core.domain.Action;
import us.zacharymaddox.scorecard.core.domain.ScoreCard;
import us.zacharymaddox.scorecard.core.domain.ScoreCardAction;
import us.zacharymaddox.scorecard.core.domain.Transaction;
import us.zacharymaddox.scorecard.core.domain.TransactionAction;
import us.zacharymaddox.scorecard.core.repository.ScoreCardActionRepository;
import us.zacharymaddox.scorecard.core.repository.ScoreCardRepository;
import us.zacharymaddox.scorecard.core.repository.TransactionActionRepository;
import us.zacharymaddox.scorecard.core.repository.TransactionRepository;
import us.zacharymaddox.scorecard.domain.Authorization;
import us.zacharymaddox.scorecard.domain.AuthorizationResult;
import us.zacharymaddox.scorecard.domain.ScoreCardActionStatus;
import us.zacharymaddox.scorecard.domain.ScoreCardId;
import us.zacharymaddox.scorecard.domain.UpdateRequest;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardClientException;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardErrorCode;
	
@Service
public class ScoreCardService {

	@Autowired
	private ScoreCardRepository scoreCardRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private ScoreCardActionRepository scoreCardActionRepository;
	
	@Autowired
	private TransactionActionRepository transactionActionRepository;
	
	private static final Map<ScoreCardActionStatus, List<ScoreCardActionStatus>> VALID_STATE_CHANGES;
	
	static {
		VALID_STATE_CHANGES = new HashMap<>();
		VALID_STATE_CHANGES.put(ScoreCardActionStatus.PENDING, Arrays.asList(new ScoreCardActionStatus[]{ScoreCardActionStatus.CANCELLED, ScoreCardActionStatus.PROCESSING}));
		VALID_STATE_CHANGES.put(ScoreCardActionStatus.CANCELLED, Arrays.asList(new ScoreCardActionStatus[0]));
		VALID_STATE_CHANGES.put(ScoreCardActionStatus.PROCESSING, Arrays.asList(new ScoreCardActionStatus[]{ScoreCardActionStatus.COMPLETED, ScoreCardActionStatus.UNKNOWN, ScoreCardActionStatus.FAILED}));
		VALID_STATE_CHANGES.put(ScoreCardActionStatus.UNKNOWN, Arrays.asList(new ScoreCardActionStatus[]{ScoreCardActionStatus.COMPLETED, ScoreCardActionStatus.FAILED}));
		VALID_STATE_CHANGES.put(ScoreCardActionStatus.FAILED, Arrays.asList(new ScoreCardActionStatus[] {ScoreCardActionStatus.CANCELLED}));
		VALID_STATE_CHANGES.put(ScoreCardActionStatus.COMPLETED, Arrays.asList(new ScoreCardActionStatus[0]));
	}
	
	private List<ScoreCardAction> createScoreCardActions(List<TransactionAction> actions, ScoreCard scoreCard) {
		List<ScoreCardAction> scActions = actions.stream().map(a -> {
			ScoreCardAction act = new ScoreCardAction();
			act.setActionId(a.getAction().getActionId());
			act.setMethod(a.getAction().getMethod());
			act.setName(a.getAction().getName());
			act.setPath(a.getAction().getName());
			act.setScoreCard(scoreCard);
			act.setStatus(ScoreCardActionStatus.PENDING);
			return act;
		}).collect(Collectors.toList());
		return scActions;
	}
	
	@Transactional
	public ScoreCardId getNextScoreCardId() {
		Long sci = scoreCardRepository.fetchNextScoreCardId();
		return new ScoreCardId(sci);
	}
	
	@Transactional
	public ScoreCard createScoreCard(Long scoreCardId, Long transactionId) {
		Optional<Transaction> t = transactionRepository.findById(transactionId);
		if (!t.isPresent()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.TRANSACTION_DNE);
		}
		
		Transaction transaction = t.get();
		
		ScoreCard scoreCard = new ScoreCard();
		scoreCard.setScoreCardId(scoreCardId);
		scoreCard.setTransactionId(transactionId);
		scoreCard.setTransactionName(transaction.getName());
		
		scoreCard = scoreCardRepository.save(scoreCard);
		
		List<ScoreCardAction> actions = createScoreCardActions(t.get().getActions(), scoreCard);
		
		// save the score card actions, we need the ids set in order to build the dependencies
		actions = scoreCardActionRepository.saveAll(actions);
		
		scoreCard.setActions(actions);
		
		// build dependencies between score card actions
		for (ScoreCardAction sca : actions) {
			Set<ScoreCardAction> dependsOn = new HashSet<>(); 
			List<TransactionAction> tas = transactionActionRepository.findByTransactionIdAndActionId(transaction.getTransactionId(), sca.getActionId());
			for (TransactionAction ta : tas) {
				for (TransactionAction dta : ta.getDependsOn()) {
					Action action = dta.getAction();
					Optional<ScoreCardAction> dependency = scoreCardActionRepository.findByScoreCardIdAndActionId(scoreCard.getScoreCardId(), action.getActionId());
					if (dependency.isPresent()) {
						dependsOn.add(dependency.get());
					}
				}
			}
			sca.setDependsOn(dependsOn);
			scoreCardActionRepository.save(sca);
		}
		
		return scoreCard;
	}
	
	@Transactional(readOnly=true)
	public ScoreCard getScoreCard(Long scoreCardId) {
		Optional<ScoreCard> sc = scoreCardRepository.findById(scoreCardId);
		if (!sc.isPresent()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.SCORE_CARD_DNE);
		}
		
		return sc.get();
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
		if (!sc.isPresent()) {
			return new AuthorizationResult(Authorization.WAIT);
		}
		
		ScoreCard scoreCard = sc.get();
		
		Optional<ScoreCardAction> a = scoreCardActionRepository.findByScoreCardIdAndActionId(scoreCardId, actionId);
		
		if (!a.isPresent()) {
			return new AuthorizationResult(Authorization.WAIT);
		}
		
		ScoreCardAction sca = a.get();
		
		switch (sca.getStatus()) {
			case PROCESSING:
			case COMPLETED:
			case FAILED:
			case CANCELLED:
				return new AuthorizationResult(Authorization.SKIP);
			case UNKNOWN:
				return new AuthorizationResult(Authorization.WAIT);
			case PENDING:
				// action is pending, lets see if its authorized
				List<ScoreCardActionStatus> failedStatus = Arrays.asList(new ScoreCardActionStatus[]{ScoreCardActionStatus.CANCELLED, ScoreCardActionStatus.FAILED});
				Long failedActionsInScoreCard = scoreCardActionRepository.countByScoreCardAndStatusIn(scoreCard, failedStatus);
				
				// check for other actions that ended abnormally, cancel all further actions
				/** TODO make failed action authorization outcome extensible
				 	In some use cases it might be appropriate to process the other services and repair the failed steps
				 	later. However, if there are dependencies this might get complicated... **/
				if (failedActionsInScoreCard > 0) {
					return new AuthorizationResult(Authorization.CANCEL);
				} else {
					// check for dependencies status
					Set<ScoreCardAction> dependencies = sca.getDependsOn();
					
					if (dependencies != null && !dependencies.isEmpty()) {
						// count the finished dependencies
						Long completedActions = dependencies.stream().filter(dep -> ScoreCardActionStatus.COMPLETED.equals(dep.getStatus())).count();
						
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
	private void updateActionStatus(UpdateRequest request, Boolean mustBeProcessing) {
		Long scoreCardId = request.getScoreCardId();
		Long actionId = request.getActionId();
		ScoreCardActionStatus status = request.getStatus();	
		Optional<ScoreCardAction> a = scoreCardActionRepository.findByScoreCardIdAndActionId(scoreCardId, actionId);
		
		if (!a.isPresent()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.SCORE_CARD_ACTION_DNE);
		}
		
		ScoreCardAction sca = a.get();
		
		//calling from the outside world (public overload), the action must be in processing status to complete
		//internal updates are allowed w/o the score card being in processing status
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
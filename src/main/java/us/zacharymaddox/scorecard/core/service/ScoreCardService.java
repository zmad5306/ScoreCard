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
import us.zacharymaddox.scorecard.core.domain.exception.ScoreCardClientException;
import us.zacharymaddox.scorecard.core.domain.exception.ScoreCardErrorCode;
import us.zacharymaddox.scorecard.core.repository.ScoreCardActionRepository;
import us.zacharymaddox.scorecard.core.repository.ScoreCardRepository;
import us.zacharymaddox.scorecard.core.repository.TransactionActionRepository;
import us.zacharymaddox.scorecard.core.repository.TransactionRepository;
import us.zacharymaddox.scorecard.domain.Authorization;
import us.zacharymaddox.scorecard.domain.AuthorizationResult;
import us.zacharymaddox.scorecard.domain.ScoreCardActionStatus;
import us.zacharymaddox.scorecard.domain.ScoreCardId;
import us.zacharymaddox.scorecard.domain.ScoreCardStatus;
import us.zacharymaddox.scorecard.domain.UpdateRequest;
	
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
		VALID_STATE_CHANGES.put(ScoreCardActionStatus.FAILED, Arrays.asList(new ScoreCardActionStatus[0]));
		VALID_STATE_CHANGES.put(ScoreCardActionStatus.COMPLETED, Arrays.asList(new ScoreCardActionStatus[0]));
	}
	
	private List<ScoreCardAction> createScoreCardActions(List<TransactionAction> actions, ScoreCard scoreCard) {
		List<ScoreCardAction> scActions = actions.stream().map(a -> {
			ScoreCardAction act = new ScoreCardAction();
			act.setAction(a.getAction());
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
		
		ScoreCard sc = new ScoreCard();
		sc.setScoreCardId(scoreCardId);
		sc.setStartTimestamp(LocalDateTime.now());
		sc.setTransaction(t.get());
		
		sc = scoreCardRepository.save(sc);
		
		List<ScoreCardAction> actions = createScoreCardActions(t.get().getActions(), sc);
		
		actions = scoreCardActionRepository.saveAll(actions);
		
		sc.setActions(actions);
		
		for (ScoreCardAction sca : actions) {
			Set<ScoreCardAction> dependsOn = new HashSet<>(); 
			List<TransactionAction> tas = transactionActionRepository.findByTransactionAndAction(transaction, sca.getAction());
			for (TransactionAction ta : tas) {
				for (TransactionAction dta : ta.getDependsOn()) {
					Action action = dta.getAction();
					Optional<ScoreCardAction> dependency = scoreCardActionRepository.findByScoreCardIdAndActionId(sc.getScoreCardId(), action.getActionId());
					if (dependency.isPresent()) {
						dependsOn.add(dependency.get());
					}
				}
			}
			sca.setDependsOn(dependsOn);
			scoreCardActionRepository.save(sca);
		}
		
		return sc;
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
	public List<ScoreCard> getScoreCards(
				ScoreCardStatus scoreCardStatus,
				Integer page,
				Integer rows
			) {
		Pageable pageable = PageRequest.of(page, rows);
		
		return scoreCardRepository.findByScoreCardStatusOrderByScoreCardIdDesc(scoreCardStatus, pageable);
	}
	
	@Transactional
	public AuthorizationResult authorize(Long scoreCardId, Long actionId) {
		Optional<ScoreCard> sc = scoreCardRepository.findById(scoreCardId);
		if (!sc.isPresent()) {
			// TODO need to respond WAIT here if score card creation is async, might have to push this to the client 
			// to retry... how can I know that its async if it hasn't been created yet...
			throw new ScoreCardClientException(ScoreCardErrorCode.SCORE_CARD_DNE);
		}
		
		ScoreCard scoreCard = sc.get();
		
		Optional<ScoreCardAction> a = scoreCardActionRepository.findByScoreCardIdAndActionId(scoreCardId, actionId);
		
		if (!a.isPresent()) {
			// TODO need to respond WAIT here if score card creation is async, might have to push this to the client 
			// to retry... how can I know that its async if it hasn't been created yet...
			throw new ScoreCardClientException(ScoreCardErrorCode.SCORE_CARD_ACTION_DNE);
		}
		
		ScoreCardAction sca = a.get();
		
		switch (sca.getStatus()) {
			case PROCESSING:
				return new AuthorizationResult(Authorization.SKIP);
			case COMPLETED:
				return new AuthorizationResult(Authorization.SKIP);
			case FAILED:
				return new AuthorizationResult(Authorization.CANCEL);
			case CANCELLED:
				return new AuthorizationResult(Authorization.CANCEL);
			case UNKNOWN:
				return new AuthorizationResult(Authorization.WAIT);
			case PENDING:
				// action is pending, lets see if its authorized
				List<ScoreCardActionStatus> failedStatus = Arrays.asList(new ScoreCardActionStatus[]{ScoreCardActionStatus.CANCELLED, ScoreCardActionStatus.FAILED});
				Long failedActionsInScoreCard = scoreCardActionRepository.countByScoreCardAndStatusIn(scoreCard, failedStatus);
				
				// check for other actions that ended abnormally, cancel all further actions
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
							updateActionStatusInternal(new UpdateRequest(scoreCardId, sca.getAction().getActionId(), ScoreCardActionStatus.PROCESSING), false);
							return new AuthorizationResult(Authorization.PROCESS);
						} 
						// a dependency isn't finished, WAIT on it
						else {
							return new AuthorizationResult(Authorization.WAIT);
						}
					} 
					// there aren't any dependencies so go ahead and process
					else {
						updateActionStatusInternal(new UpdateRequest(scoreCardId, sca.getAction().getActionId(), ScoreCardActionStatus.PROCESSING), false);
						return new AuthorizationResult(Authorization.PROCESS);
					}
				}
			default:
				return new AuthorizationResult(Authorization.WAIT);
		}
		
	}
	
	@Transactional
	public void updateActionStatus(UpdateRequest request) {
		updateActionStatusInternal(request, true);
	}
	
	private void updateActionStatusInternal(UpdateRequest request, Boolean mustBeProcessing) {
		Long scoreCardId = request.getScoreCardId();
		Long actionId = request.getActionId();
		ScoreCardActionStatus status = request.getStatus();
		Optional<ScoreCard> sc = scoreCardRepository.findById(scoreCardId);
		if (!sc.isPresent()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.SCORE_CARD_DNE);
		}
		ScoreCard scoreCard = sc.get();		
		Optional<ScoreCardAction> a = scoreCardActionRepository.findByScoreCardIdAndActionId(scoreCardId, actionId);
		
		if (!a.isPresent()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.SCORE_CARD_ACTION_DNE);
		}
		
		ScoreCardAction sca = a.get();
		
		if (mustBeProcessing && ScoreCardActionStatus.COMPLETED.equals(status) && !ScoreCardActionStatus.PROCESSING.equals(sca.getStatus())) {
			throw new ScoreCardClientException(ScoreCardErrorCode.ILLEGAL_STATE_CHANGE_NOT_AUTHORIZED);
		}
		
		List<ScoreCardActionStatus> valid = ScoreCardService.VALID_STATE_CHANGES.get(sca.getStatus());
		if (!valid.contains(status)) {
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
		
		// TODO there is a race condition when setting the score card status to COMPELTED, on occasion all actions are completed but score card is not marked completed
		// this seems to be when using HTTP based Score Card Services
		List<ScoreCardActionStatus> completedStatus = Arrays.asList(new ScoreCardActionStatus[]{ScoreCardActionStatus.CANCELLED, ScoreCardActionStatus.FAILED, ScoreCardActionStatus.COMPLETED});
		Long completedActions = scoreCardActionRepository.countByScoreCardAndStatusIn(scoreCard, completedStatus);

		if (completedActions == scoreCard.getActions().size()) {
			scoreCard.setEndTimestamp(LocalDateTime.now());
			scoreCard.setScoreCardStatus(ScoreCardStatus.COMPLETED);
		}
		
		scoreCardRepository.save(scoreCard);
	}
	
}

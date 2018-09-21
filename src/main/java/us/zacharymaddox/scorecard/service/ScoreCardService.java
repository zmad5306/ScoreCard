package us.zacharymaddox.scorecard.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import us.zacharymaddox.scorecard.domain.Authorization;
import us.zacharymaddox.scorecard.domain.AuthorizationResult;
import us.zacharymaddox.scorecard.domain.ScoreCard;
import us.zacharymaddox.scorecard.domain.ScoreCardAction;
import us.zacharymaddox.scorecard.domain.ScoreCardActionStatus;
import us.zacharymaddox.scorecard.domain.Transaction;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardClientException;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardErrorCode;
import us.zacharymaddox.scorecard.repository.ScoreCardRepository;
import us.zacharymaddox.scorecard.repository.TransactionRepository;
	
@Service
public class ScoreCardService {

	@Autowired
	private ScoreCardRepository scoreCardRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
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
	
	private List<ScoreCardAction> buildActions(Transaction t) {
		return t.getActions().stream().map(a -> {
			ScoreCardAction act = new ScoreCardAction();
			act.setActionId(a.getActionId());
			act.setDependencies(a.getDependencies());
			act.setStatus(ScoreCardActionStatus.PENDING);
			return act;
		}).collect(Collectors.toList());
	}
	
	public ScoreCard createScoreCard(String transactionId) {
		Optional<Transaction> t = transactionRepository.findById(transactionId);
		if (!t.isPresent()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.TRANSACTION_DNE);
		}
		
		ScoreCard sc = new ScoreCard();
		sc.setStartTimestamp(LocalDateTime.now());
		sc.setTransactionId(transactionId);
		sc.setActions(buildActions(t.get()));
		
		return scoreCardRepository.save(sc);
	}
	
	public ScoreCard getScoreCard(String scoreCardId) {
		Optional<ScoreCard> sc = scoreCardRepository.findById(scoreCardId);
		if (!sc.isPresent()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.SCORE_CARD_DNE);
		}
		
		return sc.get();
	}
	
	public AuthorizationResult authorize(String scoreCardId, String actionId) {
		Optional<ScoreCard> sc = scoreCardRepository.findById(scoreCardId);
		if (!sc.isPresent()) {
			// TODO need to respond WAIT here if score card creation is async
			throw new ScoreCardClientException(ScoreCardErrorCode.SCORE_CARD_DNE);
		}
		ScoreCard scoreCard = sc.get();		
		Optional<ScoreCardAction> a = scoreCard.getAction(actionId);
		
		if (!a.isPresent()) {
			// TODO need to respond WAIT here if score card creation is async
			throw new ScoreCardClientException(ScoreCardErrorCode.SCORE_CARD_ACTION_DNE);
		}
		
		ScoreCardAction sca = a.get();
		
		switch (sca.getStatus()) {
			case PROCESSING:
				return new AuthorizationResult(sca, Authorization.SKIP);
			case COMPLETED:
				return new AuthorizationResult(sca, Authorization.SKIP);
			case FAILED:
				return new AuthorizationResult(sca, Authorization.CANCEL);
			case CANCELLED:
				return new AuthorizationResult(sca, Authorization.CANCEL);
			case UNKNOWN:
				return new AuthorizationResult(sca, Authorization.WAIT);
			case PENDING:
				// action is pending, lets see if its authorized
				Long failedActionsInScoreCard = scoreCard.getActions().stream().filter(atn -> ScoreCardActionStatus.CANCELLED.equals(atn.getStatus()) || ScoreCardActionStatus.FAILED.equals(atn.getStatus())).count();
				
				// check for other actions that ended abnormally, cancel all further actions
				if (failedActionsInScoreCard > 0) {
					return new AuthorizationResult(sca, Authorization.CANCEL);
				} else {
					// check for dependencies status
					List<String> dependencies = sca.getDependencies();
					
					if (dependencies != null && !dependencies.isEmpty()) {
						// count the finished dependencies
						Long finishedDepsCount = dependencies.stream().map(dep -> {
							Optional<ScoreCardAction> actn = scoreCard.getAction(dep);
							if (!actn.isPresent()) {
								// TODO need to respond WAIT here if score card creation is async
								throw new ScoreCardClientException(ScoreCardErrorCode.SCORE_CARD_ACTION_DEPENDENCY_DNE);
							}
							return actn.get();
						}).filter(actn -> ScoreCardActionStatus.COMPLETED.equals(actn.getStatus())).count();
						
						// check that all dependencies are finished before returning PROCESS
						if (finishedDepsCount == dependencies.size()) {
							updateActionStatusInternal(scoreCardId, actionId, ScoreCardActionStatus.PROCESSING, false);
							return new AuthorizationResult(sca,  Authorization.PROCESS);
						} 
						// a dependency isn't finished, WAIT on it
						else {
							return new AuthorizationResult(sca, Authorization.WAIT);
						}
					} 
					// there aren't any dependencies so go ahead and process
					else {
						updateActionStatusInternal(scoreCardId, actionId, ScoreCardActionStatus.PROCESSING, false);
						return new AuthorizationResult(sca,  Authorization.PROCESS);
					}
				}
			default:
				return new AuthorizationResult(sca, Authorization.WAIT);
		}
		
	}
	
	public ScoreCardAction updateActionStatus(String scoreCardId, String actionId, ScoreCardActionStatus status) {
		return updateActionStatusInternal(scoreCardId, actionId, status, true);
	}
	
	private ScoreCardAction updateActionStatusInternal(String scoreCardId, String actionId, ScoreCardActionStatus status, Boolean mustBeProcessing) {
		Optional<ScoreCard> sc = scoreCardRepository.findById(scoreCardId);
		if (!sc.isPresent()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.SCORE_CARD_DNE);
		}
		ScoreCard scoreCard = sc.get();		
		Optional<ScoreCardAction> a = scoreCard.getAction(actionId);
		
		if (!a.isPresent()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.SCORE_CARD_ACTION_DNE);
		}
		
		ScoreCardAction sca = a.get();
		
		if (mustBeProcessing && !ScoreCardActionStatus.PROCESSING.equals(sca.getStatus())) {
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
		
		Long completedActions = scoreCard.getActions().stream().filter(atn -> ScoreCardActionStatus.CANCELLED.equals(atn.getStatus()) || ScoreCardActionStatus.FAILED.equals(atn.getStatus()) || ScoreCardActionStatus.COMPLETED.equals(atn.getStatus())).count();
		if (completedActions == scoreCard.getActions().size()) {
			scoreCard.setEndTimestamp(LocalDateTime.now());
		}
		
		scoreCardRepository.save(scoreCard);
		return sca;
	}
	
}

package us.zacharymaddox.scorecard.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
}

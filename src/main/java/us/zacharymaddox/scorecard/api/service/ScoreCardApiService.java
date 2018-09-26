package us.zacharymaddox.scorecard.api.service;

import java.util.List;
import java.util.Map;

import us.zacharymaddox.scorecard.api.domain.Action;
import us.zacharymaddox.scorecard.api.domain.ScoreCard;
import us.zacharymaddox.scorecard.api.domain.ScoreCardHeader;
import us.zacharymaddox.scorecard.api.domain.Transaction;
import us.zacharymaddox.scorecard.domain.Authorization;
import us.zacharymaddox.scorecard.domain.ScoreCardActionStatus;
import us.zacharymaddox.scorecard.domain.ScoreCardId;

public interface ScoreCardApiService {
	
	public ScoreCardHeader convertHeader(String value);
	public Authorization authorize(String scoreCardHeader);
	public List<ScoreCard> getScoreCards(Integer rows, Integer page);
	public List<ScoreCard> getScoreCards(String transactionName, Integer rows, Integer page);
	public List<ScoreCard> getScoreCards(ScoreCardActionStatus status, String transactionName, Integer rows, Integer page);
	public void updateStatus(String scoreCardHeader, ScoreCardActionStatus status);
	public void updateStatus(ScoreCardHeader scoreCardHeader, ScoreCardActionStatus status);
	public void updateStatus(String scoreCardHeader, ScoreCardActionStatus status, Map<String, String> metadata);
	public void updateStatus(ScoreCardHeader scoreCardHeader, ScoreCardActionStatus status, Map<String, String> metadata);
	public ScoreCardId createScoreCard(Transaction transaction);
	public void wrapAndSend(ScoreCardId id, Transaction transaction, Action action, Object message);
	
}

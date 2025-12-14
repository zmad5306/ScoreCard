package dev.zachmaddox.scorecard.lib.service;

import dev.zachmaddox.scorecard.common.domain.Authorization;
import dev.zachmaddox.scorecard.common.domain.DataPage;
import dev.zachmaddox.scorecard.common.domain.ScoreCardActionStatus;
import dev.zachmaddox.scorecard.lib.domain.*;

import java.util.List;
import java.util.Map;

public interface ScoreCardApiService {
	
	ScoreCardHeader convertHeader(String value);
	Authorization authorize(String scoreCardHeader);
    Authorization authorize(ScoreCardHeader scoreCardHeader);
    DataPage<ScoreCard> getScoreCards(Integer rows, Integer page);
    DataPage<ScoreCard> getScoreCards(String transactionName, Integer rows, Integer page);
	List<ScoreCard> getScoreCards(ScoreCardActionStatus status, String transactionName, Integer rows, Integer page);
	void updateStatus(String scoreCardHeader, ScoreCardActionStatus status);
	void updateStatus(ScoreCardHeader scoreCardHeader, ScoreCardActionStatus status);
	void updateStatus(String scoreCardHeader, ScoreCardActionStatus status, Map<String, String> metadata);
	void updateStatus(ScoreCardHeader scoreCardHeader, ScoreCardActionStatus status, Map<String, String> metadata);
	ScoreCardId createScoreCard(Transaction transaction);
	void wrapAndSend(ScoreCardId id, Transaction transaction, Action action, Object message);
	ScoreCard getScoreCard(Long scoreCardId);
	
}

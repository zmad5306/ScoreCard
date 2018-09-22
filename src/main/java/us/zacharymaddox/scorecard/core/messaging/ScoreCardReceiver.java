package us.zacharymaddox.scorecard.core.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import us.zacharymaddox.scorecard.core.domain.exception.ScoreCardException;
import us.zacharymaddox.scorecard.core.service.ScoreCardService;
import us.zacharymaddox.scorecard.domain.CreateRequest;
import us.zacharymaddox.scorecard.domain.UpdateRequest;

@Component
public class ScoreCardReceiver {
	
	@Autowired
	private ScoreCardService scoreCardService;
	private Logger logger = LoggerFactory.getLogger(ScoreCardReceiver.class);
	
	@JmsListener(destination="scorecard", selector="ACTION='CREATE'", containerFactory="myFactory")
	@Transactional
	public void createScoreCard(CreateRequest request) {
		try {
			scoreCardService.createScoreCard(request.getScoreCardId(), request.getTransactionId());
		} catch (ScoreCardException e) {
			// TODO what should we do here?
			logger.error(e.getError().getMessage(), e);
		}
	}
	
	@JmsListener(destination="scorecard", selector="ACTION='UPDATE'", containerFactory="myFactory")
	@Transactional
	public void updateStatus(UpdateRequest request) {
		try {
			scoreCardService.updateActionStatus(request.getScoreCardId(), request.getActionId(), request.getStatus());
		} catch (ScoreCardException e) {
			// TODO what should we do here?
			logger.error(e.getError().getMessage(), e);
		}
	}

}

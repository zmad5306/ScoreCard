package us.zacharymaddox.scorecard.core.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import us.zacharymaddox.scorecard.core.service.ScoreCardService;
import us.zacharymaddox.scorecard.domain.CreateRequest;
import us.zacharymaddox.scorecard.domain.UpdateRequest;

@Component
public class ScoreCardReceiver {
	
	@Autowired
	private ScoreCardService scoreCardService;
	
	@JmsListener(destination="scorecard", selector="ACTION='CREATE'", containerFactory="myFactory")
	@Transactional
	public void createScoreCard(CreateRequest request) {
		scoreCardService.createScoreCard(request.getScoreCardId(), request.getTransactionId());
	}
	
	@JmsListener(destination="scorecard", selector="ACTION='UPDATE'", containerFactory="myFactory")
	@Transactional
	public void updateStatus(UpdateRequest request) {
		scoreCardService.updateActionStatus(request);
	}

}

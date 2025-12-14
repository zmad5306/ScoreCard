package dev.zachmaddox.scorecard.api.messaging;

import dev.zachmaddox.scorecard.common.domain.CreateRequest;
import dev.zachmaddox.scorecard.common.domain.UpdateRequest;
import dev.zachmaddox.scorecard.api.service.ScoreCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ScoreCardReceiver {
	
	private final ScoreCardService scoreCardService;
	
	@JmsListener(destination="scorecard", selector="ACTION='CREATE'")
	@Transactional
	public void createScoreCard(CreateRequest request) {
		scoreCardService.createScoreCard(request.scoreCardId(), request.transactionId());
	}
	
	@JmsListener(destination="scorecard", selector="ACTION='UPDATE'")
	@Transactional
	public void updateStatus(UpdateRequest request) {
		scoreCardService.updateActionStatus(request);
	}

}

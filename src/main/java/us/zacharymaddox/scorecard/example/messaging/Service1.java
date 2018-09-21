package us.zacharymaddox.scorecard.example.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import us.zacharymaddox.scorecard.api.domain.WaitException;
import us.zacharymaddox.scorecard.api.service.ScoreCardApiService;
import us.zacharymaddox.scorecard.common.domain.Authorization;
import us.zacharymaddox.scorecard.common.domain.ScoreCardActionStatus;

@Component
public class Service1 {
	
	@Autowired
	private ScoreCardApiService scoreCardApiService;
	private Logger logger = LoggerFactory.getLogger(Service1.class);
	
	@JmsListener(destination="service1", selector="ACTION='action1'", containerFactory="myFactory")
	@Transactional
	public void action1(@Header("SCORE_CARD") String scoreCardHeader) {
		logger.info("service1/action1 invoked");
		Authorization auth = scoreCardApiService.authorize(scoreCardHeader);
		switch (auth) {
			case CANCEL:
				break;
			case PROCESS:
				System.out.println("Processing message 1!");
				scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.COMPLETED);
				break;
			case SKIP:
				break;
			case WAIT:
				throw new WaitException();
		}
		
	}
	
	@JmsListener(destination="service1", selector="ACTION='action2'", containerFactory="myFactory")
	@Transactional
	public void action2(@Header("SCORE_CARD") String scoreCardHeader) {
		logger.info("service1/action2 invoked");
		Authorization auth = scoreCardApiService.authorize(scoreCardHeader);
		switch (auth) {
			case CANCEL:
				break;
			case PROCESS:
				System.out.println("Processing message 2!");
				scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.COMPLETED);
				break;
			case SKIP:
				break;
			case WAIT:
				throw new WaitException();
		}
		
	}
	
	@JmsListener(destination="service1", selector="ACTION='action3'", containerFactory="myFactory")
	@Transactional
	public void action3(@Header("SCORE_CARD") String scoreCardHeader) {
		logger.info("service1/action3 invoked");
		Authorization auth = scoreCardApiService.authorize(scoreCardHeader);
		switch (auth) {
			case CANCEL:
				break;
			case PROCESS:
				System.out.println("Processing message 3!");
				scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.COMPLETED);
				break;
			case SKIP:
				break;
			case WAIT:
				throw new WaitException();
		}
		
	}

}

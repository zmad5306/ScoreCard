package us.zacharymaddox.scorecard.example.messaging;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import us.zacharymaddox.scorecard.api.domain.WaitException;
import us.zacharymaddox.scorecard.api.service.ScoreCardApiService;
import us.zacharymaddox.scorecard.domain.Authorization;
import us.zacharymaddox.scorecard.domain.ScoreCardActionStatus;

@Component
@Profile({"example"})
public class Service1 {
	
	@Autowired
	private ScoreCardApiService scoreCardApiService;
	private Logger logger = LoggerFactory.getLogger(Service1.class);
	
	@JmsListener(destination="service1", selector="ACTION='action1'", containerFactory="myFactory")
	@Transactional
	public void action1(String message, @Header("SCORE_CARD") String scoreCardHeader) {
		logger.info("service1/action1 invoked");
		Authorization auth = scoreCardApiService.authorize(scoreCardHeader);
		
		switch (auth) {
			case CANCEL:
				scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.CANCELLED);
				break;
			case PROCESS:
				System.out.println("We have a message: " + message);
				Map<String, String> metadata = new HashMap<>();
				metadata.put("test", "data");
				scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.COMPLETED, metadata);
				break;
			case SKIP:
				break;
			case WAIT:
				throw new WaitException();
		}
	}
	
	@JmsListener(destination="service1", selector="ACTION='action2'", containerFactory="myFactory")
	@Transactional
	public void action2(String message, @Header("SCORE_CARD") String scoreCardHeader) {
		logger.info("service1/action2 invoked");
		Authorization auth = scoreCardApiService.authorize(scoreCardHeader);
		switch (auth) {
			case CANCEL:
				scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.CANCELLED);
				break;
			case PROCESS:
				System.out.println("We have a message: " + message);
				Map<String, String> metadata = new HashMap<>();
				metadata.put("test", "data");
				scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.COMPLETED, metadata);
				break;
			case SKIP:
				break;
			case WAIT:
				throw new WaitException();
		}
		
	}
	
	@JmsListener(destination="service1", selector="ACTION='action3'", containerFactory="myFactory")
	@Transactional
	public void action3(String message, @Header("SCORE_CARD") String scoreCardHeader) {
		logger.info("service1/action3 invoked");
		Authorization auth = scoreCardApiService.authorize(scoreCardHeader);
		switch (auth) {
			case CANCEL:
				scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.CANCELLED);
				break;
			case PROCESS:
				System.out.println("We have a message: " + message);
				Map<String, String> metadata = new HashMap<>();
				metadata.put("test", "data");
				scoreCardApiService.updateStatus(scoreCardHeader, ScoreCardActionStatus.COMPLETED, metadata);
				break;
			case SKIP:
				break;
			case WAIT:
				throw new WaitException();
		}
	}

}

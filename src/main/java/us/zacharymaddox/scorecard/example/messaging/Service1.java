package us.zacharymaddox.scorecard.example.messaging;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import us.zacharymaddox.scorecard.domain.Authorization;
import us.zacharymaddox.scorecard.domain.AuthorizationRequest;
import us.zacharymaddox.scorecard.domain.AuthorizationResult;
import us.zacharymaddox.scorecard.domain.ScoreCardActionStatus;
import us.zacharymaddox.scorecard.domain.UpdateRequest;
import us.zacharymaddox.scorecard.example.domain.WaitException;
import us.zacharymaddox.scorecard.example.service.MessageSelectorPostProcessor;
import us.zacharymaddox.scorecard.example.service.ScoreCardHeader;

@Component
public class Service1 {
	
	private Logger logger = LoggerFactory.getLogger(Service1.class);
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private ObjectMapper mapper;
	
	private ScoreCardHeader convertHeader(String value) {
		try {
			return mapper.readValue(value, ScoreCardHeader.class);
		} catch (IOException e) {
			throw new IllegalArgumentException("Could not parse Score Card Header.");
		}
	}
	
	private Authorization authorize(ScoreCardHeader scoreCardHeader) {
		AuthorizationRequest req = new AuthorizationRequest();
		req.setActionId(scoreCardHeader.getActionId());
		req.setScoreCardId(scoreCardHeader.getScoreCardId());
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<AuthorizationResult> result = restTemplate.postForEntity("http://localhost:8080/api/v1/scorecard", req, AuthorizationResult.class);
		AuthorizationResult aResult = result.getBody();
		return aResult.getAuthorization();
	}
	
	private void updateStatus(ScoreCardHeader scoreCardHeader, ScoreCardActionStatus status) {
		UpdateRequest request = new UpdateRequest(scoreCardHeader.getScoreCardId(), scoreCardHeader.getActionId(), status);
		jmsTemplate.convertAndSend("scorecard", request, new MessageSelectorPostProcessor("UPDATE"));
	}
	
	@JmsListener(destination="service1", selector="ACTION='action1'", containerFactory="myFactory")
	@Transactional
	public void action1(@Header("SCORE_CARD") String sc) {
		logger.info("service1/action1 invoked");
		ScoreCardHeader scoreCardHeader = convertHeader(sc);
		Authorization auth = authorize(scoreCardHeader);
		switch (auth) {
			case CANCEL:
				break;
			case PROCESS:
				System.out.println("Processing message 1!");
				updateStatus(scoreCardHeader, ScoreCardActionStatus.COMPLETED);
				break;
			case SKIP:
				break;
			case WAIT:
				throw new WaitException();
		}
		
	}
	
	@JmsListener(destination="service1", selector="ACTION='action2'", containerFactory="myFactory")
	@Transactional
	public void action2(@Header("SCORE_CARD") String sc) {
		logger.info("service1/action2 invoked");
		ScoreCardHeader scoreCardHeader = convertHeader(sc);
		Authorization auth = authorize(scoreCardHeader);
		switch (auth) {
			case CANCEL:
				break;
			case PROCESS:
				System.out.println("Processing message 2!");
				updateStatus(scoreCardHeader, ScoreCardActionStatus.COMPLETED);
				
				break;
			case SKIP:
				break;
			case WAIT:
				throw new WaitException();
		}
		
	}
	
	@JmsListener(destination="service1", selector="ACTION='action3'", containerFactory="myFactory")
	@Transactional
	public void action3(@Header("SCORE_CARD") String sc) {
		logger.info("service1/action3 invoked");
		ScoreCardHeader scoreCardHeader = convertHeader(sc);
		Authorization auth = authorize(scoreCardHeader);
		switch (auth) {
			case CANCEL:
				break;
			case PROCESS:
				System.out.println("Processing message 3!");
				updateStatus(scoreCardHeader, ScoreCardActionStatus.COMPLETED);
				break;
			case SKIP:
				break;
			case WAIT:
				throw new WaitException();
		}
		
	}

}

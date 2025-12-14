package dev.zachmaddox.scorecard.example.basic.service;

import java.util.HashMap;
import java.util.Map;

import dev.zachmaddox.scorecard.common.domain.Authorization;
import dev.zachmaddox.scorecard.common.domain.ScoreCardActionStatus;
import dev.zachmaddox.scorecard.lib.domain.WaitException;
import dev.zachmaddox.scorecard.lib.service.ScoreCardApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class BasicService {
	
	private final ScoreCardApiService scoreCardApiService;

    public BasicService(@Qualifier("scoreCardApiServiceJms") ScoreCardApiService scoreCardApiService) {
        this.scoreCardApiService = scoreCardApiService;
    }

    private void process(String message, @Header("SCORE_CARD") String scoreCardHeader) {
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

    @JmsListener(destination="service1", selector="ACTION='action1'")
	@Transactional
	public void action1(String message, @Header("SCORE_CARD") String scoreCardHeader) {
		log.info("service1/action1 invoked");
        process(message, scoreCardHeader);
    }

    @JmsListener(destination="service1", selector="ACTION='action2'")
	@Transactional
	public void action2(String message, @Header("SCORE_CARD") String scoreCardHeader) {
		log.info("service1/action2 invoked");
        process(message, scoreCardHeader);

    }
	
	@JmsListener(destination="service1", selector="ACTION='action3'")
	@Transactional
	public void action3(String message, @Header("SCORE_CARD") String scoreCardHeader) {
		log.info("service1/action3 invoked");
        process(message, scoreCardHeader);
    }

}

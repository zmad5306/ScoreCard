package dev.zachmaddox.scorecard.example.basic.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import dev.zachmaddox.scorecard.lib.annotation.ProcessAuthorized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class BasicService {


    private Optional<Map<String, String>> process(String message) {
        System.out.println("We have a message: " + message);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("test", "data");
        return Optional.of(metadata);
    }

    @JmsListener(destination="service1", selector="ACTION='action1'")
	@Transactional
    @ProcessAuthorized(allowMissingHeader = false)
	public Optional<Map<String, String>> action1(Message<String> message) {
		log.info("service1/action1 invoked");
        return process(message.getPayload());
    }

    @JmsListener(destination="service1", selector="ACTION='action2'")
	@Transactional
    @ProcessAuthorized(allowMissingHeader = false)
	public Optional<Map<String, String>> action2(Message<String> message) {
		log.info("service1/action2 invoked");
        return process(message.getPayload());

    }
	
	@JmsListener(destination="service1", selector="ACTION='action3'")
	@Transactional
    @ProcessAuthorized(allowMissingHeader = false)
	public Optional<Map<String, String>> action3(Message<String> message) {
		log.info("service1/action3 invoked");
        return process(message.getPayload());
    }

}

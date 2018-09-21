package us.zacharymaddox.scorecard.example.service;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.jms.core.MessagePostProcessor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ScoreCardPostProcessor implements MessagePostProcessor {

	private ObjectMapper mapper;
	private ScoreCardHeader scoreCardHeader;
	
	public ScoreCardPostProcessor(ScoreCardHeader scoreCardHeader, ObjectMapper mapper) {
		super();
		this.scoreCardHeader = scoreCardHeader;
		this.mapper = mapper;
	}

	@Override
	public Message postProcessMessage(Message message) throws JMSException {
		try {
			message.setStringProperty("SCORE_CARD", mapper.writeValueAsString(scoreCardHeader));
			message.setStringProperty("ACTION", scoreCardHeader.getPath());
		} catch (JsonProcessingException e) {
			throw new JMSException("Could not convert Score Card Header to JSON");
		}
		return message;
	}

	public ObjectMapper getMapper() {
		return mapper;
	}

	public ScoreCardHeader getScoreCardHeader() {
		return scoreCardHeader;
	}
	
}

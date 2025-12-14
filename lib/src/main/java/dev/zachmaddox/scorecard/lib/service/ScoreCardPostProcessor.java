package dev.zachmaddox.scorecard.lib.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zachmaddox.scorecard.lib.domain.ScoreCardHeader;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.jms.core.MessagePostProcessor;

@Getter
public class ScoreCardPostProcessor implements MessagePostProcessor {

	private final ObjectMapper mapper;
	private final ScoreCardHeader scoreCardHeader;
	
	public ScoreCardPostProcessor(ScoreCardHeader scoreCardHeader, ObjectMapper mapper) {
		super();
		this.scoreCardHeader = scoreCardHeader;
		this.mapper = mapper;
	}

	@Override
	public @NonNull Message postProcessMessage(@NotNull Message message) throws JMSException {
		try {
			message.setStringProperty("SCORE_CARD", mapper.writeValueAsString(scoreCardHeader));
			message.setStringProperty("ACTION", scoreCardHeader.getPath());
		} catch (JsonProcessingException e) {
			throw new JMSException("Could not convert Score Card Header to JSON");
		}
		return message;
	}

}

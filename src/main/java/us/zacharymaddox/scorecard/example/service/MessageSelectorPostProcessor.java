package us.zacharymaddox.scorecard.example.service;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.jms.core.MessagePostProcessor;

public class MessageSelectorPostProcessor implements MessagePostProcessor {

	private String action;
	
	public MessageSelectorPostProcessor(String action) {
		super();
		this.action = action;
	}

	@Override
	public Message postProcessMessage(Message message) throws JMSException {
		message.setStringProperty("ACTION", action);
		return message;
	}
	
	public String getAction() {
		return action;
	}

	

}

package dev.zachmaddox.scorecard.lib.service;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor
@Getter
public class MessageSelectorPostProcessor implements MessagePostProcessor {

    private final String action;

    @Override
    public @NonNull Message postProcessMessage(@NonNull Message message) throws JMSException {
        message.setStringProperty("ACTION", action);
        return message;
    }
}

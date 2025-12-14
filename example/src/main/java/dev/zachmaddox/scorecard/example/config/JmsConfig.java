package dev.zachmaddox.scorecard.example.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zachmaddox.scorecard.example.domain.QueuedMessage;
import dev.zachmaddox.scorecard.example.repository.QueuedMessageRepository;
import dev.zachmaddox.scorecard.lib.domain.WaitException;
import jakarta.jms.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.util.ErrorHandler;

import java.time.OffsetDateTime;

@Configuration
@EnableJms
@Slf4j
public class JmsConfig {

    @Bean
    public ErrorHandler jmsErrorHandler(QueuedMessageRepository queuedMessageRepository, ObjectMapper objectMapper) {
        return t -> {
            if (t.getCause() instanceof WaitException waitException) {
                QueuedMessage queuedMessage = new QueuedMessage();
                queuedMessage.setScoreCardHeader(objectMapper.convertValue(waitException.getScoreCardHeader(), new TypeReference<>() {}));
                queuedMessage.setMessageBody(objectMapper.convertValue(waitException.getMessageBody(), new TypeReference<>() {}));
                queuedMessage.setQueuedAt(OffsetDateTime.now());
                queuedMessage.setStatus(QueuedMessage.Status.PENDING);

                queuedMessage = queuedMessageRepository.save(queuedMessage);
                log.info("Queued JMS action due to WAIT: queuedMessageId={}", queuedMessage.getId());

                return; // swallow so container doesn’t rethrow/stop
            }
            log.error("JMS listener failed", t);
        };
    }

        @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory(
            @Value("${spring.activemq.broker-url}") String brokerUrl,
            @Value("${spring.activemq.user}") String username,
            @Value("${spring.activemq.password}") String password
    ) {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(brokerUrl);
        factory.setUserName(username);
        factory.setPassword(password);
        factory.setTrustAllPackages(true);
        return factory;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(@Qualifier("activeMQConnectionFactory") ConnectionFactory connectionFactory, MessageConverter messageConverter, ErrorHandler errorHandler) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setSessionTransacted(true);
        factory.setMessageConverter(messageConverter);
        factory.setErrorHandler(errorHandler);
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate(@Qualifier("activeMQConnectionFactory") ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        JmsTemplate template = new JmsTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}

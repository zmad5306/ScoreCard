package dev.zachmaddox.scorecard.example.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zachmaddox.scorecard.example.domain.QueuedMessage;
import dev.zachmaddox.scorecard.example.repository.QueuedMessageRepository;
import dev.zachmaddox.scorecard.lib.domain.WaitException;
import java.time.OffsetDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpErrorHandler {

    private final QueuedMessageRepository queuedMessageRepository;
    private final ObjectMapper objectMapper;

    @ExceptionHandler(WaitException.class)
    public ResponseEntity<Map<String, Object>> handleWait(WaitException ex) {
        QueuedMessage queuedMessage = new QueuedMessage();
        queuedMessage.setScoreCardHeader(objectMapper.convertValue(ex.getScoreCardHeader(), new TypeReference<>() {}));
        queuedMessage.setMessageBody(objectMapper.convertValue(ex.getMessageBody(), new TypeReference<>() {}));
        queuedMessage.setQueuedAt(OffsetDateTime.now());
        queuedMessage.setStatus(QueuedMessage.Status.PENDING);

        queuedMessage = queuedMessageRepository.save(queuedMessage);
        log.info("Queued HTTP action due to WAIT: queuedMessageId={}", queuedMessage.getId());

        return ResponseEntity.accepted()
                .body(Map.of(
                        "status", "queued",
                        "queued_message_id", queuedMessage.getId()
                ));
    }
}

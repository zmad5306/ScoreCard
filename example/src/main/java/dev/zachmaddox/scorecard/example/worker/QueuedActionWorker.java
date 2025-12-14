package dev.zachmaddox.scorecard.example.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zachmaddox.scorecard.common.domain.ScoreCardActionStatus;
import dev.zachmaddox.scorecard.common.domain.Transport;
import dev.zachmaddox.scorecard.example.domain.QueuedMessage;
import dev.zachmaddox.scorecard.example.repository.QueuedMessageRepository;
import dev.zachmaddox.scorecard.lib.domain.*;
import dev.zachmaddox.scorecard.lib.service.ActionApiService;
import dev.zachmaddox.scorecard.lib.service.ScoreCardApiService;
import dev.zachmaddox.scorecard.lib.service.TransactionApiService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class QueuedActionWorker {

    private final QueuedMessageRepository queuedMessageRepository;
    private final ScoreCardApiService jmsScoreCardApiService;
    private final ScoreCardApiService httpScoreCardApiService;
    private final TransactionApiService transactionApiService;
    private final ActionApiService actionApiService;
    private final ObjectMapper objectMapper;

    public QueuedActionWorker(
            QueuedMessageRepository queuedMessageRepository,
            @Qualifier("scoreCardApiServiceJms") ScoreCardApiService jmsScoreCardApiService,
            @Qualifier("scoreCardApiServiceHttp") ScoreCardApiService httpScoreCardApiService,
            TransactionApiService transactionApiService,
            ActionApiService actionApiService,
            ObjectMapper objectMapper) {
        this.queuedMessageRepository = queuedMessageRepository;
        this.jmsScoreCardApiService = jmsScoreCardApiService;
        this.httpScoreCardApiService = httpScoreCardApiService;
        this.transactionApiService = transactionApiService;
        this.actionApiService = actionApiService;
        this.objectMapper = objectMapper;
    }

    private void resubmit(QueuedMessage queuedMessage) {
        Map<String,Object> header = queuedMessage.getScoreCardHeader();

        if (header.containsKey("score_card_id")) {
            ScoreCardHeader scoreCardHeader = objectMapper.convertValue(queuedMessage.getScoreCardHeader(), ScoreCardHeader.class);
            ScoreCard scoreCard = httpScoreCardApiService.getScoreCard(scoreCardHeader.getScoreCardId());
            Transaction transaction = transactionApiService.getTransaction(scoreCard.getTransactionId());
            ScoreCardId scoreCardId = new ScoreCardId(scoreCardHeader.getScoreCardId());

            scoreCard.getActions().stream()
                    .filter(scoreCardAction -> ScoreCardActionStatus.PENDING.equals(scoreCardAction.getStatus()) && scoreCardHeader.getActionId().equals(scoreCardAction.getActionId()))
                    .forEach(scoreCardAction -> {
                        Action action = actionApiService.getAction(scoreCardAction.getActionId());
                        Service service = action.getService();
                        if (Transport.QUEUE.equals(service.getTransport())) {
                            jmsScoreCardApiService.wrapAndSend(scoreCardId, transaction, action, queuedMessage.getMessageBody());
                        } else {
                            httpScoreCardApiService.wrapAndSend(scoreCardId, transaction, action, queuedMessage.getMessageBody());
                        }
            });
        }

        throw new IllegalStateException(String.format("Queued Message %s does not contain valid Score Card metadata.", queuedMessage.getId()));
    }

    @Scheduled(fixedDelay = 10000L)
    public void resubmit() {
        Page<QueuedMessage> messages = queuedMessageRepository.findPending(Pageable.ofSize(100));
        messages.getContent().forEach(queuedMessage -> {
            try {
                resubmit(queuedMessage);
                queuedMessage.setStatus(QueuedMessage.Status.RESUBMITTED);
            } catch (Exception e) {
                queuedMessage.setStatus(QueuedMessage.Status.ERROR);
                queuedMessage.setError(e.getMessage());
            } finally {
                queuedMessageRepository.save(queuedMessage);
            }
        });
    }

    // TODO clean up old QueuedMessages

}

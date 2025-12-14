package dev.zachmaddox.scorecard.example.httpdemo.web;

import dev.zachmaddox.scorecard.example.httpdemo.domain.OrderRequest;
import dev.zachmaddox.scorecard.lib.annotation.ProcessAuthorized;
import dev.zachmaddox.scorecard.lib.domain.Action;
import dev.zachmaddox.scorecard.lib.domain.ScoreCardId;
import dev.zachmaddox.scorecard.lib.domain.Transaction;
import dev.zachmaddox.scorecard.lib.service.ScoreCardApiService;
import dev.zachmaddox.scorecard.lib.service.TransactionApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/app/example/store/order")
@Slf4j
public class OrderController {
	
	private final ScoreCardApiService scoreCardApiService;
    private final TransactionApiService transactionApiService;

    public OrderController(@Qualifier("scoreCardApiServiceHttp") ScoreCardApiService scoreCardApiService, TransactionApiService transactionApiService) {
        this.scoreCardApiService = scoreCardApiService;
        this.transactionApiService = transactionApiService;
    }

    @GetMapping(produces="application/json")
	public ScoreCardId place(@RequestParam(value="order_id") Long orderId) {
        String transactionName = "http-order";
        Transaction transaction = transactionApiService.getTransactionByName(transactionName);
        Action validateAction = transaction.getAction("validate-order");
        Action finalizeAction = transaction.getAction("finalize-order");

        OrderRequest orderRequest = new OrderRequest(orderId);
        
        ScoreCardId id = scoreCardApiService.createScoreCard(transaction);

        scoreCardApiService.wrapAndSend(id, transaction, validateAction, orderRequest);
        scoreCardApiService.wrapAndSend(id, transaction, finalizeAction, orderRequest);

        return id;
	}

    @PostMapping(value="/validate", produces="application/json")
    @ProcessAuthorized(allowMissingHeader = false)
    public Map<String, String> validate(OrderRequest orderRequest) {
        log.info("Validating order: {}", orderRequest.getOrderId());
        return Map.of("validated_at", LocalDateTime.now().toString());
    }

    @PostMapping(value="/finalize", produces="application/json")
    @ProcessAuthorized(allowMissingHeader = false)
    public Map<String, String> finalize(OrderRequest orderRequest) {
        log.info("Finalizing order: {}", orderRequest.getOrderId());
        return Map.of("finalized_at", LocalDateTime.now().toString());
    }

}

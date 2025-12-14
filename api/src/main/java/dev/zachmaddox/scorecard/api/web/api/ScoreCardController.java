package dev.zachmaddox.scorecard.api.web.api;

import java.util.List;

import dev.zachmaddox.scorecard.api.domain.ScoreCard;
import dev.zachmaddox.scorecard.api.domain.ScoreCardAction;
import dev.zachmaddox.scorecard.api.domain.ScoreCardId;
import dev.zachmaddox.scorecard.api.service.ScoreCardService;
import dev.zachmaddox.scorecard.common.domain.AuthorizationRequest;
import dev.zachmaddox.scorecard.common.domain.AuthorizationResult;
import dev.zachmaddox.scorecard.common.domain.CreateRequest;
import dev.zachmaddox.scorecard.common.domain.DataPage;
import dev.zachmaddox.scorecard.common.domain.ScoreCardActionStatus;
import dev.zachmaddox.scorecard.common.domain.UpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/scorecard")
@RequiredArgsConstructor
@Tag(name = "Score Card", description = "Endpoints for score card lifecycle and state")
public class ScoreCardController {

	private final ScoreCardService scoreCardService;

	@GetMapping(produces="application/json")
	@Operation(summary = "List score cards", description = "Paged list of score cards", responses = {
			@ApiResponse(responseCode = "200", description = "Score cards returned", content = @Content(schema = @Schema(implementation = DataPage.class)))
	})
	public DataPage<ScoreCard> getScoreCards(
			@RequestParam(name="rows", required=false, defaultValue="100") Integer rows,
			@RequestParam(name="page", required=false, defaultValue="1") Integer page 
		) {
		return new DataPage<>(
                scoreCardService.getScoreCards(page, rows),
                rows,
                page,
                scoreCardService.countAll()
        );
	}
	
	@GetMapping(value="/filter", produces="application/json")
	@Operation(summary = "Filter score cards by transaction name", responses = {
			@ApiResponse(responseCode = "200", description = "Score cards returned", content = @Content(schema = @Schema(implementation = DataPage.class)))
	})
	public DataPage<ScoreCard> getScoreCardsFilterByTransactionName(
			@RequestParam(name="transaction_name") String transactionName,
			@RequestParam(name="rows", required=false, defaultValue="100") Integer rows,
			@RequestParam(name="page", required	=false, defaultValue="1") Integer page
		) {
		return new DataPage<>(
                scoreCardService.getScoreCards(transactionName, page, rows),
                rows,
                page,
                scoreCardService.countByTransactionName(transactionName)
        );
	}
	
	@GetMapping(value="/status", produces="application/json")
	@Operation(summary = "Filter score cards by transaction name and action status", responses = {
			@ApiResponse(responseCode = "200", description = "Score cards returned", content = @Content(schema = @Schema(implementation = ScoreCard.class)))
	})
	public List<ScoreCard> getScoreCardsFilterByTransactionNameAndActionStatus(
			@RequestParam(name="score_card_action_status") ScoreCardActionStatus status,
			@RequestParam(name="transaction_name") String transactionName,
			@RequestParam(name="rows", required=false, defaultValue="100") Integer rows,
			@RequestParam(name="page", required		=false, defaultValue="1") Integer page
		) {
		return scoreCardService.getScoreCards(status, transactionName, page, rows);
	}
	
	@GetMapping(value="/id", produces="application/json")
	@Operation(summary = "Reserve the next score card id", responses = {
			@ApiResponse(responseCode = "200", description = "Next score card id", content = @Content(schema = @Schema(implementation = ScoreCardId.class)))
	})
	public ScoreCardId getScoreCardId() {
		return scoreCardService.getNextScoreCardId();
	}
	
	@GetMapping(value="/{score_card_id}", produces="application/json")
	@Operation(summary = "Get score card by id", responses = {
			@ApiResponse(responseCode = "200", description = "Score card found", content = @Content(schema = @Schema(implementation = ScoreCard.class))),
			@ApiResponse(responseCode = "404", description = "Score card not found", content = @Content)
	})
	public ScoreCard getScoreCard(@PathVariable("score_card_id") Long scoreCardId) {
		return scoreCardService.getScoreCard(scoreCardId);
	}

	@PutMapping(consumes="application/json", produces="application/json")
	@Operation(summary = "Create a score card", responses = {
			@ApiResponse(responseCode = "200", description = "Score card created", content = @Content(schema = @Schema(implementation = ScoreCard.class))),
			@ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
	})
	public ScoreCard createScoreCard(@RequestBody CreateRequest request) {
		return scoreCardService.createScoreCard(request.scoreCardId(), request.transactionId());
	}
	
	@PostMapping(consumes="application/json", produces="application/json")
	@Operation(summary = "Authorize an action", responses = {
			@ApiResponse(responseCode = "200", description = "Authorization result", content = @Content(schema = @Schema(implementation = AuthorizationResult.class))),
			@ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
	})
	public AuthorizationResult authorize(@RequestBody AuthorizationRequest request) {
		return scoreCardService.authorize(request.scoreCardId(), request.actionId());
	}
	
	@PostMapping(value="/{score_card_id}", consumes="application/json", produces="application/json")
	@Operation(summary = "Update action status", responses = {
			@ApiResponse(responseCode = "204", description = "Status updated"),
			@ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
	})
	public void updateActionStatus(@RequestBody UpdateRequest request, @PathVariable("score_card_id") Long scoreCardId) {
        assert scoreCardId != null;
        assert scoreCardId.equals(request.getScoreCardId());
		scoreCardService.updateActionStatus(request);
	}
	
}

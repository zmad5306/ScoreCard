package us.zacharymaddox.scorecard.core.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import us.zacharymaddox.scorecard.common.domain.AuthorizationRequest;
import us.zacharymaddox.scorecard.common.domain.AuthorizationResult;
import us.zacharymaddox.scorecard.common.domain.CreateRequest;
import us.zacharymaddox.scorecard.common.domain.ScoreCardId;
import us.zacharymaddox.scorecard.common.domain.UpdateRequest;
import us.zacharymaddox.scorecard.core.domain.ScoreCard;
import us.zacharymaddox.scorecard.core.service.ScoreCardService;

@RestController
@RequestMapping("/scorecard")
public class ScoreCardController {

	@Autowired
	private ScoreCardService scoreCardService;
	
	@GetMapping(produces="application/json")
	public ScoreCardId getScoreCardId() {
		return scoreCardService.getNextScoreCardId();
	}
	
	@PutMapping(consumes="application/json", produces="application/json")
	public ScoreCard createScoreCard(@RequestBody CreateRequest request) {
		return scoreCardService.createScoreCard(request.getScoreCardId(), request.getTransactionId());
	}
	
	@GetMapping(value="/{score_card_id}", produces="application/json")
	public ScoreCard getScoreCard(@PathVariable("score_card_id") Long scoreCardId) {
		return scoreCardService.getScoreCard(scoreCardId);
	}
	
	@PostMapping(consumes="application/json", produces="application/json")
	public AuthorizationResult authorize(@RequestBody AuthorizationRequest request) {
		return scoreCardService.authorize(request.getScoreCardId(), request.getActionId());
	}
	
	@PatchMapping(consumes="application/json", produces="application/json")
	public void updateActionStatus(@RequestBody UpdateRequest request) {
		scoreCardService.updateActionStatus(request.getScoreCardId(), request.getActionId(), request.getStatus());
	}
	
}

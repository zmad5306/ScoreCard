package us.zacharymaddox.scorecard.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import us.zacharymaddox.scorecard.domain.ScoreCard;
import us.zacharymaddox.scorecard.service.ScoreCardService;

@RestController
@RequestMapping("/scorecard")
public class ScoreCardController {

	@Autowired
	private ScoreCardService scoreCardService;
	
	@PutMapping(consumes="multipart/form-data", produces="application/json")
	public ScoreCard createScoreCard(@RequestParam("transaction_id") String transactionId) {
		return scoreCardService.createScoreCard(transactionId);
	}
	
	@GetMapping(value="/{score_card_id}", produces="application/json")
	public ScoreCard getScoreCard(@PathVariable("score_card_id") String scoreCardId) {
		return scoreCardService.getScoreCard(scoreCardId);
	}
	
}

package us.zacharymaddox.scorecard.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import us.zacharymaddox.scorecard.domain.ScoreCard;
import us.zacharymaddox.scorecard.service.ScoreCardService;

@RestController
@RequestMapping("/scorecard")
public class ScoreCardController {

	@Autowired
	private ScoreCardService scoreCardService;
	
	@PutMapping(consumes="application/json")
	public ScoreCard createScoreCard() {
		return scoreCardService.createScoreCard();
	}
	
}

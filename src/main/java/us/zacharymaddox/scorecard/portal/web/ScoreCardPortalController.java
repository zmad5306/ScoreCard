package us.zacharymaddox.scorecard.portal.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import us.zacharymaddox.scorecard.api.service.ScoreCardApiService;
import us.zacharymaddox.scorecard.domain.ScoreCardStatus;

@Controller
@RequestMapping("/portal/scorecard")
public class ScoreCardPortalController {
	
	@Autowired
	private ScoreCardApiService scoreCardApiService;
	
	@GetMapping("/list")
	public String list(
			@RequestParam(name="score_card_status", required=false, defaultValue="COMPLETED") ScoreCardStatus scoreCardStatus,
			@RequestParam(name="rows", required=false, defaultValue="10") Integer rows,
			@RequestParam(name="page", required=false, defaultValue="0") Integer page,
			Model model
		) {
		model.addAttribute("scorecards", scoreCardApiService.getScoreCards(scoreCardStatus, rows, page));
		return "portal/scorecard/list";
	}

}

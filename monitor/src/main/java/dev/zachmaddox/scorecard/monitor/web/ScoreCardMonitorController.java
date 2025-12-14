package dev.zachmaddox.scorecard.monitor.web;

import dev.zachmaddox.scorecard.lib.service.ScoreCardApiService;
import dev.zachmaddox.scorecard.lib.service.TransactionApiService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/scorecard")
public class ScoreCardMonitorController {
	
	private final ScoreCardApiService scoreCardApiService;
	private final TransactionApiService transactionApiService;

    public ScoreCardMonitorController(@Qualifier("scoreCardApiServiceJms") ScoreCardApiService scoreCardApiService, TransactionApiService transactionApiService) {
        this.scoreCardApiService = scoreCardApiService;
        this.transactionApiService = transactionApiService;
    }

    @GetMapping("/list")
	public String list(
			@RequestParam(name="transaction_name", required=false) String transactionName,
			@RequestParam(name="rows", required=false, defaultValue="10") Integer rows,
			@RequestParam(name="page", required=false, defaultValue="0") Integer page,
			Model model
		) {
		
		if (null == transactionName) {
			model.addAttribute("scorecards", scoreCardApiService.getScoreCards(rows, page));
		} else {
			model.addAttribute("scorecards", scoreCardApiService.getScoreCards(transactionName, rows, page));
			model.addAttribute("transactionName", transactionName);
		}
		
		
		model.addAttribute("transactions", transactionApiService.getTransactions());
		return "scorecard/list";
	}
	
	@GetMapping("/{score_card_id}")
	public String details(@PathVariable("score_card_id") Long scoreCardId, Model model) {
		model.addAttribute("scorecard", scoreCardApiService.getScoreCard(scoreCardId));
		return "scorecard/detail";
	}

}

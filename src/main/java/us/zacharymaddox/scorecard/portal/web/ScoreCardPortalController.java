package us.zacharymaddox.scorecard.portal.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import us.zacharymaddox.scorecard.api.service.ScoreCardApiService;
import us.zacharymaddox.scorecard.api.service.TransactionApiService;

@Controller
@RequestMapping("/portal/scorecard")
public class ScoreCardPortalController {
	
	@Autowired
	private ScoreCardApiService scoreCardApiService;
	
	@Autowired TransactionApiService transactionApiService;
	
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
		return "portal/scorecard/list";
	}

}

package us.zacharymaddox.scorecard.portal.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import us.zacharymaddox.scorecard.api.service.TransactionApiService;

@Controller
@RequestMapping("/portal/transaction")
@Profile({"portal"})
public class TransactionPortalController {
	
	@Autowired
	TransactionApiService transactionApiService;
	
	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("transactions", transactionApiService.getTransactions());
		return "portal/transaction/list";
	}

}

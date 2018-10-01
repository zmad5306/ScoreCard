package us.zacharymaddox.scorecard.portal.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import us.zacharymaddox.scorecard.api.service.TransactionApiService;

@Controller
@RequestMapping("/portal/transaction")
@Profile({"portal"})
public class TransactionPortalController {
	
	@Autowired
	private TransactionApiService transactionApiService;
	
	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("transactions", transactionApiService.getTransactions());
		return "portal/transaction/list";
	}
	
	@GetMapping("/{transaction_id}")
	public String details(
			@RequestParam(value="successful_add", required=false, defaultValue="false") Boolean successfulAdd, 
			@PathVariable("transaction_id") Long transactionId, 
			Model model
		) {
		model.addAttribute("transaction", transactionApiService.getTransaction(transactionId));
		model.addAttribute("successfulAdd", successfulAdd);
		return "portal/transaction/detail";
	}

}

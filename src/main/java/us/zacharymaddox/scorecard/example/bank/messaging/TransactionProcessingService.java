package us.zacharymaddox.scorecard.example.bank.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import us.zacharymaddox.scorecard.api.service.ScoreCardApiService;
import us.zacharymaddox.scorecard.domain.Authorization;
import us.zacharymaddox.scorecard.example.bank.domain.CreditRequest;
import us.zacharymaddox.scorecard.example.bank.domain.DebitRequest;

@Component
@Profile({"test-app"})
public class TransactionProcessingService {
	
	@Autowired
	private ScoreCardApiService scoreCardApiService;
	private Logger logger = LoggerFactory.getLogger(TransactionProcessingService.class);
	
	@JmsListener(destination="account", selector="ACTION='debit'", containerFactory="myFactory")
	@Transactional
	public void debit(CreditRequest request, @Header("SCORE_CARD") String scoreCardHeader) {
		logger.info("processing credit reqeust");
		Authorization auth = scoreCardApiService.authorize(scoreCardHeader);
//		logic here
//		process(auth, message, scoreCardHeader);
	}
	
	@JmsListener(destination="account", selector="ACTION='credit'", containerFactory="myFactory")
	@Transactional
	public void credit(DebitRequest debitRequest, @Header("SCORE_CARD") String scoreCardHeader) {
		logger.info("processing debit reqeust");
		Authorization auth = scoreCardApiService.authorize(scoreCardHeader);
//		logic here
//		process(auth, message, scoreCardHeader);
		
	}

}

package us.zacharymaddox.scorecard.core.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import us.zacharymaddox.scorecard.core.domain.Action;
import us.zacharymaddox.scorecard.core.repository.ActionRepository;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardClientException;
import us.zacharymaddox.scorecard.domain.exception.ScoreCardErrorCode;

@Service
public class ActionService {

	@Autowired
	private ActionRepository actionRepository;
	
	@Transactional(readOnly=true)
	public Action getAction(Long actionId) {
		Optional<Action> t = actionRepository.findById(actionId);
		if (!t.isPresent()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.TRANSACTION_DNE);
		}
		return t.get();
	}
	
	@Transactional(readOnly=true)
	public Action getActionByName(String name) {
		Optional<Action> t = actionRepository.findByName(name);
		if (!t.isPresent()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.ACTION_DNE);
		}
		
		return t.get();
	}
	
}

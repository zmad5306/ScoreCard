package us.zacharymaddox.scorecard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import us.zacharymaddox.scorecard.domain.ScoreCard;
import us.zacharymaddox.scorecard.repository.ScoreCardRepository;

@Service
public class ScoreCardService {

	@Autowired
	private ScoreCardRepository scoreCardRepository;
	
	public ScoreCard createScoreCard() {
		ScoreCard sc = new ScoreCard();
		return scoreCardRepository.save(sc);
	}
}

package us.zacharymaddox.scorecard.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import us.zacharymaddox.scorecard.domain.ScoreCard;

public interface ScoreCardRepository extends MongoRepository<ScoreCard, String> {

}

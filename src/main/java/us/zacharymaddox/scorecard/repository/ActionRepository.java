package us.zacharymaddox.scorecard.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import us.zacharymaddox.scorecard.domain.Action;

public interface ActionRepository extends MongoRepository<Action, String> {

}

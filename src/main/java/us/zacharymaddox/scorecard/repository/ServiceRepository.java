package us.zacharymaddox.scorecard.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import us.zacharymaddox.scorecard.domain.Service;

public interface ServiceRepository extends MongoRepository<Service, String> {

}

package us.zacharymaddox.scorecard.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import us.zacharymaddox.scorecard.domain.Transaction;

public interface TransactionRepository extends MongoRepository<Transaction	, String> {

}

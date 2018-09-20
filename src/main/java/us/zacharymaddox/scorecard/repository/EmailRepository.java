package us.zacharymaddox.scorecard.repository;

import us.zacharymaddox.scorecard.domain.Email;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailRepository extends MongoRepository<Email, String> {

}

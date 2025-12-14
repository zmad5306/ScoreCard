package dev.zachmaddox.scorecard.api.repository;

import java.util.Optional;

import dev.zachmaddox.scorecard.api.domain.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {

	Optional<Service> findByName(String name);

}

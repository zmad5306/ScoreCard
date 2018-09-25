package us.zacharymaddox.scorecard.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import us.zacharymaddox.scorecard.core.domain.Service;

public interface ServiceRepository extends JpaRepository<Service, Long> {

	public Optional<Service> findByName(String name);

}

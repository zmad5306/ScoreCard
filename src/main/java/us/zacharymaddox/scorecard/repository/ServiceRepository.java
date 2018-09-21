package us.zacharymaddox.scorecard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import us.zacharymaddox.scorecard.domain.Service;

public interface ServiceRepository extends JpaRepository<Service, Long> {

}

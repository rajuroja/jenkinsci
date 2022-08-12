package com.dpworld.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.CaseRegistrationDetails;

@Repository
public interface CaseRegistrationRepository extends CrudRepository<CaseRegistrationDetails, Long> {
	
}

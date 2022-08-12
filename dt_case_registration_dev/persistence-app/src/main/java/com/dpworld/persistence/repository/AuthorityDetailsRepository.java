package com.dpworld.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.AuthorityDetails;

@Repository
public interface AuthorityDetailsRepository extends CrudRepository<AuthorityDetails, Integer> {

	AuthorityDetails findByShortName(String shortName);
}

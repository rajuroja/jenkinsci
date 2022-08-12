package com.dpworld.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.Authority;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Integer> {

	Authority findByAuthorityId(Integer authorityid);

	Authority findByAuthorityShortName(String name);
}

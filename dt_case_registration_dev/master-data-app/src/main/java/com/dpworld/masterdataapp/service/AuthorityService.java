package com.dpworld.masterdataapp.service;

import java.util.Set;

import com.dpworld.persistence.entity.Authority;

public interface AuthorityService {

	Authority getByAuthorityId(Integer authorityid);

	void createOrUpdateAuthorities(Set<String> authorities);

	Authority findByAuthorityShortName(String string);
}

package com.dpworld.masterdataapp.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dpworld.masterdataapp.service.AuthorityService;
import com.dpworld.persistence.entity.Authority;
import com.dpworld.persistence.entity.AuthorityDetails;
import com.dpworld.persistence.repository.AuthorityDetailsRepository;
import com.dpworld.persistence.repository.AuthorityRepository;

@Service
public class AuthorityServiceImpl implements AuthorityService {

	@Autowired
	private AuthorityRepository authorityRepository;
	
	@Autowired
	private AuthorityDetailsRepository authorityDetailsRepository;

	@Override
	public Authority getByAuthorityId(Integer authorityId) {
		return authorityRepository.findByAuthorityId(authorityId);
	}

	@Override
	public void createOrUpdateAuthorities(Set<String> shortNames) {
		for (String shortName : shortNames) {
			Authority authority = authorityRepository.findByAuthorityShortName(shortName);
			if (authority == null) {
				AuthorityDetails authorityDetails = authorityDetailsRepository.findByShortName(shortName);
				authority = new Authority();
				authority.setAuthorityName(authorityDetails != null ? authorityDetails.getFullName() : shortName);
				authority.setAuthorityShortName(shortName);
				authorityRepository.save(authority);
			}
		}
	}

	@Override
	public Authority findByAuthorityShortName(String string) {
		return authorityRepository.findByAuthorityShortName(string);
	}

}

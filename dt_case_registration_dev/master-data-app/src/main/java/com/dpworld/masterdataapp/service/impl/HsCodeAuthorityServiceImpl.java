package com.dpworld.masterdataapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dpworld.masterdataapp.service.HsCodeAuthorityService;
import com.dpworld.persistence.entity.Authority;
import com.dpworld.persistence.entity.HsCode;
import com.dpworld.persistence.entity.HsCodeAuthority;
import com.dpworld.persistence.repository.HsCodeAuthorityRepository;

@Service
public class HsCodeAuthorityServiceImpl implements HsCodeAuthorityService {

	@Autowired
	private HsCodeAuthorityRepository hsCodeAuthorityRepository;
	
	@Override
	public HsCodeAuthority findByHsCodeAndAuthority(HsCode hsCode, Authority authority) {
		return hsCodeAuthorityRepository.findByHsCodeAndAuthority(hsCode, authority);
	}

	@Override
	public void save(HsCodeAuthority hsCodeAuthority) {
		hsCodeAuthorityRepository.save(hsCodeAuthority);
	}

}

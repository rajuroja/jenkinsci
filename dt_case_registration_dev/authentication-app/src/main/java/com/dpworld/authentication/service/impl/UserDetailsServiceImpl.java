package com.dpworld.authentication.service.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.dpworld.authentication.model.AuthenticationRequest;
import com.dpworld.authentication.service.UserDetailsService;
import com.dpworld.persistence.entity.UserDetails;
import com.dpworld.persistence.repository.UserDetailsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private static Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private ObjectMapper objectMapper;

	private UserDetails addUserDetails(AuthenticationRequest authenticationRequest) {

		UserDetails convertedValue = objectMapper.convertValue(authenticationRequest, UserDetails.class);
		return userDetailsRepository.save(convertedValue);
	}

	private UserDetails getUserDetail(String username, String companyId, String licenseNumber) {

		UserDetails userDetail = userDetailsRepository.findByUsernameAndCompanyIdAndLicenseNumber(username, companyId, licenseNumber);
		return userDetail;
	}

	@Override
	public UserDetails addOrFetchUserDetail(AuthenticationRequest authenticationRequest) {

		//logger.info("ENTRY:: addOrFetchUserDetail:: Add Userdetails if not available. Username={}", authenticationRequest.getUsername());
		try {

			authenticationRequest.setUsername(authenticationRequest.getUsername().trim());
			authenticationRequest.setCompanyId(authenticationRequest.getCompanyId().trim());
			authenticationRequest.setLicenseNumber(authenticationRequest.getLicenseNumber().trim());
			authenticationRequest.setLastLoginTime(LocalDateTime.now());

			UserDetails userDetail = getUserDetail(authenticationRequest.getUsername(), authenticationRequest.getCompanyId(), authenticationRequest.getLicenseNumber());
			if (userDetail != null) {
				UserDetails copy = new UserDetails(userDetail.getUsername(), userDetail.getCompanyId(), userDetail.getLicenseNumber(), userDetail.getLastLoginTime());
				userDetail.setLastLoginTime(LocalDateTime.now());
				userDetailsRepository.save(userDetail);
				//logger.info("EXIT:: addOrFetchUserDetail:: User fetched successfully. Username={}", authenticationRequest.getUsername());
				return copy;
			}

			UserDetails addUserDetails = addUserDetails(authenticationRequest);
			//logger.info("EXIT:: addOrFetchUserDetail:: User added successfully. Username={}", authenticationRequest.getUsername());
			return addUserDetails;
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException("ERROR:: Error occured on save Userdetail." + e.getMessage());
		}
	}

}

package com.dpworld.masterdataapp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dpworld.masterdataapp.service.CountryDetailsService;
import com.dpworld.persistence.entity.CountryDetails;
import com.dpworld.persistence.repository.CountryDetailsRepository;

@Service
public class CountryDetailsServiceImpl implements CountryDetailsService {

	@Autowired
	private CountryDetailsRepository countryDetailsRepository;

	@Override
	public CountryDetails getByCountryId(Integer countryId) {
		return countryDetailsRepository.findByCountryId(countryId);
	}

	@Override
	public List<CountryDetails> getAll() {
		return countryDetailsRepository.findAll();
	}

}

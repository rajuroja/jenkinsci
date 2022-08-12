package com.dpworld.masterdataapp.service;

import java.util.List;

import com.dpworld.persistence.entity.CountryDetails;

public interface CountryDetailsService {

	CountryDetails getByCountryId(Integer countryId);
	
	List<CountryDetails> getAll();
}

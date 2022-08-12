package com.dpworld.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.CountryDetails;

@Repository
public interface CountryDetailsRepository extends CrudRepository<CountryDetails, Integer> {

	CountryDetails findByCountryId(Integer countryId);

	List<CountryDetails> findAll();
}

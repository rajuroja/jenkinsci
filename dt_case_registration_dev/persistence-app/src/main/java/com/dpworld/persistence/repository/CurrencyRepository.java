package com.dpworld.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.CurrencyDetails;

@Repository
public interface CurrencyRepository extends CrudRepository<CurrencyDetails, Integer> {

	CurrencyDetails findByCurrencyId(Integer currencyId);

	List<CurrencyDetails> findByCurrencyName(String currencyName);

	List<CurrencyDetails> findAll();

}

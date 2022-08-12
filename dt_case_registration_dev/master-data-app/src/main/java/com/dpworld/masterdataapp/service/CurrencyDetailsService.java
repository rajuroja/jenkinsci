package com.dpworld.masterdataapp.service;

import java.util.List;

import com.dpworld.persistence.entity.CurrencyDetails;

public interface CurrencyDetailsService {

	CurrencyDetails getByCurrencyId(Integer currencyId);
	
	List<CurrencyDetails> getAll();

	CurrencyDetails findDefaultCurrency();
	
	List<CurrencyDetails> findByCurrencyName(String currencyname);

}

package com.dpworld.masterdataapp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dpworld.masterdataapp.service.CurrencyDetailsService;
import com.dpworld.persistence.entity.CurrencyDetails;
import com.dpworld.persistence.repository.CurrencyRepository;

@Service
public class CurrencyServiceImpl implements CurrencyDetailsService {

	@Value("${wms.defaults.currency}")
	private String defaultCurrency;
	
	@Autowired
	private CurrencyRepository currencyRepository;

	@Override
	public CurrencyDetails getByCurrencyId(Integer currencyId) {
		return currencyRepository.findByCurrencyId(currencyId);
	}

	@Override
	public List<CurrencyDetails> getAll() {
		return currencyRepository.findAll();
	}
	
	@Override
	public CurrencyDetails findDefaultCurrency() {
		List<CurrencyDetails> list = currencyRepository.findByCurrencyName(defaultCurrency);
		if (list != null && !list.isEmpty())
			return list.get(0);
		else {
			CurrencyDetails currency = new CurrencyDetails();
			currency.setCurrencyName(defaultCurrency);
			return currencyRepository.save(currency);
		}
	}
	
	@Override
	public List<CurrencyDetails> findByCurrencyName(String currencyName) {
		return currencyRepository.findByCurrencyName(currencyName);
	}

}

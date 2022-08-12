package com.dpworld.masterdataapp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dpworld.masterdataapp.service.MarketTypeService;
import com.dpworld.persistence.entity.MarketType;
import com.dpworld.persistence.repository.MarketTypeRepository;

@Service
public class MarketTypeServiceImpl implements MarketTypeService {

	@Autowired
	private MarketTypeRepository marketTypeRepository;

	@Override
	public MarketType getByMarketTypeId(Integer marketTypeId) {
		return marketTypeRepository.findByMarketTypeId(marketTypeId);
	}

	@Override
	public List<MarketType> getAll() {
		return marketTypeRepository.findAll();
	}

}

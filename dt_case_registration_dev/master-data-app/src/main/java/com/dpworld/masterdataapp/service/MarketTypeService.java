package com.dpworld.masterdataapp.service;

import java.util.List;

import com.dpworld.persistence.entity.MarketType;

public interface MarketTypeService {

	MarketType getByMarketTypeId(Integer marketTypeId);
	
	List<MarketType> getAll();
}

package com.dpworld.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.MarketType;

@Repository
public interface MarketTypeRepository extends JpaRepository<MarketType, Integer> {

	MarketType findByMarketTypeId(Integer marketTypeId);

	List<MarketType> findAll();
}

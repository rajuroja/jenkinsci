package com.dpworld.masterdataapp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dpworld.masterdataapp.service.GoodsConditionService;
import com.dpworld.persistence.entity.GoodsCondition;
import com.dpworld.persistence.repository.GoodsConditionRepository;

@Service
public class GoodsConditionServiceImpl implements GoodsConditionService {

	@Autowired
	private GoodsConditionRepository goodsConditionRepository;

	@Override
	public GoodsCondition getByGoodsConditionId(Byte id) {
		return goodsConditionRepository.findByGoodsConditionId(id);
	}

	@Override
	public List<GoodsCondition> getAll() {
		return goodsConditionRepository.findAll();
	}

}

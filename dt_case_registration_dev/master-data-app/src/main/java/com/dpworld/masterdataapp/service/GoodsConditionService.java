package com.dpworld.masterdataapp.service;

import java.util.List;

import com.dpworld.persistence.entity.GoodsCondition;

public interface GoodsConditionService {

	GoodsCondition getByGoodsConditionId(Byte id);

	List<GoodsCondition> getAll();
}

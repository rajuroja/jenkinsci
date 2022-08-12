package com.dpworld.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.GoodsCondition;

@Repository
public interface GoodsConditionRepository extends CrudRepository<GoodsCondition, Integer> {

	GoodsCondition findByGoodsConditionId(Byte id);

	List<GoodsCondition> findAll();
}

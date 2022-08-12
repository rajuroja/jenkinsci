package com.dpworld.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.ComplainType;

@Repository
public interface ComplainTypeRepository extends CrudRepository<ComplainType, Integer> {

	ComplainType findByComplainTypeKeyId(Integer complainTypeKeyId);

	List<ComplainType> findAll();
}

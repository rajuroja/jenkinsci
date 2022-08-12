package com.dpworld.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.CaseType;
import com.dpworld.persistence.entity.PortType;

@Repository
public interface PortTypeRepository extends CrudRepository<PortType, Integer> {

	CaseType findByPortId(Integer portTypeId);

	List<PortType> findAll();
}

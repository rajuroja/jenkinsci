package com.dpworld.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.CaseType;

@Repository
public interface CaseTypeRepository extends CrudRepository<CaseType, Integer> {

	CaseType findByCaseTypeId(Integer caseTypeId);

	List<CaseType> findAll();
}

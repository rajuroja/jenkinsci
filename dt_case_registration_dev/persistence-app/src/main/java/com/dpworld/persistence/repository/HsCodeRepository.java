package com.dpworld.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.HsCode;

@Repository
public interface HsCodeRepository extends CrudRepository<HsCode, Long> {

	HsCode findByHsCodeId(Long hsCodeId);

	List<HsCode> findTop20ByHsCodeContains(String hsCode);

	@Query("Select hscode from HsCode hscode where hscode.hsCode = ?1")
	List<HsCode> findAllByHsCode(String hsCode);
}

package com.dpworld.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.BarCode;
import com.dpworld.persistence.entity.HsCode;

@Repository
public interface BarCodeRepository extends CrudRepository<BarCode, Long> {

	BarCode findByBarCodeAndHsCodeId(String barCode, HsCode hsCodeId);

	BarCode findByBarCodeId(Long barCodeId);

	@Query("Select barcode from BarCode barcode where barcode.pushedToOic is null OR barcode.pushedToOic='F' ")
	List<BarCode> findAllNotPushedInOic();

}

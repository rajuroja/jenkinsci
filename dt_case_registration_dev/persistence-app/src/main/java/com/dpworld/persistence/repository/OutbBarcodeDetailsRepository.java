package com.dpworld.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dpworld.persistence.entity.OutbBarCodeDetails;

@Repository
public interface OutbBarcodeDetailsRepository extends JpaRepository<OutbBarCodeDetails, Long> {

	@Modifying
	@Transactional
	@Query("DELETE FROM OutbBarCodeDetails u WHERE u.outbBarcodeDetails IN (?1)")
	void deleteByOutbBarcodeDetailsIn(List<Long> collect);

	@Modifying
	@Transactional
	@Query("DELETE FROM OutbBarCodeDetails u WHERE u.outbCargoInformationId.id IN (?1)")
	void deleteAllByCargoInformationIdsIn(List<Long> ids);

}

package com.dpworld.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dpworld.persistence.entity.InbBarCodeDetails;

@Repository
public interface InbBarcodeDetailsRepository extends JpaRepository<InbBarCodeDetails, Long> {

	@Modifying
	@Transactional
	@Query("DELETE FROM InbBarCodeDetails u WHERE u.inbBarcodeDetails IN (?1)")
	void deleteByInbBarcodeDetailsIn(List<Long> barcodeDetailIds);

	@Modifying
	@Transactional
	@Query("DELETE FROM InbBarCodeDetails u WHERE u.inbCargoInformationId.id IN (?1)")
	void deleteAllByCargoInformationIdsIn(List<Long> ids);

}

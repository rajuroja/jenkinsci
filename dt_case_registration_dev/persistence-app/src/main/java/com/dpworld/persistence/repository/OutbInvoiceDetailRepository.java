package com.dpworld.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dpworld.persistence.entity.OutbInvoiceDetail;

@Repository
public interface OutbInvoiceDetailRepository extends JpaRepository<OutbInvoiceDetail, Long> {

	@Modifying
	@Transactional
	@Query("DELETE FROM OutbInvoiceDetail u WHERE u.outbCargoInformationId.id IN (?1)")
	void deleteAllByCargoInformationIdsIn(List<Long> ids);
	
}

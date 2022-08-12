package com.dpworld.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dpworld.persistence.entity.InbInvoiceDetail;

@Repository
public interface InbInvoiceDetailRepository extends JpaRepository<InbInvoiceDetail, Long> {

	@Modifying
	@Transactional
	@Query("DELETE FROM InbInvoiceDetail u WHERE u.id = ?1")
	void deleteById(Long id);

	@Modifying
	@Transactional
	@Query("DELETE FROM InbInvoiceDetail u WHERE u.inbCargoInformationId.id IN (?1)")
	void deleteAllByCargoInformationIdsIn(List<Long> ids);
}

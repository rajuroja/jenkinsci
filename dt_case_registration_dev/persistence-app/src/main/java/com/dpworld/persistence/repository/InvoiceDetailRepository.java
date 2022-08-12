package com.dpworld.persistence.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.InvoiceDetail;
import com.dpworld.persistence.vo.InvoiceDetailsListVo;

@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Long> {

	@Query("Select invoice from InvoiceDetail invoice where invoice.invoiceDetailId = ?1")
	InvoiceDetail findByInvoiceDetailId(Long id);

	@Query("Select invoice from InvoiceDetail invoice order by invoice.createdOn desc")
	List<InvoiceDetail> findAll();

	// Logic : Start1 <= End2 AND Start2 <= End1
	@Query("Select invoice from InvoiceDetail invoice where invoice.fromDate <= ?2 AND invoice.toDate >= ?1 AND invoice.companyCode=?3")
	List<InvoiceDetail> findAllByDateRangeOverlapAndBusinessCode(Date fromDate, Date toDate, String businessCode);

	@Query("Select invoice from InvoiceDetail invoice where invoice.createdOn >= ?1 AND invoice.createdOn <= ?2 AND invoice.companyCode = ?3 AND invoice.isCancel = ?4")
	List<InvoiceDetail> findAllByCreatedDateBetweenAndBusinessCodeAndIsCancel(Date fromDate, Date toDate, String businessCode, boolean isCancel);

	@Query("Select invoice.invoiceDetailId AS invoiceDetailId, invoice.referenceNumber AS referenceNumber, invoice.createdOn AS createdOn FROM InvoiceDetail invoice")
	List<InvoiceDetailsListVo> list();

	@Query("Select invoice from InvoiceDetail invoice where invoice.companyCode in (?1) order by invoice.createdOn desc")
	List<InvoiceDetail> findAllByCompanyCodes(List<String> agentCodes);
	
	InvoiceDetail findByReferenceNumber(String referenceNumber);

}
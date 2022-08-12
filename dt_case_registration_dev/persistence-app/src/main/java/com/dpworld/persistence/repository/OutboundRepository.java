package com.dpworld.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.InvoiceDetail;
import com.dpworld.persistence.entity.Outbound;
import com.dpworld.persistence.vo.SearchInventory;

@Repository
public interface OutboundRepository extends JpaRepository<Outbound, Long>, JpaSpecificationExecutor<Outbound> {

	@Query("Select new com.dpworld.persistence.vo.SearchInventory(ob.outboundId, ob.referenceNumber, ob.createdOn, 'Outbound', ob.invoiceDetail.invoiceDetailId) "
			+ " from Outbound ob JOIN OutbDeclarationInformation decl on decl.outbound.outboundId = ob.outboundId "
			+ " where ob.isCancel=false AND decl.businessCode in (?1)")
	List<SearchInventory> listSearchInventory(List<String> agentCodes);

	Outbound findByOutboundId(long id);

	List<Outbound> findByOutboundIdIn(List<Long> outboundIds);

	@Modifying
	@Query("Update Outbound ob set ob.invoiceDetail = ?2 where ob.outboundId in (?1)")
	void updateOutboundsWithInvoice(List<Long> outboundIds, InvoiceDetail invoice);


	@Query("Select ob from Outbound ob JOIN OutbDeclarationInformation decl on decl.outbound.outboundId = ob.outboundId "
			+ " where ob.isCancel=?2 AND decl.businessCode in (?1)")
	List<Outbound> findAllByBusinessCodeAndIsCancel(List<String> agentCodes, Boolean isCancel);
	
	Outbound findByReferenceNumber(String referenceNumber);

}
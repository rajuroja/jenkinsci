package com.dpworld.persistence.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.Inbound;
import com.dpworld.persistence.vo.SearchInventory;

@Repository
public interface InboundRepository extends JpaRepository<Inbound, Long>, JpaSpecificationExecutor<Inbound> {

	@Query("Select inbound from Inbound inbound where inbound.inboundId=?1")
	Inbound findByInboundId(Long inboundId);

	@Query("Select inbound from Inbound inbound where inbound.inboundId in (?1)")
	List<Inbound> findByInboundIdIn(List<Long> inboundIds);

	@Query("Select new com.dpworld.persistence.vo.SearchInventory(ib.inboundId, ib.referenceNumber, ib.createdOn, 'Inbound', ib.returnType) "
			+ " from Inbound ib JOIN InbDeclarationInformation decl on decl.inbound.inboundId = ib.inboundId "
			+ " where ib.isCancel=false AND decl.businessCode in (?1) ")
	List<SearchInventory> listSearchInventory(List<String> agentCodes);

	@Query("Select ib from Inbound ib JOIN InbDeclarationInformation decl on decl.inbound.inboundId = ib.inboundId "
			+ " where ib.isCancel=?2 AND decl.businessCode in (?1) ")
	List<Inbound> findAllByBusinessCodeAndIsCancel(List<String> agentCodes, Boolean isCancel);
	
	@Query("Select ib from Inbound ib JOIN InbDeclarationInformation decl on decl.inbound.inboundId = ib.inboundId "
			+ " where ib.isCancel='F' AND decl.businessCode = ?1 AND ib.createdOn >= ?2 AND ib.createdOn <= ?3")
	List<Inbound> findAllByBusinessCodeAndDateRange(String businessCode, Date fromDate, Date toDate);
	
	Inbound findByReferenceNumber(String referenceNumber);

}

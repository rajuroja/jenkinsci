package com.dpworld.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dpworld.persistence.entity.OutbUploadDocuments;

@Repository
public interface OutbUploadDocumentsRepository extends JpaRepository<OutbUploadDocuments, Long> {

	@Query("SELECT COUNT(doc) FROM OutbUploadDocuments doc WHERE doc.outbound.outboundId=?1 AND doc.uploadDocumentTypeId.uploadDocumentTypeName=?2 AND doc.id NOT IN (?3) AND doc.isCancel=false ")
	Integer countByOutboundIdUploadDocumentTypeIsCancelAndIdNotIn(Long outboundId, String documentType, List<Long> ids);

	@Modifying
	@Transactional
	@Query("UPDATE OutbUploadDocuments u set u.isCancel=true WHERE u.id IN (?1)")
	void cancelByIdIn(List<Long> ids);
}
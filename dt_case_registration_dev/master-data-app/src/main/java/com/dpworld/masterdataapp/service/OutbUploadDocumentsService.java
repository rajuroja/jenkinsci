package com.dpworld.masterdataapp.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.dpworld.persistence.entity.OutbUploadDocuments;

public interface OutbUploadDocumentsService {

	OutbUploadDocuments save(OutbUploadDocuments document);

	void save(OutbUploadDocuments document, MultipartFile file);

	OutbUploadDocuments cancelById(Long id);

	OutbUploadDocuments findById(Long id);

	Integer countByOutboundIdUploadDocumentTypeIsCancelAndIdNotIn(Long outboundId, String fileType, List<Long> deletedDocuments);

	void cancelByIdIn(List<Long> deletedSalesInvoiceDocuments);

}

package com.dpworld.masterdataapp.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.dpworld.persistence.entity.InbUploadDocuments;

public interface InbUploadDocumentsService {

	InbUploadDocuments save(InbUploadDocuments document);

	void save(InbUploadDocuments document, MultipartFile file);

	InbUploadDocuments cancelById(Long id);

	InbUploadDocuments findById(Long id);

	Integer countByInboundIdUploadDocumentTypeIsCancelAndIdNotIn(Long inboundId, String packingList, List<Long> deletedPackingListDocuments);

	void cancelByIdIn(List<Long> deletedCustomDeclarationDocuments);

}

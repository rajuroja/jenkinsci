package com.dpworld.masterdataapp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dpworld.masterdataapp.service.FileUploadService;
import com.dpworld.masterdataapp.service.InbUploadDocumentsService;
import com.dpworld.persistence.entity.InbUploadDocuments;
import com.dpworld.persistence.repository.InbUploadDocumentsRepository;

@Service
public class InbUploadDocumentsServiceImpl implements InbUploadDocumentsService {

	@Autowired
	private InbUploadDocumentsRepository inbUploadDocumentsRepository;

	@Autowired
	private FileUploadService fileUploadService;

	@Override
	public InbUploadDocuments save(InbUploadDocuments document) {
		return inbUploadDocumentsRepository.save(document);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(InbUploadDocuments document, MultipartFile file) {
		String response = fileUploadService.uploadInboundDocument(document, file);
		if (response != null) {
			document.setFilePath(response);
			inbUploadDocumentsRepository.save(document);
		}
	}

	@Override
	public InbUploadDocuments cancelById(Long id) {
		InbUploadDocuments uploadDoc = findById(id);
		uploadDoc.setIsCancel(true);
		return inbUploadDocumentsRepository.save(uploadDoc);
	}

	@Override
	public InbUploadDocuments findById(Long id) {
		return inbUploadDocumentsRepository.findById(id).orElse(null);
	}

	@Override
	public Integer countByInboundIdUploadDocumentTypeIsCancelAndIdNotIn(Long inboundId, String documentType, List<Long> deletedDocuments) {
		return inbUploadDocumentsRepository.countByInboundIdUploadDocumentTypeIsCancelAndIdNotIn(inboundId, documentType, deletedDocuments);
	}

	@Override
	public void cancelByIdIn(List<Long> ids) {
		inbUploadDocumentsRepository.cancelByIdIn(ids);
	}

}

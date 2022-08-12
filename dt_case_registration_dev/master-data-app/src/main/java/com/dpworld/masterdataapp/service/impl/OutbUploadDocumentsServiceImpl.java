package com.dpworld.masterdataapp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dpworld.masterdataapp.service.FileUploadService;
import com.dpworld.masterdataapp.service.OutbUploadDocumentsService;
import com.dpworld.persistence.entity.OutbUploadDocuments;
import com.dpworld.persistence.repository.OutbUploadDocumentsRepository;

@Service
public class OutbUploadDocumentsServiceImpl implements OutbUploadDocumentsService {

	@Autowired
	private OutbUploadDocumentsRepository outbUploadDocumentsRepository;

	@Autowired
	private FileUploadService fileUploadService;

	@Override
	public OutbUploadDocuments save(OutbUploadDocuments document) {
		return outbUploadDocumentsRepository.save(document);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(OutbUploadDocuments document, MultipartFile file) {
		String response = fileUploadService.uploadOutboundDocument(document, file);
		if (response != null) {
			document.setFilePath(response);
			outbUploadDocumentsRepository.save(document);
		}
	}

	@Override
	public OutbUploadDocuments cancelById(Long id) {
		OutbUploadDocuments uploadDoc = findById(id);
		uploadDoc.setIsCancel(true);
		return outbUploadDocumentsRepository.save(uploadDoc);
	}
	
	@Override
	public void cancelByIdIn(List<Long> ids) {
		outbUploadDocumentsRepository.cancelByIdIn(ids);
	}

	@Override
	public OutbUploadDocuments findById(Long id) {
		return outbUploadDocumentsRepository.findById(id).orElse(null);
	}

	@Override
	public Integer countByOutboundIdUploadDocumentTypeIsCancelAndIdNotIn(Long outboundId, String fileType, List<Long> deletedDocuments) {
		return outbUploadDocumentsRepository.countByOutboundIdUploadDocumentTypeIsCancelAndIdNotIn(outboundId, fileType, deletedDocuments);
	}

}

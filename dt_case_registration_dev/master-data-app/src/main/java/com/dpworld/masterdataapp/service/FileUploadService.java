package com.dpworld.masterdataapp.service;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.dpworld.persistence.entity.InbUploadDocuments;
import com.dpworld.persistence.entity.OutbUploadDocuments;

public interface FileUploadService {

	String uploadOutboundDocument(OutbUploadDocuments document, MultipartFile file);

	String uploadInboundDocument(InbUploadDocuments document, MultipartFile file);
	
	String getFileBase64(String type, Long id) throws IOException;

	File getFile(String type, Long id);

}

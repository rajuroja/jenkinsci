package com.dpworld.apiapp.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.dpworld.apiapp.model.CaseDetailsRequest;
import com.dpworld.persistence.entity.CaseRegistrationDetails;

public interface CaseRegistrationService {
	public CaseRegistrationDetails addOrUpdateUser(CaseDetailsRequest caseDetailsRequest, boolean isUpdate, CaseRegistrationDetails userMasterEntity, List<MultipartFile> caseFiles);

}

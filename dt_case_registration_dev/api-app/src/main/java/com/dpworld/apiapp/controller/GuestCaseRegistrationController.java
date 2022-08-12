package com.dpworld.apiapp.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dpworld.apiapp.model.CaseDetailsRequest;
import com.dpworld.apiapp.service.CaseRegistrationService;
import com.dpworld.common.model.ApiResponse;
import com.dpworld.persistence.entity.CaseRegistrationDetails;

@RestController
@RequestMapping("/guestCaseRegistration")
public class GuestCaseRegistrationController {
	

	@Autowired
	CaseRegistrationService caseRegistrationService; 
	private static final Logger LOGGER = LogManager.getLogger(GuestCaseRegistrationController.class);

	@PostMapping(value = "/save", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
	public ApiResponse<Object> saveCaseWoLogin(@Valid @RequestPart("data") CaseDetailsRequest caseDetailsRequest,@RequestPart("filelist") List<MultipartFile> caseFiles){
		try {
			CaseRegistrationDetails caseRegistrationDetailsEntity = new CaseRegistrationDetails();
			caseRegistrationService.addOrUpdateUser(caseDetailsRequest, false, caseRegistrationDetailsEntity, caseFiles);
				return new ApiResponse<>(true, HttpStatus.OK.name(), "Success", "Case registred Successfully");
		} catch (DataIntegrityViolationException e) {
			LOGGER.error("Error during saving case due to data integrity violation exception error ");
			return new ApiResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.name(),"failed");
		} catch (Exception e) {
			LOGGER.error("Error during saving case due to error ");
			return new ApiResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.name(), "failed");
		}
	}
	
	@PostMapping("/saveInquiry")
	public ApiResponse<Object> saveInquiryWoLogin(@RequestBody CaseDetailsRequest caseDetailsRequest){
		try {
				return new ApiResponse<>(true, HttpStatus.OK.name(), "Success", "Case registred Successfully");
		} catch (DataIntegrityViolationException e) {
			LOGGER.error("Error during saving case due to data integrity violation exception error ");
			return new ApiResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.name(),"failed");
		} catch (Exception e) {
			LOGGER.error("Error during saving container object due to error ");
			return new ApiResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.name(), "failed");
		}
	}
	
}
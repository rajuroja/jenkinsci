package com.dpworld.masterdataapp.controller;

import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.FAILED;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.INBOUND_ADD_SUCCESS;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.INBOUND_CANCEL_SUCCESS;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.INBOUND_DETAILS_NOT_FOUND;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.INBOUND_ONLOAD_DATA_RETRIEVAL_SUCCESS;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.INBOUND_RETRIEVAL_SUCCESS;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.INBOUND_UPDATE_SUCCESS;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.INBOUND_UPLOAD_DATA_DELETED_SUCCESS;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.INBOUND_VALIDATION_ERROR;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.INBOUND_UPLOADDOC_NOT_FOUND;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.DATE_UPDATE_SUCCESS;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dpworld.common.model.ApiResponse;
import com.dpworld.common.utils.CustomException;
import com.dpworld.masterdataapp.constants.MasterDataAppConstants;
import com.dpworld.masterdataapp.model.request.InboundRequest;
import com.dpworld.masterdataapp.model.response.InboundAddResponse;
import com.dpworld.masterdataapp.model.response.InboundOnloadResponse;
import com.dpworld.masterdataapp.model.response.InboundResponse;
import com.dpworld.masterdataapp.service.InbUploadDocumentsService;
import com.dpworld.masterdataapp.service.InboundService;
import com.dpworld.persistence.entity.InbUploadDocuments;
import com.dpworld.persistence.entity.Inbound;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/inbound")
public class InboundController {

	private static Logger logger = LoggerFactory.getLogger(InboundController.class);

	@Autowired
	private InboundService inboundService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private InbUploadDocumentsService inbUploadDocService;

	@PostMapping(value = "/add", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ApiResponse<?> addInbound(@RequestPart("inbound") InboundRequest inboundRequest, @RequestPart("custom_declaration") List<MultipartFile> customDeclaration,
			@RequestPart("certificate_of_origin") List<MultipartFile> certificateOfOrigin, @RequestPart("commercial_invoice") List<MultipartFile> commercialInvoice,
			@RequestPart("packing_list") List<MultipartFile> packingList) {

		//logger.info("ENTRY :: addInbound :: Add inbound method with request : {}", inboundRequest);

		try {

			Boolean isUpdate = false;
			StringBuilder errMessage = inboundService.validateAddInboundRequest(inboundRequest, isUpdate, customDeclaration, certificateOfOrigin, commercialInvoice, packingList);
			if (errMessage.length() > 0) {
				logger.error("ERROR:: Validation message : {}", errMessage);
				return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), INBOUND_VALIDATION_ERROR + ": \n" + errMessage);
			}

			Inbound inbound = inboundService.addOrUpdateInbound(inboundRequest, isUpdate, customDeclaration, certificateOfOrigin, commercialInvoice, packingList);
			InboundResponse inboundResponse = convertToInboundResponse(inbound);
			//logger.info("EXIT:: addInbound:: {}", INBOUND_ADD_SUCCESS);
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), INBOUND_ADD_SUCCESS, inboundResponse);
		} catch (DataIntegrityViolationException e) {
			logger.error("ERROR:: addInbound:: Error occured while adding Inbound : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), FAILED + MasterDataAppConstants.INBOUND_ADD_UPDATE_FAILED);
		} catch (CustomException e) {
			logger.error("ERROR:: addInbound:: Error occured while adding Inbound : {}", e.getMessage());
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), FAILED + e.getMessage());
		} catch (Exception e) {
			logger.error("ERROR:: addInbound:: Error occured while adding Inbound : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.INTERNAL_SERVER_ERROR.name(), FAILED + MasterDataAppConstants.INBOUND_ADD_UPDATE_FAILED);
		}
	}

	@PutMapping(value = "/update", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ApiResponse<Object> updateInbound(@RequestPart("inbound") InboundRequest inboundRequest, @RequestPart("custom_declaration") List<MultipartFile> customDeclaration,
			@RequestPart("certificate_of_origin") List<MultipartFile> certificateOfOrigin, @RequestPart("commercial_invoice") List<MultipartFile> commercialInvoice,
			@RequestPart("packing_list") List<MultipartFile> packingList) {

		//logger.info("ENTRY :: updateInbound :: Update inbound method with request : {}", inboundRequest);

		Boolean isUpdate = TRUE;
		StringBuilder errMessage = inboundService.validateAddInboundRequest(inboundRequest, isUpdate, customDeclaration, certificateOfOrigin, commercialInvoice, packingList);
		if (errMessage.length() > 0) {
			logger.error("ERROR:: updateInbound:: Validation message : {}", errMessage);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), INBOUND_VALIDATION_ERROR + ": \n" + errMessage);
		}

		try {
			Inbound inbound = inboundService.addOrUpdateInbound(inboundRequest, isUpdate, customDeclaration, certificateOfOrigin, commercialInvoice, packingList);
			InboundResponse inboundResponse = convertToInboundResponse(inbound);
			//logger.info("EXIT:: updateInbound:: {}", INBOUND_UPDATE_SUCCESS);
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), INBOUND_UPDATE_SUCCESS, inboundResponse);
		} catch (DataIntegrityViolationException e) {
			logger.error("ERROR:: addInbound:: Error occured while updating Inbound : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), FAILED + MasterDataAppConstants.INBOUND_ADD_UPDATE_FAILED);
		} catch (CustomException e) {
			logger.error("ERROR:: addInbound:: Error occured while updating Inbound : {}", e.getMessage());
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), FAILED + e.getMessage());
		} catch (Exception e) {
			logger.error("ERROR:: addInbound:: Error occured while updating Inbound : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.INTERNAL_SERVER_ERROR.name(), FAILED + MasterDataAppConstants.INBOUND_ADD_UPDATE_FAILED);
		}
	}

	@PostMapping("/cancel")
	public ApiResponse<Object> cancelInbound(@RequestBody List<Long> inboundIds) {

		//logger.info("ENTRY:: cancelInbound:: Cancel inbound method with inboundIds : {}", inboundIds);

		List<Inbound> inboundList = inboundService.getInboundByIds(inboundIds);
		if (null == inboundList || inboundList.isEmpty()) {
			logger.error("ERROR:: cancelInbound:: {}", INBOUND_DETAILS_NOT_FOUND);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), INBOUND_DETAILS_NOT_FOUND);
		}

		try {
			inboundService.cancelInbound(inboundList);
			List<InboundAddResponse> inboundResponse = inboundList.stream().map(e -> convertToInboundAddResponse(e)).collect(Collectors.toList());
			//logger.info("EXIT:: cancelInbound:: {}", INBOUND_CANCEL_SUCCESS);
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), INBOUND_CANCEL_SUCCESS, inboundResponse);
		} catch (CustomException e) {
			logger.error("ERROR:: cancelInbound:: Error occured while cancelling Inbound : {}", e.getMessage());
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), FAILED + e.getMessage());
		} catch (Exception e) {
			logger.error("ERROR:: cancelInbound:: Error occured while cancelling Inbound : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.INTERNAL_SERVER_ERROR.name(), FAILED + " " + MasterDataAppConstants.INBOUND_CANCEL_FAILED);
		}
	}

	@GetMapping("/fetch/{inboundId}")
	public ApiResponse<?> fetchInboundById(@PathVariable("inboundId") String inboundId) {
		//logger.info("ENTRY:: fetchInboundById:: Fetch inbound method with inboundId : {}", inboundId);
		try {
			Long inboundIdValue = (inboundId == null || inboundId.isEmpty()) ? 0l : Long.valueOf(inboundId);
			Inbound inbound = inboundService.getInboundById(inboundIdValue);
			if (inbound == null || inbound.getIsCancel()) {
				logger.error("ERROR:: fetchInboundById:: {}", INBOUND_DETAILS_NOT_FOUND);
				return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), INBOUND_DETAILS_NOT_FOUND);
			}
			InboundResponse inboundResponse = convertToInboundResponse(inbound);
			//logger.info("EXIT:: fetchInboundById:: {}", INBOUND_RETRIEVAL_SUCCESS);
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), INBOUND_RETRIEVAL_SUCCESS, inboundResponse);
		} catch (Exception e) {
			logger.error("ERROR:: fetchInboundId:: Error occured while fetching Inbound : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.INTERNAL_SERVER_ERROR.name(), FAILED + MasterDataAppConstants.INBOUND_FETCH_FAILED);
		}
	}

	@GetMapping("/onload")
	public ApiResponse<Object> inboundOnLoad(@RequestParam("serviceId") String serviceId) {
		//logger.info("ENTRY:: inboundOnLoad:: Fetch inbound onload data with serviceId : {}", serviceId);
		try {
			InboundOnloadResponse inboundOnloadResponse = inboundService.getOnLoadData(serviceId);
			//logger.info("EXIT:: inboundOnLoad:: {}", INBOUND_ONLOAD_DATA_RETRIEVAL_SUCCESS);
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), INBOUND_ONLOAD_DATA_RETRIEVAL_SUCCESS, inboundOnloadResponse);
		} catch (CustomException e) {
			logger.error("ERROR:: addInbound:: Error occured while fetching Inbound onload data : {}", e.getMessage());
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), FAILED + e.getMessage());
		} catch (Exception e) {
			logger.error("ERROR:: inboundOnLoad:: Error occured while fetching Inbound onload data : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.INTERNAL_SERVER_ERROR.name(), FAILED + MasterDataAppConstants.INBOUND_ONLOAD_FAILED);
		}
	}

	public InboundAddResponse convertToInboundAddResponse(Inbound inbound) {
		InboundAddResponse inboundAddResponse = objectMapper.convertValue(inbound, InboundAddResponse.class);
		return inboundAddResponse;
	}

	public InboundResponse convertToInboundResponse(Inbound inbound) {
		InboundResponse inboundResponse = objectMapper.convertValue(inbound, InboundResponse.class);
		return inboundResponse;
	}

	@PostMapping("/canceldoc")
	public ApiResponse<Object> cancelInbUploadDocdById(long id) {
		//logger.info("ENTRY:: inboundUploadDoc:: Fetch InbUploadDoc data: {}", id);
		try {
			InbUploadDocuments uploadDocId = inbUploadDocService.findById(id);

			if (null == uploadDocId) {
				logger.error("ERROR:: inboundUploadDoc : {}", INBOUND_UPLOADDOC_NOT_FOUND);
				return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), INBOUND_UPLOADDOC_NOT_FOUND);
			}
			InbUploadDocuments uploadDocs = inbUploadDocService.cancelById(id);
			//logger.info("EXIT:: inboundUploadDoc : {}", INBOUND_UPLOAD_DATA_DELETED_SUCCESS);
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), INBOUND_UPLOAD_DATA_DELETED_SUCCESS, uploadDocs);
		} catch (Exception e) {
			logger.error("ERROR:: inboundUpLoad Doc:: Error occured while deleting Inbound upload doc: {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.INTERNAL_SERVER_ERROR.name(), FAILED + " Calncel document failed");
		}
	}
	
	@GetMapping(value = "/updateDate")
	public ApiResponse<Object> updateDate(@RequestParam("createdOn") String createdOn, @RequestParam("referenceNumber") String referenceNumber) {

		//logger.info("ENTRY:: updateDate:: Fetch updateDate data: {}", createdOn + "," + referenceNumber);
		Inbound inbound = inboundService.findByReferenceNumber(referenceNumber);
		if (inbound == null) {
			logger.error("ERROR:: updateDate : {}", INBOUND_DETAILS_NOT_FOUND);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), INBOUND_DETAILS_NOT_FOUND);
		}
		try {
			inbound = inboundService.updateDateByReferenceNumber(inbound, createdOn);
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), DATE_UPDATE_SUCCESS, inbound);
		} catch (Exception e) {
			logger.error("ERROR:: updateDate :: Error occured while updating Inbound updateDate : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.INTERNAL_SERVER_ERROR.name(), FAILED + " Update date failed");

		}
	}

}
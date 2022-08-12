
package com.dpworld.masterdataapp.controller;

import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.*;
import static java.lang.Boolean.*;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dpworld.common.model.ApiResponse;
import com.dpworld.common.utils.CustomException;
import com.dpworld.masterdataapp.constants.MasterDataAppConstants;
import com.dpworld.masterdataapp.model.request.OutboundRequest;
import com.dpworld.masterdataapp.model.response.OutboundAddResponse;
import com.dpworld.masterdataapp.model.response.OutboundOnloadResponse;
import com.dpworld.masterdataapp.model.response.OutboundResponse;
import com.dpworld.masterdataapp.service.FileUploadService;
import com.dpworld.masterdataapp.service.OutbUploadDocumentsService;
import com.dpworld.masterdataapp.service.OutboundService;
import com.dpworld.persistence.entity.OutbUploadDocuments;
import com.dpworld.persistence.entity.Outbound;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/outbound")
public class OutboundController {

	private static Logger logger = LoggerFactory.getLogger(OutboundController.class);

	@Autowired
	private OutboundService outboundService;

	@Autowired
	FileUploadService fileUploadService;

	@Autowired
	OutbUploadDocumentsService outbUploadDocService;

	@Autowired
	private ObjectMapper objectMapper;

	@PostMapping(value = "/add", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ApiResponse<Object> addOutbound(@RequestPart("outbound") OutboundRequest outboundRequest, @RequestPart("custom_declaration") List<MultipartFile> customDeclaration,
			@RequestPart("packing_list") List<MultipartFile> packingList, @RequestPart("sales_invoice") List<MultipartFile> salesInvoice) {

		//logger.info("ENTRY :: addOutbound :: Add outbound method with request : {}", outboundRequest);
		StringBuilder errMessage = outboundService.validateAddOutboundRequest(outboundRequest, false, customDeclaration, packingList, salesInvoice);
		if (errMessage.length() > 0) {
			logger.error("ERROR:: Validation message : {}", errMessage);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), OUTBOUND_VALIDATION_ERROR + ": \n" + errMessage);
		}
		try {
			Outbound outbound = outboundService.addOrUpdateOutbound(outboundRequest, false, customDeclaration, packingList, salesInvoice);
			OutboundResponse outboundResponse = convertToOutboundResponse(outbound);
			//logger.info("EXIT:: addOutbound : {}", OUTBOUND_ADD_SUCCESS);
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), OUTBOUND_ADD_SUCCESS, outboundResponse);
			
		} catch (DataIntegrityViolationException e) {
			logger.error("ERROR:: addOutbound:: Error occured while adding Outbound : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), FAILED + MasterDataAppConstants.OUTBOUND_ADD_UPDATE_FAILED);
		} catch (CustomException e) {
			logger.error("ERROR:: addOutbound:: Error occured while adding Outbound : {}", e.getMessage());
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), FAILED + e.getMessage());
		} catch (Exception e) {
			logger.error("ERROR:: addOutbound:: Error occured while adding Outbound : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.INTERNAL_SERVER_ERROR.name(), FAILED + MasterDataAppConstants.OUTBOUND_ADD_UPDATE_FAILED);
		}
	}

	@PutMapping(value = "/update", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ApiResponse<Object> updateOutbound(@RequestPart("outbound") OutboundRequest outboundRequest, @RequestPart("custom_declaration") List<MultipartFile> customDeclaration,
			@RequestPart("packing_list") List<MultipartFile> packingList, @RequestPart("sales_invoice") List<MultipartFile> salesInvoice) {

		//logger.info("ENTRY :: updateOutbound :: Update outbound method with request : {}.", outboundRequest);

		StringBuilder errMessage = outboundService.validateAddOutboundRequest(outboundRequest, TRUE, customDeclaration, packingList, salesInvoice);
		if (errMessage.length() > 0) {
			logger.error("ERROR:: updateOutbound:: Validation message : {}", errMessage);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), OUTBOUND_VALIDATION_ERROR + ": \n" + errMessage);
		}

		try {
			Outbound outbound = outboundService.addOrUpdateOutbound(outboundRequest, TRUE, customDeclaration, packingList, salesInvoice);
			OutboundResponse outboundResponse = convertToOutboundResponse(outbound);
			//logger.info("EXIT:: updateOutbound : {}", OUTBOUND_UPDATE_SUCCESS);
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), OUTBOUND_UPDATE_SUCCESS, outboundResponse);
		} catch (DataIntegrityViolationException e) {
			logger.error("ERROR:: addOutbound:: Error occured while adding Outbound : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), FAILED + MasterDataAppConstants.OUTBOUND_ADD_UPDATE_FAILED);
		} catch (CustomException e) {
			logger.error("ERROR:: addOutbound:: Error occured while adding Outbound : {}", e.getMessage());
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), FAILED + e.getMessage());
		} catch (Exception e) {
			logger.error("ERROR:: addOutbound:: Error occured while adding Outbound : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.INTERNAL_SERVER_ERROR.name(), FAILED + MasterDataAppConstants.OUTBOUND_ADD_UPDATE_FAILED);
		}
	}

	@PostMapping("/cancel")
	public ApiResponse<Object> cancelOutbound(@RequestParam List<Long> outboundIds) {

		//logger.info("ENTRY:: cancelOutbound:: Cancel outbound method with outboundIds  : {}", outboundIds);

		List<Outbound> outboundList = outboundService.getOutboundByIds(outboundIds);
		if (null == outboundList || outboundList.isEmpty()) {
			logger.error("ERROR:: cancelOutbound : {}", OUTBOUND_DETAILS_NOT_FOUND);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), OUTBOUND_DETAILS_NOT_FOUND);
		}

		try {
			outboundService.cancelOutbound(outboundList);
			List<OutboundAddResponse> outboundResponse = outboundList.stream().map(e -> convertToOutboundAddResponse(e)).collect(Collectors.toList());
			//logger.info("EXIT:: cancelOutbound : {}", OUTBOUND_CANCEL_SUCCESS);
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), OUTBOUND_CANCEL_SUCCESS, outboundResponse);
		} catch (CustomException e) {
			logger.error("ERROR:: outboundOnLoad:: Error occured while cancelling Outbound : {}", e.getMessage());
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), FAILED, e.getMessage());
		} catch (Exception e) {
			logger.error("ERROR:: cancelOutbound:: Error occured while cancelling Outbound : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.INTERNAL_SERVER_ERROR.name(), FAILED + MasterDataAppConstants.OUTBOUND_CANCEL_FAILED);
		}
	}

	@GetMapping("/fetch/{outboundId}")
	public ApiResponse<Object> fetchOutboundById(@PathVariable String outboundId) {

		//logger.info("ENTRY:: fetchOutboundById:: Fetch outbound method with outboundId : {}", outboundId);

		Long outboundIdValue = (outboundId == null || outboundId.isEmpty()) ? 0l : Long.valueOf(outboundId);
		try {
			Outbound outbound = outboundService.getOutboundById(outboundIdValue);
			if (outbound == null || outbound.getIsCancel()) {
				logger.error("ERROR:: fetchOutboundById : {}", OUTBOUND_DETAILS_NOT_FOUND);
				return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), OUTBOUND_DETAILS_NOT_FOUND);
			}
			OutboundResponse outboundResponse = convertToOutboundResponse(outbound);
			//logger.info("EXIT:: fetchOutboundById : {}", OUTBOUND_RETRIEVAL_SUCCESS);
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), OUTBOUND_RETRIEVAL_SUCCESS, outboundResponse);
		} catch (Exception e) {
			logger.error("ERROR:: fetchOutboundId:: Error occured while fetching Outbound : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.INTERNAL_SERVER_ERROR.name(), FAILED + MasterDataAppConstants.OUTBOUND_FETCH_FAILED);
		}
	}

	@GetMapping("/onload")
	public ApiResponse<Object> outboundOnLoad(@RequestParam("serviceId") String serviceId) {
		//logger.info("ENTRY:: outboundOnLoad:: Fetch outbound onload data with serviceId : {}", serviceId);
		try {
			OutboundOnloadResponse outboundOnloadResponse = outboundService.getOnLoadData(serviceId);
			//logger.info("EXIT:: outboundOnLoad : {}", OUTBOUND_ONLOAD_DATA_RETRIEVAL_SUCCESS);
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), OUTBOUND_ONLOAD_DATA_RETRIEVAL_SUCCESS, outboundOnloadResponse);
		} catch (CustomException e) {
			logger.error("ERROR:: outboundOnLoad:: Error occured while fetching Outbound onload data : {}", e.getMessage());
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), FAILED, e.getMessage());
		} catch (Exception e) {
			logger.error("ERROR:: outboundOnLoad:: Error occured while fetching Outbound onload data : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.INTERNAL_SERVER_ERROR.name(), FAILED + MasterDataAppConstants.OUTBOUND_ONLOAD_FAILED);
		}
	}

	public OutboundAddResponse convertToOutboundAddResponse(Outbound outbound) {
		OutboundAddResponse outboundAddResponse = objectMapper.convertValue(outbound, OutboundAddResponse.class);
		return outboundAddResponse;
	}

	public OutboundResponse convertToOutboundResponse(Outbound outbound) {
		OutboundResponse outboundResponse = objectMapper.convertValue(outbound, OutboundResponse.class);
		return outboundResponse;
	}

	@PostMapping("/canceldoc")
	public ApiResponse<Object> cancelOutbUploadDocdById(long id) {

		//logger.info("ENTRY:: outboundUploadDoc:: Fetch OutbUploadDoc data : {}",  id);
		try {
			OutbUploadDocuments uploadDocId = outbUploadDocService.findById(id);

			if (null == uploadDocId) {
				logger.error("ERROR:: outboundUploadDoc : {}", OUTBOUND_UPLOADDOC_NOT_FOUND);
				return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), OUTBOUND_UPLOADDOC_NOT_FOUND);
			}
			OutbUploadDocuments uploadDocs = outbUploadDocService.cancelById(id);
			//logger.info("EXIT:: outboundUploadDoc : {}", OUTBOUND_UPLOAD_DATA_DELETED_SUCCESS);
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), OUTBOUND_UPLOAD_DATA_DELETED_SUCCESS, uploadDocs);
		} catch (Exception e) {
			logger.error("ERROR:: outboundUpLoad Doc:: Error occured while deleting Outbound upload doc : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.INTERNAL_SERVER_ERROR.name(), FAILED + e.getMessage());
		}
	}
	
	@GetMapping(value = "/updateDate")
	public ApiResponse<Object> updateDate(@RequestParam("createdOn") String createdOn, @RequestParam("referenceNumber") String referenceNumber) {

		//logger.info("ENTRY:: updateDate:: Fetch updateDate data: {}", createdOn + "," + referenceNumber);
		Outbound outbound = outboundService.findByReferenceNumber(referenceNumber);
		if (outbound == null) {
			logger.error("ERROR:: updateDate : {}", OUTBOUND_DETAILS_NOT_FOUND);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), OUTBOUND_DETAILS_NOT_FOUND);
		}
		try {
			outbound = outboundService.updateDateByReferenceNumber(outbound, createdOn);
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), DATE_UPDATE_SUCCESS, outbound);
		} catch (Exception e) {
			logger.error("ERROR:: updateDate :: Error occured while updating outbound updateDate : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.INTERNAL_SERVER_ERROR.name(), FAILED + e.getMessage());

		}
	}

}

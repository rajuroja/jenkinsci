package com.dpworld.masterdataapp.controller;

import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.*;
import static java.lang.Boolean.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dpworld.common.model.ApiResponse;
import com.dpworld.masterdataapp.constants.MasterDataAppConstants;
import com.dpworld.masterdataapp.model.request.BarCodeAddRequest;
import com.dpworld.masterdataapp.model.request.BarCodeSearchRequest;
import com.dpworld.masterdataapp.model.response.BarCodeResponse;
import com.dpworld.masterdataapp.service.BarCodeService;
import com.dpworld.persistence.entity.BarCode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/barcode")
public class BarcodeController {

	private static Logger logger = LoggerFactory.getLogger(BarcodeController.class);

	@Autowired
	private BarCodeService barCodeService;

	@Autowired
	private ObjectMapper objectMapper;

	@PostMapping("/search")
	public ApiResponse<Object> barCodeSearch(@RequestBody BarCodeSearchRequest barCodeSearchRequest) {
		//logger.info("ENTRY:: barCodeSearch:: Search for barcode : {}", barCodeSearchRequest);

		try {
			if (barCodeSearchRequest == null || barCodeSearchRequest.getBarCode() == null || barCodeSearchRequest.getBarCode().isEmpty() || barCodeSearchRequest.getHsCodeId() == null) {
				logger.error("ERROR:: barCodeSearch : {}", BARCODE_SEARCH_DETAIL_INVALID);
				return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), BARCODE_SEARCH_DETAIL_INVALID);
			}

			BarCodeResponse barCodeResponse = barCodeService.getByBarCode(barCodeSearchRequest.getBarCode(), barCodeSearchRequest.getHsCodeId());

			if (barCodeResponse == null || barCodeResponse.getBarCode() == null || barCodeResponse.getBarCode().isEmpty()) {
				logger.error("ERROR:: barCodeSearch : {}", BARCODE_SEARCH_DETAIL_INVALID);
				return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), BARCODE_SEARCH_DETAIL_INVALID);
			}
			//logger.info("EXIT:: barCodeSearch:: {}", BARCODE_SEARCH_SUCCESS);
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), BARCODE_SEARCH_SUCCESS, barCodeResponse);
		} catch (Exception e) {
			logger.error("ERROR:: barCodeSearch:: Error occured while searching Barcode : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.INTERNAL_SERVER_ERROR.name(), FAILED + " Barcode search failed");
		}
	}

	@PostMapping(value = "/add")
	public ApiResponse<Object> addBarCode(@RequestBody BarCodeAddRequest barCodeAddRequest) {

		//logger.info("ENTRY :: addBarCode :: Add inbound method with request : {}", barCodeAddRequest);

		Boolean isUpdate = false;
		StringBuilder errMessage = barCodeService.validateAddBarCodeRequest(barCodeAddRequest, isUpdate);
		if (errMessage.length() > 0) {
			logger.error("ERROR:: Validation message : {}", errMessage);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), BARCODE_VALIDATION_ERROR + ": " + errMessage);
		}

		try {
			BarCode barcode = barCodeService.addOrUpdateBarCode(barCodeAddRequest, isUpdate);
			BarCodeResponse barCodeResponse = convertToBarCodeAddResponse(barcode);
			//logger.info("EXIT:: addBarCode:: {}", BARCODE_ADD_SUCCESS);
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), BARCODE_ADD_SUCCESS, barCodeResponse);
		} catch (Exception e) {
			logger.error("ERROR:: addBarCode:: Error occured while adding Barcode : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.INTERNAL_SERVER_ERROR.name(), FAILED + " Barcode add failed");
		}
	}
	
	public BarCodeResponse convertToBarCodeAddResponse(BarCode barCode) {
		return objectMapper.convertValue(barCode, BarCodeResponse.class);
	}
	
	@GetMapping(value = "/pushBarCode")
	public ApiResponse<Object> pushBarCode(@RequestParam("id") Long id) {
		BarCode barcode = barCodeService.getByBarCodeId(id);
		if (barcode == null) {
			return new ApiResponse<>(FALSE, HttpStatus.INTERNAL_SERVER_ERROR.name(), FAILED + " Barcode Not Found");
		}
		if (barCodeService.pushItemDetailsToOic(barcode)) {
			barcode.setPushedToOic(true);
			barCodeService.save(barcode);
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), "Item details pushed to OIC successfully");
		} else {
			barcode.setPushedToOic(false);
			barCodeService.save(barcode);
			return new ApiResponse<>(FALSE, HttpStatus.INTERNAL_SERVER_ERROR.name(), FAILED + " Push Item details Failed");
		}
	}
	
	@GetMapping(value = "/syncBarCode")
	public ApiResponse<Object> syncBarCode() {
		//logger.info("ENTRY :: syncBarCode :: Sync BarCode API");
		barCodeService.syncBarCode();
		//logger.info("EXIT :: syncBarCode :: Sync BarCode API");
		return new ApiResponse<>(TRUE, HttpStatus.OK.name(), MasterDataAppConstants.SUCCESS);
	}

}

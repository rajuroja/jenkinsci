package com.dpworld.masterdataapp.controller;

import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.DATE_UPDATE_SUCCESS;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.FAILED;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.INVOICE_DETAILS_NOT_FOUND;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.INVOICE_GENERATE_SUCCESS;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.INVOICE_LIST_SUCCESS;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.INVOICE_VIEW_SUCCESS;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.dpworld.common.constants.DateFormates;
import com.dpworld.common.model.ApiResponse;
import com.dpworld.common.utils.CustomException;
import com.dpworld.common.utils.DateUtils;
import com.dpworld.masterdataapp.model.request.GenerateInvoiceRequest;
import com.dpworld.masterdataapp.model.response.FileResponse;
import com.dpworld.masterdataapp.service.InvoiceDetailService;
import com.dpworld.masterdataapp.utility.TmWmsUtils;
import com.dpworld.masterdataapp.webServiceResponse.JsonResponseData.Agents;
import com.dpworld.persistence.entity.InvoiceDetail;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/generate-invoice")
public class GenerateInvoiceController {

	private static Logger logger = LoggerFactory.getLogger(GenerateInvoiceController.class);

	@Autowired
	private InvoiceDetailService invoiceDetailService;

	@Autowired
	private TmWmsUtils tmWmsUtils;

	@Autowired
	private DateUtils dateUtils;

	@GetMapping("/list")
	public ApiResponse<?> list(@RequestParam("serviceId") String serviceId) throws Exception {
		//logger.info("ENTRY :: listInvoice with serviceId: {}", serviceId);
		try {
			Map<String, Object> map = new HashMap<>();
			List<Agents> list = tmWmsUtils.getImporterAgentsByLoggedInUser(serviceId);
			if (list != null && !list.isEmpty()) {
				List<String> agentCodes = list.stream().map(Agents::getAgentCode).collect(Collectors.toList());
				map.put("invoiceList", (agentCodes != null && !agentCodes.isEmpty()) ? invoiceDetailService.findAllByCompanyCodes(agentCodes) : null);
			} else
				map.put("invoiceList", null);
			map.put("agentList", list);
			//logger.info("EXIT :: listInvoice:: {}", INVOICE_LIST_SUCCESS);
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), INVOICE_LIST_SUCCESS, map);
		} catch (CustomException e) {
			logger.error("ERROR :: listInvoice : Error occured while list invoice : {}", e.getMessage());
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), FAILED + " Invoice list failed");
		} catch (Exception e) {
			logger.error("ERROR :: listInvoice : Error occured while list invoice : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.INTERNAL_SERVER_ERROR.name(), FAILED + " Invoice list failed");
		}
	}

	@PostMapping("/generate")
	public ApiResponse<?> generate(@RequestBody GenerateInvoiceRequest vo, @RequestParam("serviceId") String serviceId) {
		//logger.info("ENTRY :: generateInvoice : Generate invoice method with GenerateInvoiceRequest: {}, serviceId: {}", vo, serviceId);
		StringBuilder errMessage = invoiceDetailService.validateGenerateInvoiceRequest(vo);
		if (errMessage.length() > 0) {
			logger.error("ERROR:: Validation message : {}", errMessage);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), errMessage.toString());
		}

		Date fromDate = (vo.getFromDate() != null && !vo.getFromDate().isEmpty()) ? dateUtils.stringToDate(vo.getFromDate() + " 00:00:00", DateFormates.DDMMYYYYHHMMSS_HYPHEN) : null;
		Date toDate = (vo.getToDate() != null && !vo.getToDate().isEmpty()) ? dateUtils.stringToDate(vo.getToDate() + " 23:59:59", DateFormates.DDMMYYYYHHMMSS_HYPHEN) : null;

		if (fromDate == null || toDate == null) {
			logger.error("ERROR :: generateInvoice : Error occured while generate invoice");
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), "Invalid date format");
		}

		List<InvoiceDetail> existing = invoiceDetailService.findByDateRangeOverlapAndBusinessCode(fromDate, toDate, vo.getBusinessCode());
		if (existing != null && !existing.isEmpty()) {
			logger.error("ERROR :: generateInvoice : Error occured while generate invoice");
			return new ApiResponse<>(FALSE, HttpStatus.CONFLICT.name(), "For the selected date range below invoices are already exist. Please change the date generation criteria.", existing);
		}

		try {
			//logger.info("EXIT :: generateInvoice:: {}", INVOICE_GENERATE_SUCCESS);
			FileResponse fileResponse = invoiceDetailService.generateInvoice(vo, serviceId);
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), INVOICE_GENERATE_SUCCESS, fileResponse);
		} catch (CustomException e) {
			logger.error("ERROR :: generateInvoice : Error occured while generate invoice : {}", e.getMessage());
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), FAILED + " " + e.getMessage());
		} catch (JRException e) {
			logger.error("ERROR :: generateInvoice : Error occured while generate invoice : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), FAILED + " Generate Invoice Failed");
		} catch (Exception e) {
			logger.error("ERROR :: generateInvoice : Error occured while generate invoice : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), FAILED + " Generate Invoice Failed");
		}
	}

	@PostMapping("/view")
	public ApiResponse<?> view(@RequestBody GenerateInvoiceRequest vo) {
		//logger.info("ENTRY :: viewInvoice : viewInvoice method with request: {}", vo);
		StringBuilder errMessage = invoiceDetailService.validateViewInvoiceRequest(vo);
		if (errMessage.length() > 0) {
			logger.error("ERROR:: Validation message : {}", errMessage);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), errMessage.toString());
		}
		InvoiceDetail invoice = invoiceDetailService.findById(vo.getInvoiceId());
		if (invoice == null) {
			logger.error("ERROR :: viewInvoice : Error occured while view invoice : {}", INVOICE_DETAILS_NOT_FOUND);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), INVOICE_DETAILS_NOT_FOUND);
		}
		try {
			//logger.info("EXIT :: viewInvoice:: {}", INVOICE_VIEW_SUCCESS);
			FileResponse fileResponse = invoiceDetailService.viewInvoice(invoice, vo.getFormat());
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), INVOICE_VIEW_SUCCESS, fileResponse);
		} catch (Exception e) {
			logger.error("ERROR :: viewInvoice : Error occured while view invoice : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), FAILED + " View invoice failed");
		}
	}
	
	@GetMapping(value = "/updateDate")
	public ApiResponse<Object> updateDate(@RequestParam("createdOn") String createdOn, @RequestParam("referenceNumber") String referenceNumber) {

		//logger.info("ENTRY:: updateDate:: Fetch updateDate data: {}", createdOn + "," + referenceNumber);
		InvoiceDetail invoice = invoiceDetailService.findByReferenceNumber(referenceNumber);
		if (invoice == null) {
			logger.error("ERROR:: updateDate : {}", INVOICE_DETAILS_NOT_FOUND);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), INVOICE_DETAILS_NOT_FOUND);
		}
		try {
			invoice = invoiceDetailService.updateDateByReferenceNumber(invoice, createdOn);
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), DATE_UPDATE_SUCCESS, invoice);
		} catch (Exception e) {
			logger.error("ERROR:: updateDate :: Error occured while updating invoice date : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.INTERNAL_SERVER_ERROR.name(), FAILED + " Update date failed");

		}
	}

}

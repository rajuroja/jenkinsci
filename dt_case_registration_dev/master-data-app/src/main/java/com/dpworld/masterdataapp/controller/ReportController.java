package com.dpworld.masterdataapp.controller;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.io.IOException;
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

import com.dpworld.common.model.ApiResponse;
import com.dpworld.common.utils.CustomException;
import com.dpworld.masterdataapp.constants.MasterDataAppConstants;
import com.dpworld.masterdataapp.model.request.GenerateReportRequest;
import com.dpworld.masterdataapp.model.response.FileResponse;
import com.dpworld.masterdataapp.service.ReportService;
import com.dpworld.masterdataapp.utility.TmWmsUtils;
import com.dpworld.masterdataapp.webServiceResponse.JsonResponseData.Agents;
import com.dpworld.persistence.entity.Report;
import com.dpworld.persistence.entity.ReportType;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/report")
public class ReportController {

	private static Logger logger = LoggerFactory.getLogger(ReportController.class);

	@Autowired
	private ReportService reportService;
	
	@Autowired
	private TmWmsUtils tmWmsUtils;

	@GetMapping("/list")
	public ApiResponse<?> list(@RequestParam("serviceId") String serviceId) throws Exception {
		//logger.info("ENTRY :: listReport with serviceId : {}", serviceId);
		try {
			Map<String, Object> map = new HashMap<>();
			List<Agents> list = tmWmsUtils.getImporterAgentsByLoggedInUser(serviceId);
			if (list != null && !list.isEmpty()) {
				List<String> agentCodes = list.stream().map(Agents::getAgentCode).collect(Collectors.toList());
				if (agentCodes != null && !agentCodes.isEmpty())
					map.put("reportList", reportService.listByBusinessCodes(agentCodes));
				else
					map.put("reportList", null);
			} else
				map.put("reportList", null);
			map.put("agentList", list);
			//logger.info("EXIT :: listReport:: {}", MasterDataAppConstants.REPORT_LIST_SUCCESS);
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), "success", map);
		} catch (CustomException e) {
			logger.error("ERROR :: listReport: Error occured while list Report : {}", e.getMessage());
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), e.getMessage());
		} catch (Exception e) {
			logger.error("ERROR :: listReport: Error occured while list Report : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.INTERNAL_SERVER_ERROR.name(), MasterDataAppConstants.REPORT_LIST_FAILED);
		}
	}

	@PostMapping("/generate")
	public ApiResponse<?> generate(@RequestBody GenerateReportRequest vo) {
		//logger.info("ENTRY :: generateReport : Generate report method with request : {}", vo);
		StringBuilder errMessage = reportService.validateGenerateReportRequest(vo);
		if (errMessage.length() > 0) {
			logger.error("ERROR:: Validation message : {}", errMessage);
			return new ApiResponse<>(false, HttpStatus.BAD_REQUEST.name(), errMessage.toString());
		}
		try {
			if (vo.getReportType().equalsIgnoreCase(ReportType.MISMATCH.getReportType())) {
				FileResponse fileResponse = reportService.generateMismatchReport(vo);
				//logger.info("EXIT :: generateReport:: {}", MasterDataAppConstants.REPORT_GENERATE_SUCCESS);
				return new ApiResponse<>(true, HttpStatus.OK.name(), MasterDataAppConstants.REPORT_GENERATE_SUCCESS, fileResponse);
			} else if (vo.getReportType().equalsIgnoreCase(ReportType.GENERIC.getReportType())) {
				FileResponse fileResponse = reportService.generateGenericReport(vo);
				//logger.info("EXIT :: generateReport:: {}", MasterDataAppConstants.REPORT_GENERATE_SUCCESS);
				return new ApiResponse<>(true, HttpStatus.OK.name(), MasterDataAppConstants.REPORT_GENERATE_SUCCESS, fileResponse);
			} else
				return new ApiResponse<>(false, HttpStatus.BAD_REQUEST.name(), MasterDataAppConstants.INVALID_TYPE_OF_REPORT);
		} catch (CustomException e) {
			logger.error("ERROR :: generateReport : CustomException Error occured while generate report : {}", e.getMessage());
			return new ApiResponse<>(false, HttpStatus.BAD_REQUEST.name(), e.getMessage());
		} catch (IOException | JRException e) {
			logger.error("ERROR :: generateReport : IOException Error occured while generate report : {}", e);
			return new ApiResponse<>(false, HttpStatus.BAD_REQUEST.name(), MasterDataAppConstants.REPORT_GENERATE_FAILED);
		} catch (Exception e) {
			logger.error("ERROR :: generateReport : Exception Error occured while generate report : {}", e);
			return new ApiResponse<>(false, HttpStatus.BAD_REQUEST.name(), MasterDataAppConstants.REPORT_GENERATE_FAILED);
		}
	}
	
	@PostMapping("/view")
	public ApiResponse<?> view(@RequestBody GenerateReportRequest vo) {
		//logger.info("ENTRY :: viewReport : viewInvoice method with request: {}", vo);
		StringBuilder errMessage = reportService.validateViewReportRequest(vo);
		if (errMessage.length() > 0) {
			logger.error("ERROR:: Validation message : {}", errMessage);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), errMessage.toString());
		}
		Report report = reportService.findByReportId(vo.getReportId());
		if (report == null) {
			logger.error("ERROR :: viewReport : Error occured while view report: {}", MasterDataAppConstants.REPORT_DETAILS_NOT_FOUND);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), MasterDataAppConstants.REPORT_DETAILS_NOT_FOUND);
		}
		try {
			//logger.info("EXIT :: viewInvoice:: {}", MasterDataAppConstants.REPORT_VIEW_SUCCESS);
			FileResponse fileResponse = reportService.view(report, vo.getFormat());
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(),"Report view success", fileResponse);
		} catch (Exception e) {
			logger.error("ERROR :: viewReport : Error occured while view report: {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), MasterDataAppConstants.REPORT_VIEW_FAILED);
		}
	}
}

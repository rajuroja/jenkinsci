package com.dpworld.masterdataapp.controller;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.xml.stream.XMLStreamException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dpworld.common.model.ApiResponse;
import com.dpworld.common.utils.CustomException;
import com.dpworld.masterdataapp.constants.MasterDataAppConstants;
import com.dpworld.masterdataapp.constants.ScheduledTaskName;
import com.dpworld.masterdataapp.service.BarCodeService;
import com.dpworld.masterdataapp.service.HsCodeService;
import com.dpworld.masterdataapp.service.InvoiceDetailService;
import com.dpworld.masterdataapp.service.PushInventoryHistoryService;
import com.dpworld.masterdataapp.service.PushInventoryService;
import com.dpworld.masterdataapp.service.ReportService;
import com.dpworld.masterdataapp.service.ScheduledTasksService;
import com.dpworld.masterdataapp.service.VatService;
import com.dpworld.masterdataapp.service.WmsApiService;
import com.dpworld.masterdataapp.syncHsCode.SyncHsCodeRequestItems;
import com.dpworld.masterdataapp.syncHsCode.SyncHsCodeRequestRoot;
import com.dpworld.masterdataapp.syncHsCode.SyncHsCodeResponseRoot;
import com.dpworld.persistence.entity.BarCode;
import com.dpworld.persistence.entity.HsCode;
import com.dpworld.persistence.entity.HsCodeAuthority;
import com.dpworld.persistence.entity.InbCargoInformation;
import com.dpworld.persistence.entity.ReportType;
import com.dpworld.persistence.entity.ScheduledTasks;
import com.dpworld.persistence.entity.StatusType;
import com.dpworld.persistence.repository.HsCodeAuthorityRepository;
import com.dpworld.persistence.repository.InbCargoInformationRepository;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/web-service")
public class WebServiceController {

	@Autowired
	private HsCodeService hsCodeService;

	@Autowired
	private WmsApiService wmsApiService;

	@Autowired
	private VatService vatService;

	@Autowired
	private InvoiceDetailService invoiceDetailService;

	@Autowired
	private ReportService reportService;

	@Autowired
	private HsCodeAuthorityRepository hsCodeAuthorityRepository;

	@Autowired
	private BarCodeService barCodeService;

	@Autowired
	private InbCargoInformationRepository inbCargoInformationRepository;

	@Autowired
	private ScheduledTasksService scheduledTasksService;

	@Autowired
	private PushInventoryHistoryService pushInventoryHistoryService;

	@Autowired
	private PushInventoryService pushInventoryService;

//	@GetMapping("/moveSyncHistory")
//	public ApiResponse<?> moveSyncHistory() {
//		return new ApiResponse<>(TRUE, HttpStatus.OK.name(), "", pushInventoryHistoryService.savePushedRecords());
//	}
//
//	@GetMapping("/getVat")
//	public ApiResponse<?> getVat() {
//		return new ApiResponse<>(TRUE, HttpStatus.OK.name(), "", vatService.getLatest());
//	}
//
//	@GetMapping("/clearInvoice")
//	public ApiResponse<?> clearInvoice(@RequestParam(name = "id", required = false) Long id) {
//		invoiceDetailService.clearInvoice(id);
//		return new ApiResponse<>(TRUE, HttpStatus.OK.name(), "Invoice cleared");
//	}
//
//	@GetMapping("/clearReports")
//	public ApiResponse<?> clearReport(@RequestParam(name = "reportType", required = false) String reportType) {
//		if (reportType.equalsIgnoreCase("mismatch") || reportType.equalsIgnoreCase("generic")) {
//			reportService.clearReport(ReportType.valueOf(reportType.toUpperCase()));
//			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), reportType + " Report cleared");
//		}
//		return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), "Invalid report type : valid report types are 'mismatch' and 'generic'");
//	}
//
//	@GetMapping("/fetchUserCompanyInfo")
//	public JsonNode fetchUserCompanyInfo(@RequestParam("username") String username, @RequestParam("serviceId") String serviceId) throws CustomException, Exception {
//		return wmsApiService.fetchUserCompanyInfo(username, serviceId);
//	}
//
//	@GetMapping("/getReferenceCode")
//	public String getReferenceCode(@RequestParam("type") String type) throws Exception {
//		return wmsApiService.getReferenceCode(type);
//	}
//
//	@GetMapping("/getHsCodeMasterData")
//	public SyncHsCodeResponseRoot getHsCodeMasterData(@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate) {
//		SyncHsCodeRequestRoot requestRoot = new SyncHsCodeRequestRoot(new SyncHsCodeRequestItems(fromDate, toDate, "1"));
//		try {
//			return wmsApiService.getHsCodeMasterData(requestRoot);
//		} catch (CustomException e) {
//			return null;
//		}
//	}
//
	@GetMapping("/syncHsCode")
	public ApiResponse<?> syncHsCode(@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate) {
		ScheduledTasks task = scheduledTasksService.findByProcessName(ScheduledTaskName.SYNC_HSCODE);
		task.setStartedOn(LocalDateTime.now());
		task.setStatusType(StatusType.INPROCESS);
		scheduledTasksService.save(task);
		try {
			hsCodeService.syncMasterData(fromDate, toDate);
			task.setStatusType(StatusType.SUCCESS);
			task.setCompletedOn(LocalDateTime.now());
			task.setRuntype('M');
			scheduledTasksService.save(task);
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), "HS-Code sync completed");
		} catch (CustomException e) {
			task.setStatusType(StatusType.FAILED);
			task.setCompletedOn(LocalDateTime.now());
			task.setRuntype('M');
			scheduledTasksService.save(task);
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), MasterDataAppConstants.FAILED + " " + e.getMessage());
		}
	}
//
//	@GetMapping("/getHsCodeAuthority")
//	public Optional<HsCodeAuthority> getHsCodeAuthority(@RequestParam("id") long id) throws Exception {
//		return hsCodeAuthorityRepository.findById(id);
//	}
//
//	@GetMapping("/getHsCode")
//	public HsCode getHsCode(@RequestParam("id") long id) throws Exception {
//		return hsCodeService.getByHsCodeId(id);
//	}
//
//	@GetMapping("/getBarCode")
//	public BarCode getBarCode(@RequestParam("id") long id) throws Exception {
//		return barCodeService.getByBarCodeId(id);
//	}
//
//	@GetMapping("/getInbCargoInformation")
//	public Optional<InbCargoInformation> getInbCargoInformation(@RequestParam("id") long id) throws Exception {
//		return inbCargoInformationRepository.findById(id);
//	}
//
//	@GetMapping("/resyncPushInventory")
//	public ApiResponse<?> resyncPushInventory() {
//		try {
//			pushInventoryService.resyncPushInventory();
//		} catch (CustomException | XMLStreamException e) {
//			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), "", e.getMessage());
//		}
//		return new ApiResponse<>(TRUE, HttpStatus.OK.name(), "");
//	}

	////////////////////////////
	
//	@GetMapping("/inboundPushData")
//	public InboundPushResponseRoot inboundPushData(@RequestParam("id") long id) throws Exception {
//		Inbound save = inboundService.getInboundById(id);
//		InboundPushResponseRoot root = inboundService.inboundPushData(save, null, null, null);
//		boolean flag = true;
//		if (root != null) {
//			for (InboundPushResponseItems item : root.getItems()) {
//				if (item.getStatus().equalsIgnoreCase("ERROR"))
//					flag = false;
//			}
//		} else
//			flag = false;
//		save.setPushedToOci(flag);
//		inboundService.save(save);
//		return root;
//	}
//
//	@GetMapping("/returnGoodsPushData")
//	public ReturnGoodsPushResponseRoot returnGoodsPushData(@RequestParam("id") long id) throws Exception {
//		Inbound inb = inboundService.getInboundById(id);
//		ReturnGoodsPushResponseRoot root = inboundService.returnGoodsPushData(inb, null, null, null);
//		boolean flag = true;
//		if (root != null) {
//			for (ReturnGoodsPushResponseItems item : root.getItems()) {
//				if (item.getStatus().equalsIgnoreCase("ERROR"))
//					flag = false;
//			}
//		} else
//			flag = false;
//		inb.setPushedToOci(flag);
//		inboundService.save(inb);
//		return root;
//	}
//	
//	@GetMapping("/returnLocalGoodsPushData")
//	public ReturnLocalGoodsPushResponseRoot returnLocalGoodsPushData(@RequestParam("id") long id) throws Exception {
//		Inbound inb = inboundService.getInboundById(id);
//		ReturnLocalGoodsPushResponseRoot root = inboundService.returnLocalGoodsPushData(inb, null, null, null);
//		boolean flag = true;
//		if (root != null) {
//			for (ReturnLocalGoodsPushResponseItems item : root.getItems()) {
//				if (item.getStatus().equalsIgnoreCase("ERROR"))
//					flag = false;
//			}
//		} else
//			flag = false;
//		inb.setPushedToOci(flag);
//		inboundService.save(inb);
//		return root;
//	}
//
//	@GetMapping("/outboundPushData")
//	public OutboundPushResponseRoot outboundPushData(@RequestParam("id") long id) throws Exception {
//		Outbound outbound = outboundService.getOutboundById(id);
//		OutboundPushResponseRoot root = outboundService.outboundPushData(outbound);
//		boolean flag = true;
//		if (root != null) {
//			for (OutboundPushResponseItems item : root.getItems()) {
//				if (item.getStatus().equalsIgnoreCase("ERROR"))
//					flag = false;
//			}
//		} else
//			flag = false;
//		outbound.setPushedToOci(flag);
//		outboundService.save(outbound);
//		return root;
//	}

}

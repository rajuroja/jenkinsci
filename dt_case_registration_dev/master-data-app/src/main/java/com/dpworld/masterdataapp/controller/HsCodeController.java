package com.dpworld.masterdataapp.controller;

import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.FAILED;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.HSCODE_SEARCH_DETAIL_INVALID;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.HSCODE_SEARCH_SUCCESS;
import static java.lang.Boolean.TRUE;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dpworld.common.constants.DateFormates;
import com.dpworld.common.model.ApiResponse;
import com.dpworld.common.utils.CustomException;
import com.dpworld.common.utils.DateUtils;
import com.dpworld.masterdataapp.constants.MasterDataAppConstants;
import com.dpworld.masterdataapp.constants.ScheduledTaskName;
import com.dpworld.masterdataapp.model.request.HsCodeSearchRequest;
import com.dpworld.masterdataapp.model.response.HsCodeResponse;
import com.dpworld.masterdataapp.service.HsCodeService;
import com.dpworld.masterdataapp.service.ScheduledTasksService;
import com.dpworld.persistence.entity.ScheduledTasks;
import com.dpworld.persistence.entity.StatusType;

@RestController
@RequestMapping("/hscode")
public class HsCodeController {

	private static Logger logger = LoggerFactory.getLogger(HsCodeController.class);
	
	@Autowired
	private HsCodeService hsCodeService;
	
	@Autowired
	private ScheduledTasksService scheduledTasksService;
	
	@Autowired
	private DateUtils dateUtils;

	@PostMapping("/search")
	public ApiResponse<Object> hsCodeSearch(@RequestBody HsCodeSearchRequest hsCodeSearchRequest) {
		//logger.info("ENTRY:: hsCodeSearch:: Search for hscode : {}", hsCodeSearchRequest);
		try {
			if (hsCodeSearchRequest == null || hsCodeSearchRequest.getHsCode() == null || hsCodeSearchRequest.getHsCode().isEmpty()) {
				logger.error("ERROR:: hsCodeSearch : {}", HSCODE_SEARCH_DETAIL_INVALID);
				return new ApiResponse<>(false, HttpStatus.BAD_REQUEST.name(), HSCODE_SEARCH_DETAIL_INVALID);
			}
			List<HsCodeResponse> hsCodeResponse = hsCodeService.getByHsCodeContains(hsCodeSearchRequest.getHsCode());
			//logger.info("EXIT:: hsCodeSearch : {}", HSCODE_SEARCH_SUCCESS);
			return new ApiResponse<>(Boolean.TRUE, HttpStatus.OK.name(), HSCODE_SEARCH_SUCCESS, hsCodeResponse);
		} catch (Exception e) {
			logger.error("ERROR:: hsCodeSearch:: Error occured while searching Hscode : {}", e);
			return new ApiResponse<>(Boolean.TRUE, HttpStatus.INTERNAL_SERVER_ERROR.name(), FAILED + " HSCode Search Failed");
		}
	}

	@GetMapping("/syncHsCode")
	public ApiResponse<?> syncHsCode() {
		
		//logger.info("ENTRY:: syncHSCode:: Started syncing hscode.");
		
		Date today = new Date();
		ScheduledTasks task = scheduledTasksService.findByProcessName(ScheduledTaskName.SYNC_HSCODE);
//		Date fromDate = (task.getStatusType() == StatusType.SUCCESS ? today : task.getStartedOn());
		String fromDateStr = ""; //dateUtils.dateToString(fromDate, DateFormates.YYYYMMDD_HYPHEN) + "T00:00:00";
		String toDateStr = dateUtils.dateToString(today, DateFormates.YYYYMMDD_HYPHEN) + "T23:59:59";
		task.setStartedOn(LocalDateTime.now());
		task.setStatusType(StatusType.INPROCESS);
		scheduledTasksService.save(task);
		try {
			hsCodeService.syncMasterData(fromDateStr, toDateStr);
			task.setStatusType(StatusType.SUCCESS);
			task.setCompletedOn(LocalDateTime.now());
			//logger.info("ENTRY:: syncHSCode:: Syncing hscode completed successfully.");
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), "HS-Code sync completed");
		} catch (CustomException e) {
			task.setStatusType(StatusType.FAILED);
			task.setCompletedOn(LocalDateTime.now());
			logger.info("ERROR:: syncHSCode:: Exception occured in sync code. {}", e.getMessage());
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), MasterDataAppConstants.FAILED + " HSCode Sync Failed");
		} finally {
			task.setRuntype('M');
			scheduledTasksService.save(task);
		}
	}
}

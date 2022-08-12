package com.dpworld.masterdataapp.utility;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import javax.xml.stream.XMLStreamException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dpworld.common.constants.DateFormates;
import com.dpworld.common.utils.CustomException;
import com.dpworld.common.utils.DateUtils;
import com.dpworld.masterdataapp.constants.ScheduledTaskName;
import com.dpworld.masterdataapp.service.HsCodeService;
import com.dpworld.masterdataapp.service.PushInventoryHistoryService;
import com.dpworld.masterdataapp.service.PushInventoryService;
import com.dpworld.masterdataapp.service.ScheduledTasksService;
import com.dpworld.persistence.entity.ScheduledTasks;
import com.dpworld.persistence.entity.StatusType;

@Component
public class TmWmsScheduler {

	private static final Logger logger = LoggerFactory.getLogger(TmWmsScheduler.class);

	@Autowired
	private HsCodeService hsCodeService;

	@Autowired
	private ScheduledTasksService scheduledTasksService;

	@Autowired
	private DateUtils dateUtils;

	@Autowired
	private PushInventoryService pushInventoryService;

	@Autowired
	private PushInventoryHistoryService pushInventoryHistoryService;

	@Scheduled(cron = "00 00 00 * * *")
	public void syncHsCode() {
		//logger.info("ENTRY :: syncHsCode :: Sync Hs Code : Time : {} ", LocalDateTime.now());
		ScheduledTasks task = scheduledTasksService.findByProcessName(ScheduledTaskName.SYNC_HSCODE);
		task.setStartedOn(LocalDateTime.now());
		task.setStatusType(StatusType.INPROCESS);
		scheduledTasksService.save(task);
		try {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			Date yesterDay = cal.getTime();
			String fromDate = dateUtils.dateToString(yesterDay, DateFormates.YYYYMMDD_HYPHEN) + "T00:00:00";
			String toDate = dateUtils.dateToString(yesterDay, DateFormates.YYYYMMDD_HYPHEN) + "T23:59:59";
			hsCodeService.syncMasterData(fromDate, toDate);
			task.setStatusType(StatusType.SUCCESS);
			task.setCompletedOn(LocalDateTime.now());
			//logger.info("EXIT :: syncHsCode :: Sync Hs Code scuccessfully. Time : {} ", LocalDateTime.now());
		} catch (CustomException e) {
			logger.error("ERROR :: syncHsCode :: Error occured while syncing HS Code. {}", e.getMessage());
			task.setReason(e.getMessage());
			task.setStatusType(StatusType.FAILED);
			task.setCompletedOn(LocalDateTime.now());
		} finally {
			task.setRuntype('A');
			scheduledTasksService.save(task);
		}
	}

	// every 6 hours
	@Scheduled(cron = "00 00 */6 * * *")
	public void syncFailedPushInventory() {
		//logger.info("ENTRY :: syncFailedPushInventory :: Re-Sync failed push inventory : Time : {} ", LocalDateTime.now());
		ScheduledTasks task = scheduledTasksService.findByProcessName(ScheduledTaskName.SYNC_FAILED_PUSH_INVENTORY);
		task.setStartedOn(LocalDateTime.now());
		task.setStatusType(StatusType.INPROCESS);
		scheduledTasksService.save(task);
		try {
			pushInventoryService.resyncPushInventory();
			task.setStatusType(StatusType.SUCCESS);
			task.setCompletedOn(LocalDateTime.now());
			logger.error("EXIT :: syncFailedPushInventory :: Re-syncing failed push inventory successfully. {}", LocalDateTime.now());
		} catch (CustomException | XMLStreamException e) {
			logger.error("ERROR :: syncFailedPushInventory :: Error occured while re-syncing failed push inventory. {}", e.getMessage());
			task.setReason(e.getMessage());
			task.setStatusType(StatusType.FAILED);
			task.setCompletedOn(LocalDateTime.now());
		} finally {
			task.setRuntype('A');
			scheduledTasksService.save(task);
		}
	}
	
//	@Scheduled(cron = "00 */30 * * * *")
//	public void syncPushInventory() {
//		//logger.info("ENTRY :: Sync Push Inventory : Time : {} ", LocalDateTime.now());
//		ScheduledTasks task = scheduledTasksService.findByProcessName(ScheduledTaskName.SYNC_FAILED_PUSH_INVENTORY);
//		task.setStartedOn(LocalDateTime.now());
//		task.setStatusType(StatusType.INPROCESS);
//		scheduledTasksService.save(task);
//		try {
//			pushInventoryService.resyncPushInventory();
//			task.setStatusType(StatusType.SUCCESS);
//			task.setCompletedOn(LocalDateTime.now());
//		} catch (CustomException e) {
//			logger.error(e.getMessage());
//			task.setStatusType(StatusType.FAILED);
//			task.setCompletedOn(LocalDateTime.now());
//		} finally {
//			task.setRuntype('A');
//			scheduledTasksService.save(task);
//		}
//	}

	@Scheduled(cron = "00 00 00 * * *")
	public void copyPushedRecords() {
		//logger.info("ENTRY :: Move pushed inventory to history table. {}", LocalDateTime.now());
		pushInventoryHistoryService.savePushedRecords();
		//logger.info("EXIT :: Moved pushed inventory to history table.");
	}

}

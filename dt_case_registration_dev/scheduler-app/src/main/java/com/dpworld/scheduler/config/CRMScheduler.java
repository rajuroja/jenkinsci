package com.dpworld.scheduler.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CRMScheduler {

	private static final Logger LOGGER = LogManager.getLogger(CRMScheduler.class);

	@Value("${scheduler.fetchCRMdataEnabled}")
	private boolean fetchCRMdataEnabled;

	@Value("${scheduler.pushCRMdataEnabled}")
	private boolean pushCRMdataEnabled;

	@Value("${scheduler.sendSurveyEnabled}")
	private boolean sendSurveyEnabled;

	@Scheduled(initialDelay = 5 * 1000, fixedRateString = "${scheduler.fetchCRMdataInMills}") // todo uncomment for run
	public void fetchComplainDetailsFromCRM() {
		if (fetchCRMdataEnabled) {
			try {
				LOGGER.info("Fetching data from CRM by Scheduler Started");

				LOGGER.info("Fetching data From CRM by Scheduler Ended");
			} catch (Exception e) {
				LOGGER.error("error in fetchComplainDetailFromCRM", e);
			}
		} else {
			LOGGER.info("FetchComplainDetail scheduler is disabled");
		}
	}

	@Scheduled(initialDelay = 10 * 1000, fixedRateString = "${scheduler.pushCRMdataInMills}") // todo uncomment for run
	public void pushComplainDetailsToCRM() {
		if (pushCRMdataEnabled) {
			try {
				LOGGER.info("Sending data to CRM by Scheduler Started");

				LOGGER.info("Sending data to CRM by Scheduler Ended");
			} catch (Exception e) {
				LOGGER.error("error in push Complain Details to CRM", e);
			}
		} else {
			LOGGER.info("Sending data to CRM scheduler is disabled");
		}
	}

	@Scheduled(initialDelay = 15 * 1000, fixedRateString = "${scheduler.sendSurveyInMills}") // todo uncomment for run
	public void sendSurveyDetailsToCustomer() {
		if (sendSurveyEnabled) {
			try {
				LOGGER.info("Send Survey data to Customer Scheduler Started");

				LOGGER.info("Send Survey data to Customer Scheduler Ended");
			} catch (Exception e) {
				LOGGER.error("error in sendSurveyDetailsToCustomer", e);
			}
		} else {
			LOGGER.info("Send Survey Details To Customer scheduler is disabled");
		}
	}

}

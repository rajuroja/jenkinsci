package com.dpworld.scheduler.service;

import org.springframework.stereotype.Component;

@Component
public interface CRMService {
	<T> void fetchLocationDetailsFromWMS() throws Exception;

//	<T> void fetchLocationDetailsFromWMSXML(FacilityMaster facilityMaster) throws Exception;
}

package com.dpworld.scheduler.service.Impl;
/*
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dpworld.common.model.RequestDetail;
import com.dpworld.common.utils.WebserviceUtility;
import com.dpworld.persistence.entity.CompanyMaster;
import com.dpworld.persistence.entity.FacilityMaster;
import com.dpworld.persistence.entity.LocationMaster;
import com.dpworld.persistence.repository.CompanyMasterRepository;
import com.dpworld.persistence.repository.FacilityMasterRepository;
import com.dpworld.persistence.repository.LocationMasterRepository;
import com.dpworld.scheduler.constants.SchedulerAppConstants;
import com.dpworld.scheduler.model.WMSCommonResponse;
import com.dpworld.scheduler.model.WMSLocationResults;
import com.dpworld.scheduler.model.location.xml.LocationList;
import com.dpworld.scheduler.model.location.xml.Results;
import com.dpworld.scheduler.model.location.xml.WMSLocationXMLResponse;
import com.dpworld.scheduler.service.CRMService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class CRMServiceImpl implements CRMService {

	private static final Logger LOGGER = LogManager.getLogger(CRMServiceImpl.class);

	@Value("${oic.username}")
	private String wmsUsername;

	@Value("${oic.password}")
	private String wmsPassword;

	@Value("${oic.locationFetchURL}")
	private String wmsURLLocationFetch;

	@Override
	public void fetchLocationDetailsFromWMSXML(FacilityMaster facility) throws Exception {
		RequestDetail<Object> requestdetails = new RequestDetail<>();
		requestdetails.setUrl(wmsURLLocationFetch);
		requestdetails.setUsername(wmsUsername);
		requestdetails.setPassword(wmsPassword);
		int pageNumber = 1;
		List<WMSLocationXMLResponse> responseDetailList = new ArrayList<>();
		WMSLocationXMLResponse responseDetail = new WMSLocationXMLResponse();
		do {
			String xmlFormate = convertToXMLFormate(pageNumber,facility.getFacilityCode());
			requestdetails.setRequestBody(xmlFormate);
			LOGGER.info("feching Location data from pageNumber:- {}", pageNumber);
			Response response = WebserviceUtility.callPostWebServiceRequestXML(requestdetails);
			if (HttpStatus.OK.value() == response.getStatus() || HttpStatus.ACCEPTED.value() == response.getStatus()) {
				responseDetail = response.readEntity(WMSLocationXMLResponse.class);
				responseDetailList.add(responseDetail);
			}
			pageNumber++;
			LOGGER.info("callGETWebServiceRequestXML responseDetail {}", responseDetail.getNextPage());
		} while (responseDetail.getNextPage() != null && !responseDetail.getNextPage().isEmpty());
		// } while (responseDetail.getPageNbr() == 2);

		if(null!=responseDetailList && responseDetailList.size()>0)
		{	
			System.out.println(responseDetailList.size()+"responseDetailList size");
			
			List<LocationMaster> listOfLocation = mappingWMSXMLRespsoneToDBFormate(responseDetailList);
			
			List<LocationMaster> listOfLocationUniq= listOfLocation.stream().collect(collectingAndThen(
					toCollection(() -> new TreeSet<>(Comparator.comparing(LocationMaster::getLocationFullDisplayName))), ArrayList::new));
			
			if (!listOfLocation.isEmpty()) {
				compareAndSave(listOfLocationUniq,facility);
			}
		}
	}

	private String convertToXMLFormate(int pageNumber,String facilityCode) {
		StringBuilder xmlBuilder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xmlBuilder.append("<root>");
		xmlBuilder.append("<page_no>").append(pageNumber);
		xmlBuilder.append("</page_no>");
		
		if(facilityCode!=null && !facilityCode.isEmpty()) {
			xmlBuilder.append("<facility_code>").append(facilityCode);
			xmlBuilder.append("</facility_code>");
		}
		
		xmlBuilder.append("</root>");
		return xmlBuilder.toString();
	}

	private List<LocationMaster> mappingWMSXMLRespsoneToDBFormate(List<WMSLocationXMLResponse> responseDetailList) {
		List<LocationMaster> listOfLocationMaster = new ArrayList<>();
		LocationMaster locationMaster = null;
		for (WMSLocationXMLResponse wMSCommonResults : responseDetailList) {
			for (Results result : wMSCommonResults.getResults()) {
				List<LocationList> locationList = result.getListOfObjects();
				for (LocationList wMSLocationResults : locationList) {
					locationMaster = new LocationMaster();

					locationMaster.setWmsLocationId(Long.valueOf(wMSLocationResults.getId()));
					locationMaster.setUpdatedOn(wMSLocationResults.getModTs().toGregorianCalendar().getTime());
					locationMaster.setCreatedBy(wMSLocationResults.getCreateUser());
					locationMaster.setCreatedOn(wMSLocationResults.getCreateTs().toGregorianCalendar().getTime());
					locationMaster.setUpdatedBy(wMSLocationResults.getModUser());
					locationMaster.setIsActive(SchedulerAppConstants.isACTIVE);
					FacilityMaster facilityMaster = facMasterRepository
							.findByFacilityCode(wMSLocationResults.getFacilityId().getKey());
					locationMaster.setFacilityId(facilityMaster);

					CompanyMaster companyMaster = companyMasterRepository
							.findByCompanyName(wMSLocationResults.getDedicatedCompanyId().getKey());
					locationMaster.setCompany(companyMaster);

					locationMaster.setArea(wMSLocationResults.getArea());
					locationMaster.setRow(wMSLocationResults.getAisle()); // need to conform
					locationMaster.setBay(wMSLocationResults.getBay());
					locationMaster.setLevel(wMSLocationResults.getLevel());
					locationMaster.setPosition(wMSLocationResults.getPosition());
					locationMaster.setBin(wMSLocationResults.getBin());
					// locationMaster.setLocationFullDisplayName(wMSLocationResults.getLocnStr());
					locationMaster.setLocationFullDisplayName(wMSLocationResults.getBarcode());
					locationMaster.setLength(wMSLocationResults.getLength());
					locationMaster.setWidth(wMSLocationResults.getWidth());
					locationMaster.setHeight(wMSLocationResults.getHeight());
					locationMaster.setMaxContainer(Integer.valueOf(wMSLocationResults.getMaxUnits())); // need to
																										// conform

					int tobeCountedFlag = wMSLocationResults.getToBeCountedFlg().equalsIgnoreCase("False") ? 0 : 1;
					locationMaster.setToBeCountedFlag(tobeCountedFlag);
					Date toBeCountedAt = wMSLocationResults.getToBeCountedTs() != null
							? wMSLocationResults.getToBeCountedTs().toGregorianCalendar().getTime()
							: new Date();
					locationMaster.setToBeCountedTime(toBeCountedAt);
					locationMaster.setLockCode(wMSLocationResults.getLockCodeId());
					locationMaster.setPickSequence(wMSLocationResults.getPickSeq());
					Date lastCountAt = wMSLocationResults.getLastCountTs() != null
							? wMSLocationResults.getLastCountTs().toGregorianCalendar().getTime()
							: new Date();
					locationMaster.setLastCountedAt(lastCountAt);
					locationMaster.setLastCountedBy(wMSLocationResults.getLastCountUser());

					locationMaster.setMinVolume(wMSLocationResults.getMinVolume());
					locationMaster.setMaxVolume(wMSLocationResults.getMaxVolume());
					// locationMaster.setZone(wMSLocationResults.getAllocZone());

					// todo verify all these values
					// locationMaster.setLocationType(wMSLocationResults.getType_id().getKey());
					locationMaster.setContainerSize(wMSLocationResults.getCustField1());
					locationMaster.setCommodityType(wMSLocationResults.getCustField2());
					locationMaster.setTempWise(wMSLocationResults.getCustField3());
					locationMaster.setContainerStatusType(wMSLocationResults.getCustField4());
					locationMaster.setLocationYardType(wMSLocationResults.getAllocZone());

					listOfLocationMaster.add(locationMaster);
				}
			}
		}
		return listOfLocationMaster;
	}

	@Override
	public <T> void fetchLocationDetailsFromWMS() throws Exception {
		T responseDetail = null;
		RequestDetail<Object> requestdetails = new RequestDetail<>();
		requestdetails.setUrl(wmsURLLocationFetch);
		requestdetails.setUsername(wmsUsername);
		requestdetails.setPassword(wmsPassword);
		int pageNumber = 1;

		WMSCommonResponse<WMSLocationResults> yResponse = new WMSCommonResponse<>();
		List<WMSLocationResults> ymsresultList = new ArrayList<>(); // CHANGE AS map
		do {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("page", pageNumber);
			requestdetails.setQueryParams(map);
			LOGGER.info("feching Location data from pageNumber:- {}", pageNumber);
			Object shipmentDetailsResponse = WebserviceUtility.callGETWebServiceRequest(requestdetails);
			ObjectMapper mapper = new ObjectMapper();
			yResponse = mapper.convertValue(shipmentDetailsResponse,
					new TypeReference<WMSCommonResponse<WMSLocationResults>>() {
					});
			pageNumber++;
			if (null != yResponse.getResults() && !yResponse.getResults().isEmpty()) {
				ymsresultList.addAll(yResponse.getResults()); // put into map key as barcode and object
			}
		} while (yResponse.getNext_page() != null);
//map to list

		List<WMSLocationResults> ymsresultListUniq= ymsresultList.stream().collect(collectingAndThen(
				toCollection(() -> new TreeSet<>(Comparator.comparing(WMSLocationResults::getBarcode))), ArrayList::new));

		List<LocationMaster> listOfWMSLocation = mappingWMSRespsoneToDBFormate(ymsresultListUniq);
		if (!listOfWMSLocation.isEmpty()) {
			compareAndSave(listOfWMSLocation,null);
		}
	}

	private void compareAndSave(List<LocationMaster> listOfWMSLocation, FacilityMaster facilityId) {
		List<LocationMaster> locationToBeInserted = new ArrayList<>();
		List<LocationMaster> locationToBeUpdated = new ArrayList<>();
		//List<LocationMaster> locationInDB = locationRepository.findAll(); // NEED TO change only have wms location
		
		List<LocationMaster> locationInDB = locationRepository.findByFacilityIdAndIsActiveAndWmsLocationIdNotNull(facilityId,1); 
		
		Map<Long, LocationMaster> locationIdAndMasterMap = new HashMap<>();

		for (LocationMaster masterLocation : locationInDB) {
			Long key = masterLocation.getWmsLocationId();
			if (key != null && key!=0) {
				locationIdAndMasterMap.put(key, masterLocation);
			}
		}

		for (LocationMaster masterLocation : listOfWMSLocation) {
			Long key = masterLocation.getWmsLocationId();
			if (locationIdAndMasterMap.get(key) == null) {
				locationToBeInserted.add(masterLocation);
			} else {
				LocationMaster locationMasterFromDB = locationIdAndMasterMap.get(key);

				if ((masterLocation.getUpdatedOn().getTime() - locationMasterFromDB.getUpdatedOn().getTime()) > 10) {
					locationMasterFromDB.setArea(masterLocation.getArea());
					locationMasterFromDB.setBay(masterLocation.getBay());
					locationMasterFromDB.setHeight(masterLocation.getHeight());
					locationMasterFromDB.setLength(masterLocation.getLength());
					locationMasterFromDB.setLevel(masterLocation.getLevel());
					locationMasterFromDB.setPosition(masterLocation.getPosition());
					locationMasterFromDB.setBin(masterLocation.getBin());
					locationMasterFromDB.setCommodityType(masterLocation.getCommodityType());
					locationMasterFromDB.setLockCode(masterLocation.getLockCode());
					locationMasterFromDB.setMaxContainer(masterLocation.getMaxContainer());
					locationMasterFromDB.setMaxVolume(masterLocation.getMaxVolume());
					locationMasterFromDB.setMinVolume(masterLocation.getMinVolume());
					locationMasterFromDB.setPickSequence(masterLocation.getPickSequence());
					locationMasterFromDB.setRow(masterLocation.getRow());
					locationMasterFromDB.setWidth(masterLocation.getWidth());
					locationMasterFromDB.setZone(masterLocation.getZone());
					locationMasterFromDB.setWmsLocationId(masterLocation.getWmsLocationId());

					locationMasterFromDB.setCreatedBy(masterLocation.getCreatedBy());
					locationMasterFromDB.setCreatedOn(masterLocation.getCreatedOn());
					locationMasterFromDB.setUpdatedBy(masterLocation.getUpdatedBy());
					locationMasterFromDB.setUpdatedOn(masterLocation.getUpdatedOn());
					locationToBeUpdated.add(locationMasterFromDB);
				} // to do put condition avoid unwanted update

				locationIdAndMasterMap.remove(key);
			}
		}
		boolean refreshCatch = false;
		if (!locationToBeInserted.isEmpty()) {
			locationRepository.saveAll(locationToBeInserted);
			// add in catch flag= true
			refreshCatch = true;
			LOGGER.info("Total insterted location size{}", locationToBeInserted.size());
		} else {
			LOGGER.info("No new Location found  for insert");
		}

		if (!locationIdAndMasterMap.isEmpty()) {
			for (LocationMaster masterFacility : locationIdAndMasterMap.values()) {
				masterFacility.setIsActive(0);
				locationToBeUpdated.add(masterFacility);
			}
		}

		if (!locationToBeUpdated.isEmpty()) {
			locationRepository.saveAll(locationToBeUpdated);
			refreshCatch = true;
			LOGGER.info("Total updated location size{}", locationToBeUpdated.size());
		} else {
			LOGGER.info("No new Location found for update");
		}

		if (refreshCatch) {
			//locationCache.fillLocations();
		}
	}

	private List<LocationMaster> mappingWMSRespsoneToDBFormate(List<WMSLocationResults> wmsresultList) {
		List<LocationMaster> listOfLocationMaster = new ArrayList<>();
		LocationMaster locationMaster = null;
		for (WMSLocationResults wMSLocationResults : wmsresultList) {
			locationMaster = new LocationMaster();
			locationMaster.setArea(wMSLocationResults.getArea());
			locationMaster.setBay(wMSLocationResults.getBay());
			FacilityMaster facilityMaster = facMasterRepository
					.findByFacilityCode(wMSLocationResults.getFacility_id().getKey());
			locationMaster.setFacilityId(facilityMaster);
			CompanyMaster companyMaster = companyMasterRepository
					.findByCompanyName(wMSLocationResults.getDedicated_company_id().getKey());
			locationMaster.setCompany(companyMaster);
			locationMaster.setHeight(wMSLocationResults.getHeight());
			locationMaster.setLength(wMSLocationResults.getLength());
			locationMaster.setLevel(wMSLocationResults.getLevel());
			locationMaster.setPosition(locationMaster.getPosition());
			locationMaster.setBin(locationMaster.getBin());
			// locationMaster.setLocationFullDisplayName(wMSLocationResults.getLocn_str());
			locationMaster.setLocationFullDisplayName(wMSLocationResults.getBarcode());
			// locationMaster.setLocationType(wMSLocationResults.getType_id().getKey());
			locationMaster.setCommodityType(wMSLocationResults.getType_id().getKey());
			locationMaster.setLockCode(wMSLocationResults.getLock_code_id());
			locationMaster.setMaxContainer(Integer.valueOf(wMSLocationResults.getMax_units()));
			locationMaster.setMaxVolume(wMSLocationResults.getMax_volume());
			locationMaster.setMinVolume(wMSLocationResults.getMin_volume());
			locationMaster.setPickSequence(wMSLocationResults.getPick_seq());
			locationMaster.setRow(wMSLocationResults.getAisle());
			locationMaster.setWidth(wMSLocationResults.getWidth());
			locationMaster.setZone(wMSLocationResults.getAlloc_zone());
			locationMaster.setWmsLocationId(wMSLocationResults.getId());
			locationMaster.setUpdatedOn(wMSLocationResults.getMod_ts());
			locationMaster.setCreatedBy(wMSLocationResults.getCreate_user());
			locationMaster.setCreatedOn(wMSLocationResults.getCreate_ts());
			locationMaster.setUpdatedBy(wMSLocationResults.getMod_user());
			locationMaster.setIsActive(SchedulerAppConstants.isACTIVE);
			locationMaster.setLastCountedAt(wMSLocationResults.getLast_count_ts());
			locationMaster.setLastCountedBy(wMSLocationResults.getLast_count_user());
			int tobeCountedFlag = wMSLocationResults.isTo_be_counted_flg() ? 1 : 0;
			locationMaster.setToBeCountedFlag(tobeCountedFlag);
			listOfLocationMaster.add(locationMaster);
		}
		return listOfLocationMaster;

	}
}
*/
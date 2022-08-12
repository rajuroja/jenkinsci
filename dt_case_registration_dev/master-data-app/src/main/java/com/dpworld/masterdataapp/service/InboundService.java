package com.dpworld.masterdataapp.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.multipart.MultipartFile;

import com.dpworld.common.utils.CustomException;
import com.dpworld.masterdataapp.model.request.InboundRequest;
import com.dpworld.masterdataapp.model.request.SearchInventoryFilterRequest;
import com.dpworld.masterdataapp.model.response.InboundOnloadResponse;
import com.dpworld.persistence.entity.InbCargoInformation;
import com.dpworld.persistence.entity.Inbound;
import com.dpworld.persistence.enums.OperationType;
import com.dpworld.persistence.vo.PushApiResponse;
import com.dpworld.persistence.vo.SearchInventory;

public interface InboundService {

	Inbound getInboundById(long inboundId);

	List<Inbound> getInboundByIds(List<Long> inboundIds);

	List<Inbound> cancelInbound(List<Inbound> inboundList) throws CustomException;

	Inbound addOrUpdateInbound(InboundRequest inboundRequest, boolean isUpdate, List<MultipartFile> customDeclaration, List<MultipartFile> certificateOfOrigin, List<MultipartFile> commercialInvoice,
			List<MultipartFile> packingList) throws DataIntegrityViolationException, CustomException, Exception;

	InboundOnloadResponse getOnLoadData(String serviceId) throws CustomException, Exception;

	List<SearchInventory> filterSearchInventory(SearchInventoryFilterRequest vo, List<String> agentCodes, Boolean isCancel);

	StringBuilder validateAddInboundRequest(InboundRequest inboundRequest, Boolean isUpdate, List<MultipartFile> customDeclaration, List<MultipartFile> certificateOfOrigin,
			List<MultipartFile> commercialInvoice, List<MultipartFile> packingList);

	List<Inbound> findAllByBusinessCodeAndDateRange(String businessCode, Date fromDate, Date toDate);

	PushApiResponse inboundPushData(Inbound inbound, List<InbCargoInformation> oldCargoInformation, List<Long> deletedCargos, List<Long> deleteBarcodeDetails, OperationType operationType);

	PushApiResponse returnGoodsPushData(Inbound inbound, List<InbCargoInformation> oldCargoInformation, List<Long> deletedCargos, List<Long> deleteBarcodeDetails, OperationType operationType);

	PushApiResponse returnLocalGoodsPushData(Inbound inbound, List<InbCargoInformation> oldCargoInformation, List<Long> deletedCargos, List<Long> deleteBarcodeDetails, OperationType operationType);

	void save(Inbound save);

	Inbound updateDateByReferenceNumber(Inbound inbound, String createdOn) throws ParseException;

	Inbound findByReferenceNumber(String referenceNumber);

}

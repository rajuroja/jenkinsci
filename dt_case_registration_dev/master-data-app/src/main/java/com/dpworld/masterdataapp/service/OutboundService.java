package com.dpworld.masterdataapp.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.multipart.MultipartFile;

import com.dpworld.common.utils.CustomException;
import com.dpworld.masterdataapp.model.request.OutboundRequest;
import com.dpworld.masterdataapp.model.request.SearchInventoryFilterRequest;
import com.dpworld.masterdataapp.model.response.OutboundOnloadResponse;
import com.dpworld.persistence.entity.InvoiceDetail;
import com.dpworld.persistence.entity.OutbCargoInformation;
import com.dpworld.persistence.entity.Outbound;
import com.dpworld.persistence.enums.OperationType;
import com.dpworld.persistence.vo.PushApiResponse;
import com.dpworld.persistence.vo.SearchInventory;

public interface OutboundService {

	Outbound findById(Long id);

	List<SearchInventory> filterSearchInventory(SearchInventoryFilterRequest vo, List<String> agentCodes,
			Boolean isCancel);

	List<Outbound> findAll(Date fromDate, Date toDate, String businessCode, Boolean b);

	Outbound getOutboundById(long outboundId);

	List<Outbound> getOutboundByIds(List<Long> outboundIds);

	List<Outbound> cancelOutbound(List<Outbound> outboundList) throws CustomException;

	Outbound addOrUpdateOutbound(OutboundRequest outboundRequest, boolean isUpdate,
			List<MultipartFile> customDeclaration, List<MultipartFile> packingList, List<MultipartFile> salesInvoice)
			throws DataIntegrityViolationException, CustomException, Exception;

	OutboundOnloadResponse getOnLoadData(String serviceId) throws CustomException, Exception;

	StringBuilder validateAddOutboundRequest(OutboundRequest outboudRequest, boolean isUpdate,
			List<MultipartFile> customDeclaration, List<MultipartFile> packingList, List<MultipartFile> salesInvoice);

	void updateOutboundsWithInvoice(List<Long> outboundIds, InvoiceDetail invoice);

	void save(Outbound ob);

	PushApiResponse outboundPushData(Outbound outbound, List<OutbCargoInformation> oldCargoInformation, List<Long> deletedCargos, List<Long> deleteBarcodeDetails, OperationType operationType);

	Outbound updateDateByReferenceNumber(Outbound outbound, String createdOn) throws ParseException;

	Outbound findByReferenceNumber(String referenceNumber);
}

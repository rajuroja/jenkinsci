package com.dpworld.masterdataapp.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dpworld.common.utils.CustomException;
import com.dpworld.masterdataapp.model.request.GenerateInvoiceRequest;
import com.dpworld.masterdataapp.model.response.FileResponse;
import com.dpworld.masterdataapp.model.response.InvoiceCargoInfo;
import com.dpworld.persistence.entity.InvoiceDetail;
import com.dpworld.persistence.entity.Outbound;
import com.dpworld.persistence.vo.InvoiceDetailsListVo;

import net.sf.jasperreports.engine.JRException;

public interface InvoiceDetailService {

	List<InvoiceDetailsListVo> list();

	StringBuilder validateGenerateInvoiceRequest(GenerateInvoiceRequest vo);

	StringBuilder validateViewInvoiceRequest(GenerateInvoiceRequest vo);

	FileResponse generateInvoice(GenerateInvoiceRequest vo, String serviceId) throws JRException, CustomException, Exception;

	FileResponse viewInvoice(InvoiceDetail invoice, String format) throws JRException, CustomException, Exception;

	void save(InvoiceDetail invoice);

	InvoiceDetail createInvoice(Map<String, Object> parameters, List<InvoiceCargoInfo> cargoInfoList, List<Outbound> outboundList) throws Exception;

	List<InvoiceDetail> findAll();

	InvoiceDetail findById(Long id);

	List<InvoiceDetail> findByDateRangeOverlapAndBusinessCode(Date fromDate, Date toDate, String businessCode);

	void clearInvoice(Long id);

	List<InvoiceDetail> findAllByCreatedDateBetweenAndBusinessCodeAndIsCancel(Date fromDate, Date toDate, String businessCode, boolean b);

	List<InvoiceDetail> findAllByCompanyCodes(List<String> agentCodes);
	
	InvoiceDetail updateDateByReferenceNumber(InvoiceDetail invoice, String createdOn) throws ParseException;

	InvoiceDetail findByReferenceNumber(String referenceNumber);

}

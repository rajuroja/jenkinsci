package com.dpworld.persistence.vo;

import java.util.Date;

public interface GenericReportDataResponse {

	String getInvoiceReferenceNumber();

	Date getInvoiceCreatedDate();

	Integer getInvoicePages();

	String getConsigneeName();
	
	String getBusinessCode();

	String getCurrency();

	Double getGrandTotal();

	Double getCifValue();
	
	Long getHsCodeId();

	String getHsCode();

	String getHsCodeDescription();

	String getGoodsCondition();

	Double getTotalQuantity();
	
	Double getTotalWeight();
	
	String getInbDeclarationNumber();
	
	String getAuthorities();
}

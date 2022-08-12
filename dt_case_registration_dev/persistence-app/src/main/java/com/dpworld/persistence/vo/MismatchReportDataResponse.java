package com.dpworld.persistence.vo;

public interface MismatchReportDataResponse {
	
	String getInbDeclarationNumber();
	
	String getInbHsCode();
	
	String getInbBarCode();
	
	String getInbItemDescription();
	
	Double getInbTotalQuantity();
	
	Double getInbTotalWeight();

	Double getInbTotalCifValue();
	
	String getOutbInbDeclarationNumber();
	
	String getOutbHsCode();
	
	String getOutbBarCode();
	
	String getOutbItemDescription();
	
	Double getOutbTotalQuantity();
	
	Double getOutbTotalWeight();
	
	Double getOutbTotalCifValue();
	
	String getDocDate();
	
}

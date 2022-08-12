package com.dpworld.masterdataapp.model.response;

public class OutboundAddResponse {

	private Long outboundId;

	private String referenceNumber; // pending

	private String companyCode; // pending

	private Long returnOutboundRefId;

	private Boolean isReturn;

	private Boolean isCancel;

	public Long getOutboundId() {
		return outboundId;
	}

	public void setOutboundId(Long outboundId) {
		this.outboundId = outboundId;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public Long getReturnOutboundRefId() {
		return returnOutboundRefId;
	}

	public void setReturnOutboundRefId(Long returnOutboundRefId) {
		this.returnOutboundRefId = returnOutboundRefId;
	}

	public Boolean getIsReturn() {
		return isReturn;
	}

	public void setIsReturn(Boolean isReturn) {
		this.isReturn = isReturn;
	}

	public Boolean getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(Boolean isCancel) {
		this.isCancel = isCancel;
	}

}

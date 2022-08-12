package com.dpworld.masterdataapp.model.response;

public class InboundAddResponse {

	private Long inboundId;

	private String referenceNumber; // pending

	private String companyCode; // pending

	private Long returnInboundRefId;

	private String returnType;

	private Boolean isCancel;

	public Long getInboundId() {
		return inboundId;
	}

	public void setInboundId(Long inboundId) {
		this.inboundId = inboundId;
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

	public Long getReturnInboundRefId() {
		return returnInboundRefId;
	}

	public void setReturnInboundRefId(Long returnInboundRefId) {
		this.returnInboundRefId = returnInboundRefId;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public Boolean getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(Boolean isCancel) {
		this.isCancel = isCancel;
	}

}

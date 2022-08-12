package com.dpworld.masterdataapp.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OutbDeclarationInformationRequest {

	private long id;

	private Integer inbDeclarationTypeId;

	private String inbDeclarationNumber;

	private Integer outbDeclarationTypeId;

	private String outbDeclarationNumber;

	private String consigneeName;

	private String businessCode;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getInbDeclarationTypeId() {
		return inbDeclarationTypeId;
	}

	public void setInbDeclarationTypeId(Integer inbDeclarationTypeId) {
		this.inbDeclarationTypeId = inbDeclarationTypeId;
	}

	public String getInbDeclarationNumber() {
		return inbDeclarationNumber;
	}

	public void setInbDeclarationNumber(String inbDeclarationNumber) {
		this.inbDeclarationNumber = inbDeclarationNumber;
	}

	public Integer getOutbDeclarationTypeId() {
		return outbDeclarationTypeId;
	}

	public void setOutbDeclarationTypeId(Integer outbDeclarationTypeId) {
		this.outbDeclarationTypeId = outbDeclarationTypeId;
	}

	public String getOutbDeclarationNumber() {
		return outbDeclarationNumber;
	}

	public void setOutbDeclarationNumber(String outbDeclarationNumber) {
		this.outbDeclarationNumber = outbDeclarationNumber;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	@Override
	public String toString() {
		return "OutbDeclarationInformationRequest [id=" + id + ", inbDeclarationTypeId=" + inbDeclarationTypeId + ", inbDeclarationNumber=" + inbDeclarationNumber + ", outbDeclarationTypeId="
				+ outbDeclarationTypeId + ", outbDeclarationNumber=" + outbDeclarationNumber + ", consigneeName=" + consigneeName + ", businessCode=" + businessCode + "]";
	}

}

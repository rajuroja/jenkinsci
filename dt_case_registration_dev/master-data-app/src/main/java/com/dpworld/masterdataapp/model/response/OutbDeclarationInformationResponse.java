package com.dpworld.masterdataapp.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OutbDeclarationInformationResponse {

	private Integer id;

	private DeclarationTypeResponse inbDeclarationTypeId;

	private String inbDeclarationNumber;

	private DeclarationTypeResponse outbDeclarationTypeId;

	private String outbDeclarationNumber;

	private String businessCode;

	private String consigneeName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public DeclarationTypeResponse getInbDeclarationTypeId() {
		return inbDeclarationTypeId;
	}

	public void setInbDeclarationTypeId(DeclarationTypeResponse inbDeclarationTypeId) {
		this.inbDeclarationTypeId = inbDeclarationTypeId;
	}

	public String getInbDeclarationNumber() {
		return inbDeclarationNumber;
	}

	public void setInbDeclarationNumber(String inbDeclarationNumber) {
		this.inbDeclarationNumber = inbDeclarationNumber;
	}

	public DeclarationTypeResponse getOutbDeclarationTypeId() {
		return outbDeclarationTypeId;
	}

	public void setOutbDeclarationTypeId(DeclarationTypeResponse outbDeclarationTypeId) {
		this.outbDeclarationTypeId = outbDeclarationTypeId;
	}

	public String getOutbDeclarationNumber() {
		return outbDeclarationNumber;
	}

	public void setOutbDeclarationNumber(String outbDeclarationNumber) {
		this.outbDeclarationNumber = outbDeclarationNumber;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

}

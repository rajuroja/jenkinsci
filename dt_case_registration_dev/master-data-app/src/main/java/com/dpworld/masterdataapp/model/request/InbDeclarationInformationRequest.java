package com.dpworld.masterdataapp.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InbDeclarationInformationRequest {

	private long id;

	private Integer declarationTypeId;

	private String declarationNumber;

	private String localGoodsPassNumber;

	private String consigneeName;

	private String businessCode;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getDeclarationTypeId() {
		return declarationTypeId;
	}

	public void setDeclarationTypeId(Integer declarationTypeId) {
		this.declarationTypeId = declarationTypeId;
	}

	public String getDeclarationNumber() {
		return declarationNumber;
	}

	public void setDeclarationNumber(String declarationNumber) {
		this.declarationNumber = declarationNumber;
	}

	public String getLocalGoodsPassNumber() {
		return localGoodsPassNumber;
	}

	public void setLocalGoodsPassNumber(String localGoodsPassNumber) {
		this.localGoodsPassNumber = localGoodsPassNumber;
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
		return "InbDeclarationInformationRequest [id=" + id + ", declarationTypeId=" + declarationTypeId + ", declarationNumber=" + declarationNumber + ", localGoodsPassNumber=" + localGoodsPassNumber
				+ ", consigneeName=" + consigneeName + ", businessCode=" + businessCode + "]";
	}

}

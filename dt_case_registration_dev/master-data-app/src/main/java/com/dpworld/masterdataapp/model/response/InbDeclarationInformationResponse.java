package com.dpworld.masterdataapp.model.response;

import com.dpworld.persistence.entity.DeclarationType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InbDeclarationInformationResponse {

	private Long id;

	private DeclarationType declarationTypeId;

	private String declarationNumber;

	private String localGoodsPassNumber;

	private String consigneeName;

	private String businessCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DeclarationType getDeclarationTypeId() {
		return declarationTypeId;
	}

	public void setDeclarationTypeId(DeclarationType declarationTypeId) {
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

}

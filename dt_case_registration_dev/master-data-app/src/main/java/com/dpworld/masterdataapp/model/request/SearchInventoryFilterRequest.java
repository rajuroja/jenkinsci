package com.dpworld.masterdataapp.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchInventoryFilterRequest {

	private String declarationNumber;

	private String fromDate;

	private String toDate;

	private String typeOfInventory;

	private String hsCode;

	public String getDeclarationNumber() {
		return declarationNumber;
	}

	public void setDeclarationNumber(String declarationNumber) {
		this.declarationNumber = declarationNumber;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getTypeOfInventory() {
		return typeOfInventory;
	}

	public void setTypeOfInventory(String typeOfInventory) {
		this.typeOfInventory = typeOfInventory;
	}

	public String getHsCode() {
		return hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	@Override
	public String toString() {
		return "SearchInventoryFilterRequest [declarationNumber=" + declarationNumber + ", fromDate=" + fromDate + ", toDate=" + toDate + ", typeOfInventory=" + typeOfInventory + ", hsCode=" + hsCode
				+ "]";
	}

}
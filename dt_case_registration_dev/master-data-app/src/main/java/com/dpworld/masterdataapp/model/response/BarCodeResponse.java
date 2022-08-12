package com.dpworld.masterdataapp.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BarCodeResponse {

	public BarCodeResponse() {
		super();
	}

	public BarCodeResponse(Long barCodeId) {
		super();
		this.barCodeId = barCodeId;
	}

	private Long barCodeId;

	private String barCode;

	private String barCodeDescription;

	public Long getBarCodeId() {
		return barCodeId;
	}

	public void setBarCodeId(Long barCodeId) {
		this.barCodeId = barCodeId;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getBarCodeDescription() {
		return barCodeDescription;
	}

	public void setBarCodeDescription(String barCodeDescription) {
		this.barCodeDescription = barCodeDescription;
	}

}

package com.dpworld.masterdataapp.model.request;

public class BarCodeSearchRequest {

	private String barCode;

	private Long hsCodeId;

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public Long getHsCodeId() {
		return hsCodeId;
	}

	public void setHsCodeId(Long hsCodeId) {
		this.hsCodeId = hsCodeId;
	}

	@Override
	public String toString() {
		return "BarCodeSearchRequest [barCode=" + barCode + ", hsCodeId=" + hsCodeId + "]";
	}

}

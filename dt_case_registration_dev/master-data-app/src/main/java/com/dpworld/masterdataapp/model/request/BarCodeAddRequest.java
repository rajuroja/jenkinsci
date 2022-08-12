package com.dpworld.masterdataapp.model.request;

public class BarCodeAddRequest {

	private long barCodeId;
	
	private long hsCodeId;

	private String barCode;

	private String barCodeDescription;

	public long getHsCodeId() {
		return hsCodeId;
	}

	public void setHsCodeId(long hsCodeId) {
		this.hsCodeId = hsCodeId;
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

	public long getBarCodeId() {
		return barCodeId;
	}

	public void setBarCodeId(long barCodeId) {
		this.barCodeId = barCodeId;
	}

}

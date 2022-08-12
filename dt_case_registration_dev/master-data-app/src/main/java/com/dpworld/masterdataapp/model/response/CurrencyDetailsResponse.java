package com.dpworld.masterdataapp.model.response;

public class CurrencyDetailsResponse {

	private Integer currencyId;

	private String currencyName;

	public CurrencyDetailsResponse(Integer currencyId) {
		super();
		this.currencyId = currencyId;
	}

	public CurrencyDetailsResponse(Integer currencyId, String currencyName) {
		super();
		this.currencyId = currencyId;
		this.currencyName = currencyName;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

}

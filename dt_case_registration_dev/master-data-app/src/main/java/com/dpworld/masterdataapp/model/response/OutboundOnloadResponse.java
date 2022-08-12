package com.dpworld.masterdataapp.model.response;

import java.util.List;

import com.dpworld.masterdataapp.webServiceResponse.JsonResponseData.Agents;

public class OutboundOnloadResponse {

	private List<CountryDetailsResponse> countryDetails;
	private List<CurrencyDetailsResponse> currencyDetails;
	private List<DeclarationTypeResponse> declarationType;
	private List<GoodsConditionResponse> goodCondition;
	private List<MarketTypeResponse> marketType;
	private List<ShipmentSoldTypeResponse> shipmentSoldType;
	private List<Agents> agentDetailsList;
	public List<CountryDetailsResponse> getCountryDetails() {
		return countryDetails;
	}
	public void setCountryDetails(List<CountryDetailsResponse> countryDetails) {
		this.countryDetails = countryDetails;
	}
	public List<CurrencyDetailsResponse> getCurrencyDetails() {
		return currencyDetails;
	}
	public void setCurrencyDetails(List<CurrencyDetailsResponse> currencyDetails) {
		this.currencyDetails = currencyDetails;
	}
	public List<DeclarationTypeResponse> getDeclarationType() {
		return declarationType;
	}
	public void setDeclarationType(List<DeclarationTypeResponse> declarationType) {
		this.declarationType = declarationType;
	}
	public List<GoodsConditionResponse> getGoodCondition() {
		return goodCondition;
	}
	public void setGoodCondition(List<GoodsConditionResponse> goodCondition) {
		this.goodCondition = goodCondition;
	}
	public List<MarketTypeResponse> getMarketType() {
		return marketType;
	}
	public void setMarketType(List<MarketTypeResponse> marketType) {
		this.marketType = marketType;
	}
	public List<ShipmentSoldTypeResponse> getShipmentSoldType() {
		return shipmentSoldType;
	}
	public void setShipmentSoldType(List<ShipmentSoldTypeResponse> shipmentSoldType) {
		this.shipmentSoldType = shipmentSoldType;
	}
	public List<Agents> getAgentDetailsList() {
		return agentDetailsList;
	}
	public void setAgentDetailsList(List<Agents> agentDetailsList) {
		this.agentDetailsList = agentDetailsList;
	}


	
}

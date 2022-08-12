package com.dpworld.masterdataapp.webServiceResponse;

import java.util.List;

public class JsonAgentDetails {

	private String agentCode;

	private String agentName;

	private String vatNumber;

	private String agentType;

	private String agentTypeCode;

	private List<JsonAgentAddressDetails> addressList;

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getVatNumber() {
		return vatNumber;
	}

	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}

	public List<JsonAgentAddressDetails> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<JsonAgentAddressDetails> addressList) {
		this.addressList = addressList;
	}

	public String getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}

	public String getAgentTypeCode() {
		return agentTypeCode;
	}

	public void setAgentTypeCode(String agentTypeCode) {
		this.agentTypeCode = agentTypeCode;
	}

	@Override
	public String toString() {
		return "JsonAgentDetails [agentCode=" + agentCode + ", agentName=" + agentName + ", vatNumber=" + vatNumber + ", addressList=" + addressList + "]";
	}

}

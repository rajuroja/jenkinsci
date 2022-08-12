package com.dpworld.masterdataapp.webServiceResponse;

public class JsonAgentAddressDetails {

	private String addressType;

	private String address;

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "JsonAgentAddressDetails [addressType=" + addressType + ", address=" + address + "]";
	}

}

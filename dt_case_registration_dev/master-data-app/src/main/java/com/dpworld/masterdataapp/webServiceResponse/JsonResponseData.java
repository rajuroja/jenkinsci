package com.dpworld.masterdataapp.webServiceResponse;

import java.util.ArrayList;
import java.util.List;

public class JsonResponseData {

	private String code;
	private String status;
	private String message;
	private Data data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public static class Data {

		private List<Users> usersList = new ArrayList<>();;
		private List<Agents> agentsList = new ArrayList<>();

		public List<Users> getUsersList() {
			return usersList;
		}

		public void setUsersList(List<Users> usersList) {
			this.usersList = usersList;
		}

		public List<Agents> getAgentsList() {
			return agentsList;
		}

		public void setAgentsList(List<Agents> agentsList) {
			this.agentsList = agentsList;
		}

	}

	public static class Agents {

		private String agentType;
		private String agentTypeCode;
		private String agentCode;
		private String agentName;
		private String vatNumber;
		private List<Address> addressList = null;

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

		public List<Address> getAddressList() {
			return addressList;
		}

		public void setAddressList(List<Address> addressList) {
			this.addressList = addressList;
		}
	}

	public static class Address {

		private String adressType;
		private String address;

		public String getAdressType() {
			return adressType;
		}

		public void setAdressType(String adressType) {
			this.adressType = adressType;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}
	}

	public static class Users {

		private Integer partyId;
		private Integer companyPartyId;
		private String userName;
		private String firstName;
		private String lastName;
		private Integer globalPartyId;
		private Integer enquiryNumber;
		private String partyName;
		private String userType;
		private String groupId;
		private String designation;
		private String email;
		private String userSubType;
		private String mobileNumber;

		public Integer getPartyId() {
			return partyId;
		}

		public void setPartyId(Integer partyId) {
			this.partyId = partyId;
		}

		public Integer getCompanyPartyId() {
			return companyPartyId;
		}

		public void setCompanyPartyId(Integer companyPartyId) {
			this.companyPartyId = companyPartyId;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public Integer getGlobalPartyId() {
			return globalPartyId;
		}

		public void setGlobalPartyId(Integer globalPartyId) {
			this.globalPartyId = globalPartyId;
		}

		public Integer getEnquiryNumber() {
			return enquiryNumber;
		}

		public void setEnquiryNumber(Integer enquiryNumber) {
			this.enquiryNumber = enquiryNumber;
		}

		public String getPartyName() {
			return partyName;
		}

		public void setPartyName(String partyName) {
			this.partyName = partyName;
		}

		public String getUserType() {
			return userType;
		}

		public void setUserType(String userType) {
			this.userType = userType;
		}

		public String getGroupId() {
			return groupId;
		}

		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}

		public String getDesignation() {
			return designation;
		}

		public void setDesignation(String designation) {
			this.designation = designation;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getUserSubType() {
			return userSubType;
		}

		public void setUserSubType(String userSubType) {
			this.userSubType = userSubType;
		}

		public String getMobileNumber() {
			return mobileNumber;
		}

		public void setMobileNumber(String mobileNumber) {
			this.mobileNumber = mobileNumber;
		}
	}

}

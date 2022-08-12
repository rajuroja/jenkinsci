package com.dpworld.masterdataapp.webServiceResponse;

public class JsonUserDetails {

	private String partyId;

	private String companyPartyId;

	private String userName;

	private String title;

	private String firstName;

	private String lastName;

	private String globalPartyId;

	private String enquiryNumber;

	private String partyName;

	private String userType;

	private String groupId;

	private String designation;

	private String email;

	private String userSubType;

	private String mobileNumber;

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getCompanyPartyId() {
		return companyPartyId;
	}

	public void setCompanyPartyId(String companyPartyId) {
		this.companyPartyId = companyPartyId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getGlobalPartyId() {
		return globalPartyId;
	}

	public void setGlobalPartyId(String globalPartyId) {
		this.globalPartyId = globalPartyId;
	}

	public String getEnquiryNumber() {
		return enquiryNumber;
	}

	public void setEnquiryNumber(String enquiryNumber) {
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

	@Override
	public String toString() {
		return "JsonUserDetails [partyId=" + partyId + ", companyPartyId=" + companyPartyId + ", userName=" + userName
				+ ", title=" + title + ", firstName=" + firstName + ", lastName=" + lastName + ", globalPartyId="
				+ globalPartyId + ", enquiryNumber=" + enquiryNumber + ", partyName=" + partyName + ", userType=" + userType
				+ ", groupId=" + groupId + ", designation=" + designation + ", email=" + email + ", userSubType="
				+ userSubType + ", mobileNumber=" + mobileNumber + "]";
	}

}

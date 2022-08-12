package com.dpworld.masterdataapp.model.request;

public class ValidateUserServiceRequest {

	private String userName;
	private String serviceId;
	private String url;

	public ValidateUserServiceRequest() {
		super();
	}

	public ValidateUserServiceRequest(String userName, String serviceId, String url) {
		super();
		this.userName = userName;
		this.serviceId = serviceId;
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "ValidateUserServiceRequest [userName=" + userName + ", serviceId=" + serviceId + ", url=" + url + "]";
	}

}

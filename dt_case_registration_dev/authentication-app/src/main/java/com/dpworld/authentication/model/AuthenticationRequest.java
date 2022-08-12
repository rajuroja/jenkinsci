package com.dpworld.authentication.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;

public class AuthenticationRequest implements Serializable {

	private static final long serialVersionUID = 8114270454926828474L;

	@NotEmpty(message = "Username must not be empty.")
	private String username;

	@NotEmpty(message = "Company Id must not be empty.")
	private String companyId;

	@NotEmpty(message = "License number must not be empty.")
	private String licenseNumber;

	private LocalDateTime lastLoginTime;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public LocalDateTime getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(LocalDateTime lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@Override
	public String toString() {
		return "AuthenticationRequest [username=" + username + ", companyId=" + companyId + ", licenseNumber=" + licenseNumber + "]";
	}

}

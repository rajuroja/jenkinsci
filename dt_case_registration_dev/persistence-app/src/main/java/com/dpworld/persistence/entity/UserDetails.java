package com.dpworld.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "USER_DETAILS", indexes = @Index(columnList = "USERNAME, COMPANY_ID, LICENSE_NUMBER"))
public class UserDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "USER_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user_details")
	@SequenceGenerator(name = "seq_user_details", sequenceName = "seq_user_details", allocationSize = 1)
	private long userId;

	@Column(name = "USERNAME", length = 30)
	private String username;

	@Column(name = "COMPANY_ID", length = 30)
	private String companyId;

	@Column(name = "LICENSE_NUMBER", length = 30)
	private String licenseNumber;

	@Column(name = "LAST_LOGIN_TIME")
	private LocalDateTime lastLoginTime;

	public UserDetails() {
		super();
	}

	public UserDetails(String username, String companyId, String licenseNumber, LocalDateTime lastLoginTime) {
		super();
		this.username = username;
		this.companyId = companyId;
		this.licenseNumber = licenseNumber;
		this.lastLoginTime = lastLoginTime;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

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

}
package com.dpworld.persistence.entity;

public enum StatusType {

	SUCCESS("Success"), FAILED("Failed"), ACTIVE("Active"), CANCELLED("Cancelled"), INPROCESS("InProcess"), PENDING("Pending");

	private String status;

	private StatusType(String status) {

		this.status = status;
	}

	public String getStatus() {
		return status;
	}

}

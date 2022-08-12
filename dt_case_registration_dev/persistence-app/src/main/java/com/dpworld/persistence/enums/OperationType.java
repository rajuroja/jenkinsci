package com.dpworld.persistence.enums;

public enum OperationType {

	CREATE("Create"), UPDATE("Update"), CANCEL("Cancel");

	private String operationType;

	private OperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getOperationType() {
		return operationType;
	}

}

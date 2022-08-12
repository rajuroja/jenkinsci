package com.dpworld.persistence.entity;

public enum ReportType {

	MISMATCH("Mismatch"), GENERIC("Generic");

	private String reportType;

	private ReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getReportType() {
		return reportType;
	}

}

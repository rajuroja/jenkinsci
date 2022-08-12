package com.dpworld.masterdataapp.model.request;

public class GenerateReportRequest {

	private String reportType;

	private Long reportId;

	private String fromDate;

	private String toDate;

	private String format;

	private String businessCode;

	private String consigneeName;

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	@Override
	public String toString() {
		return "GenerateReportRequest [reportType=" + reportType + ", reportId=" + reportId + ", fromDate=" + fromDate + ", toDate=" + toDate + ", format=" + format + ", businessCode=" + businessCode
				+ ", consigneeName=" + consigneeName + "]";
	}

}
package com.dpworld.persistence.vo;

import java.util.Date;

public interface ReportListResponse {
	
	Long getReportId();

	Date getCreatedDate();
	
	String getReferenceNumber();
	
	String getReportType();
}

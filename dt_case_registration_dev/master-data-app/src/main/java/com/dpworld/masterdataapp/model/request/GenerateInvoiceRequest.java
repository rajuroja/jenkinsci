package com.dpworld.masterdataapp.model.request;

import com.dpworld.masterdataapp.webServiceResponse.JsonResponseData.Agents;

public class GenerateInvoiceRequest {

	private String fromDate;

	private String toDate;

	private String format;

	private String businessCode;

	private Long invoiceId;

	private Agents agents;

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
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

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public Agents getAgents() {
		return agents;
	}

	public void setAgents(Agents agents) {
		this.agents = agents;
	}

	@Override
	public String toString() {
		return "GenerateInvoiceRequest [fromDate=" + fromDate + ", toDate=" + toDate + ", format=" + format + ", businessCode=" + businessCode + ", invoiceId=" + invoiceId + ", agents=" + agents
				+ "]";
	}

}

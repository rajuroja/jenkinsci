package com.dpworld.masterdataapp.syncHsCode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class SyncHsCodeRequestItems {

	public SyncHsCodeRequestItems() {
		super();
	}

	public SyncHsCodeRequestItems(String fromDate, String toDate, String page) {
		super();
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.page = page;
	}

	@XmlElement(name = "From_dateTime")
	private String fromDate;

	@XmlElement(name = "To_dateTime")
	private String toDate;

	@XmlElement(name = "Page")
	private String page;

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

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	@Override
	public String toString() {
		return "XMLItemsRequest [fromDate=" + fromDate + ", toDate=" + toDate + ", page=" + page + "]";
	}

}
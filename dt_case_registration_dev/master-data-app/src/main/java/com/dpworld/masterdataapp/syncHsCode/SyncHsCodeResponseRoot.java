package com.dpworld.masterdataapp.syncHsCode;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "itemsresponse", namespace = "http://www.dpworld.com")
@XmlAccessorType(XmlAccessType.FIELD)
public class SyncHsCodeResponseRoot {

	@XmlElement(name = "items")
	List<SyncHsCodeResponseItems> items;

	@XmlElement(name = "From_dateTime")
	private String fromDateTime;

	@XmlElement(name = "To_dateTime")
	private String toDateTime;

	@XmlElement(name = "Page")
	private String page;

	@XmlElement(name = "page_count")
	private String pageCount;

	@XmlElement(name = "next_page")
	private String nextPage;

	@XmlElement(name = "result_count")
	private String resultCount;

	public List<SyncHsCodeResponseItems> getItems() {
		return items;
	}

	public void setItems(List<SyncHsCodeResponseItems> items) {
		this.items = items;
	}

	public String getFromDateTime() {
		return fromDateTime;
	}

	public void setFromDateTime(String fromDateTime) {
		this.fromDateTime = fromDateTime;
	}

	public String getToDateTime() {
		return toDateTime;
	}

	public void setToDateTime(String toDateTime) {
		this.toDateTime = toDateTime;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getPageCount() {
		return pageCount;
	}

	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}

	public String getNextPage() {
		return nextPage;
	}

	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}

	public String getResultCount() {
		return resultCount;
	}

	public void setResultCount(String resultCount) {
		this.resultCount = resultCount;
	}

	@Override
	public String toString() {
		return "SyncHsCodeResponseRoot [items=" + items + ", fromDateTime=" + fromDateTime + ", toDateTime=" + toDateTime + ", page=" + page + ", pageCount=" + pageCount + ", nextPage=" + nextPage
				+ ", resultCount=" + resultCount + "]";
	}

}

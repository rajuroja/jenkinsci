package com.dpworld.masterdataapp.pushBarcode;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "itemspostresponse", namespace = "http://www.dpworld.com")
@XmlAccessorType(XmlAccessType.FIELD)
public class BarcodePushResponseRoot {

	@XmlElement(name = "status")
	private String status;

	@XmlElement(name = "message")
	private String message;

	@XmlElement(name = "items")
	private List<BarcodePushResponseItems> items;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<BarcodePushResponseItems> getItems() {
		return items;
	}

	public void setItems(List<BarcodePushResponseItems> items) {
		this.items = items;
	}

}

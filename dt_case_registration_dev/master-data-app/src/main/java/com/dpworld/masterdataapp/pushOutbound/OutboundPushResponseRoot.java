package com.dpworld.masterdataapp.pushOutbound;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "itemsoutboundresponse", namespace = "http://www.dpworld.org")
@XmlAccessorType(XmlAccessType.FIELD)
public class OutboundPushResponseRoot {

	@XmlElement(name = "status")
	private String status;

	@XmlElement(name = "message")
	private String message;

	@XmlElement(name = "items")
	private List<OutboundPushResponseItems> items;

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

	public List<OutboundPushResponseItems> getItems() {
		return items;
	}

	public void setItems(List<OutboundPushResponseItems> items) {
		this.items = items;
	}

}

package com.dpworld.masterdataapp.pushInbound;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "itemsupdateResponse", namespace = "http://www.dpworld.org")
@XmlAccessorType(XmlAccessType.FIELD)
public class InboundPushResponseRoot {

	@XmlElement(name = "items")
	private List<InboundPushResponseItems> items;

	public List<InboundPushResponseItems> getItems() {
		return items;
	}

	public void setItems(List<InboundPushResponseItems> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "InboundPushResponseRoot [items=" + items + "]";
	}

}

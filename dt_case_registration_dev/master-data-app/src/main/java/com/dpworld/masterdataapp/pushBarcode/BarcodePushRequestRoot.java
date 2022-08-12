package com.dpworld.masterdataapp.pushBarcode;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "itemspostrequest", namespace = "http://www.dpworld.com")
@XmlAccessorType(XmlAccessType.FIELD)
public class BarcodePushRequestRoot {

	@XmlElement(name = "items")
	private List<BarcodePushRequestItems> items;

	public List<BarcodePushRequestItems> getItems() {
		return items;
	}

	public void setItems(List<BarcodePushRequestItems> items) {
		this.items = items;
	}

}

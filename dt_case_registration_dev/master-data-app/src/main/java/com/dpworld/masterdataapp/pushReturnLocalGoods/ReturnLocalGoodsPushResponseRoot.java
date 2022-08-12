package com.dpworld.masterdataapp.pushReturnLocalGoods;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "outputresponse", namespace = "http://www.dpworld.org")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReturnLocalGoodsPushResponseRoot {

	@XmlElement(name = "items")
	private List<ReturnLocalGoodsPushResponseItems> items;

	public List<ReturnLocalGoodsPushResponseItems> getItems() {
		return items;
	}

	public void setItems(List<ReturnLocalGoodsPushResponseItems> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "ReturnLocalGoodsPushResponseRoot [items=" + items + "]";
	}

}
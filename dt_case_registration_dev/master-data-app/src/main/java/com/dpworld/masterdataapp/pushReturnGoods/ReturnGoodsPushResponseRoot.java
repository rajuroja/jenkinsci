package com.dpworld.masterdataapp.pushReturnGoods;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "outputresponse", namespace = "http://www.dpworld.org")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReturnGoodsPushResponseRoot {

	@XmlElement(name = "items")
	private List<ReturnGoodsPushResponseItems> items;

	public List<ReturnGoodsPushResponseItems> getItems() {
		return items;
	}

	public void setItems(List<ReturnGoodsPushResponseItems> items) {
		this.items = items;
	}

}
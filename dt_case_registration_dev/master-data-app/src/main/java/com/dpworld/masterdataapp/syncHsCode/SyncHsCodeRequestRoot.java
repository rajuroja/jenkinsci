package com.dpworld.masterdataapp.syncHsCode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "itemsrequest", namespace = "http://www.dpworld.com")
@XmlAccessorType(XmlAccessType.FIELD)
public class SyncHsCodeRequestRoot {

	public SyncHsCodeRequestRoot() {
		super();
	}

	public SyncHsCodeRequestRoot(SyncHsCodeRequestItems items) {
		super();
		this.items = items;
	}

	@XmlElement(name = "items")
	private SyncHsCodeRequestItems items;

	public SyncHsCodeRequestItems getItems() {
		return items;
	}

	public void setItems(SyncHsCodeRequestItems items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "SyncHsCodeRequestRoot [items=" + items + "]";
	}

}

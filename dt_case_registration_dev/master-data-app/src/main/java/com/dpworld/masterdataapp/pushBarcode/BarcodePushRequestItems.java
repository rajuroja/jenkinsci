package com.dpworld.masterdataapp.pushBarcode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class BarcodePushRequestItems {

	public BarcodePushRequestItems() {
		super();
	}

	public BarcodePushRequestItems(String hsCode, String barCode, String itemDescription, String hscodeDuty) {
		super();
		this.hsCode = hsCode;
		this.barCode = barCode;
		this.itemDescription = itemDescription;
		this.hscodeDuty = hscodeDuty;
	}

	@XmlElement(name = "hs_code")
	private String hsCode;

	@XmlElement(name = "part_b")
	private String barCode;

	@XmlElement(name = "item_description")
	private String itemDescription;

	@XmlElement(name = "description_3")
	private String hscodeDuty;

	public String getHsCode() {
		return hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getHscodeDuty() {
		return hscodeDuty;
	}

	public void setHscodeDuty(String hscodeDuty) {
		this.hscodeDuty = hscodeDuty;
	}

}
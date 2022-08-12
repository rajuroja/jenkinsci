package com.dpworld.masterdataapp.pushReturnGoods;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "inputrequest", namespace = "http://www.dpworld.org")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReturnGoodsPushRequestRoot {

	@XmlElement(name = "facility_id__code")
	private String facilityIdCode;

	@XmlElement(name = "barcode")
	private String barcode;

	@XmlElement(name = "items")
	private List<ReturnGoodsPushRequestItems> items;

	public String getFacilityIdCode() {
		return facilityIdCode;
	}

	public void setFacilityIdCode(String facilityIdCode) {
		this.facilityIdCode = facilityIdCode;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public List<ReturnGoodsPushRequestItems> getItems() {
		return items;
	}

	public void setItems(List<ReturnGoodsPushRequestItems> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "XMLReturnGoodsPushRequestRoot [facilityIdCode=" + facilityIdCode + ", barcode=" + barcode + ", items=" + items + "]";
	}

}

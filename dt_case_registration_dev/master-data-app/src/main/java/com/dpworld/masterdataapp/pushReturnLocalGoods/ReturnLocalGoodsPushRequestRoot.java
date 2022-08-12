package com.dpworld.masterdataapp.pushReturnLocalGoods;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "inputrequest", namespace = "http://www.dpworld.org")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReturnLocalGoodsPushRequestRoot {

	@XmlElement(name = "facility_id__code")
	private String facilityIdCode;

	@XmlElement(name = "barcode")
	private String barcode;

	@XmlElement(name = "items")
	private List<ReturnLocalGoodsPushRequestItems> items;

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

	public List<ReturnLocalGoodsPushRequestItems> getItems() {
		return items;
	}

	public void setItems(List<ReturnLocalGoodsPushRequestItems> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "ReturnLocalGoodsPushRequestRoot [facilityIdCode=" + facilityIdCode + ", barcode=" + barcode + ", items=" + items + "]";
	}

}

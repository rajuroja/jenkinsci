package com.dpworld.masterdataapp.syncHsCode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class SyncHsCodeResponseItems {

	@XmlElement(name = "hs_code")
	private String hsCode;

	@XmlElement(name = "bar_code")
	private String barCode;

	@XmlElement(name = "item_description")
	private String itemDescription;

	@XmlElement(name = "authority")
	private String authority;

	@XmlElement(name = "authority_code")
	private String authorityCode;

	@XmlElement(name = "duty")
	private String duty;

	@XmlElement(name = "Active")
	private String active;

	private String hsCodeAuthorityCode;

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

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getAuthorityCode() {
		return authorityCode;
	}

	public void setAuthorityCode(String authorityCode) {
		this.authorityCode = authorityCode;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public void setHsCodeAuthorityCode(String hsCodeAuthorityCode) {
		this.hsCodeAuthorityCode = hsCodeAuthorityCode;
	}

	public String getHsCodeAuthorityCode() {
		return this.hsCode + "/" + this.authorityCode;
	}

	@Override
	public String toString() {
		return "SyncHsCodeResponseItems [hsCode=" + hsCode + ", barCode=" + barCode + ", authorityCode=" + authorityCode + ", duty=" + duty + ", active=" + active + "]";
	}

}

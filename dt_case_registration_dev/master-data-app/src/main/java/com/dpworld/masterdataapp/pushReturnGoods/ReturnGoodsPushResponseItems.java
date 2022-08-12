package com.dpworld.masterdataapp.pushReturnGoods;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReturnGoodsPushResponseItems {

	@XmlElement(name = "hs_code")
	private String hsCode;

	@XmlElement(name = "bar_code")
	private String barCode;

	@XmlElement(name = "status")
	private String status;

	@XmlElement(name = "Message")
	private String message;

	@XmlElement(name = "retrurngoods_Reference_Number")
	private String returnGoodReferenceNumber;

	@XmlElement(name = "Outbound_Declaration_Number")
	private String outboundDeclarationNumber;

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

	public String getReturnGoodReferenceNumber() {
		return returnGoodReferenceNumber;
	}

	public void setReturnGoodReferenceNumber(String returnGoodReferenceNumber) {
		this.returnGoodReferenceNumber = returnGoodReferenceNumber;
	}

	public String getOutboundDeclarationNumber() {
		return outboundDeclarationNumber;
	}

	public void setOutboundDeclarationNumber(String outboundDeclarationNumber) {
		this.outboundDeclarationNumber = outboundDeclarationNumber;
	}

}

package com.dpworld.masterdataapp.pushReturnLocalGoods;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReturnLocalGoodsPushResponseItems {

	@XmlElement(name = "status")
	private String status;

	@XmlElement(name = "Message")
	private String message;

	@XmlElement(name = "retrurnlocalgoods_Reference_Number")
	private String returnLocalGoodReferenceNumber;

	@XmlElement(name = "Outbound_Declaration_Number")
	private String outboundDeclarationNumber;

	@XmlElement(name = "hs_code")
	private String hsCode;

	@XmlElement(name = "bar_code")
	private String barCode;

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

	public String getReturnLocalGoodReferenceNumber() {
		return returnLocalGoodReferenceNumber;
	}

	public void setReturnLocalGoodReferenceNumber(String returnLocalGoodReferenceNumber) {
		this.returnLocalGoodReferenceNumber = returnLocalGoodReferenceNumber;
	}

	public String getOutboundDeclarationNumber() {
		return outboundDeclarationNumber;
	}

	public void setOutboundDeclarationNumber(String outboundDeclarationNumber) {
		this.outboundDeclarationNumber = outboundDeclarationNumber;
	}

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

	@Override
	public String toString() {
		return "ReturnLocalGoodsPushResponseItems [status=" + status + ", message=" + message + ", returnLocalGoodReferenceNumber=" + returnLocalGoodReferenceNumber + ", outboundDeclarationNumber="
				+ outboundDeclarationNumber + ", hsCode=" + hsCode + ", barCode=" + barCode + "]";
	}

}

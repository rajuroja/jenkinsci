package com.dpworld.masterdataapp.pushOutbound;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class OutboundPushResponseItems {

	@XmlElement(name = "bar_code")
	private String barCode;

	@XmlElement(name = "hs_code")
	private String hsCode;

	@XmlElement(name = "status")
	private String status;

	@XmlElement(name = "Message")
	private String message;

	@XmlElement(name = "Indound_Declaration_Number")
	private String indoundDeclarationNumber;

	@XmlElement(name = "Outbound_Reference_Number")
	private String outboundReferenceNumber;

	@XmlElement(name = "Outbound_Declaration_Number")
	private String outboundDeclarationNumber;

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barcode) {
		this.barCode = barcode;
	}

	public String getHsCode() {
		return hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
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

	public String getIndoundDeclarationNumber() {
		return indoundDeclarationNumber;
	}

	public void setIndoundDeclarationNumber(String indoundDeclarationNumber) {
		this.indoundDeclarationNumber = indoundDeclarationNumber;
	}

	public String getOutboundReferenceNumber() {
		return outboundReferenceNumber;
	}

	public void setOutboundReferenceNumber(String outboundReferenceNumber) {
		this.outboundReferenceNumber = outboundReferenceNumber;
	}

	public String getOutboundDeclarationNumber() {
		return outboundDeclarationNumber;
	}

	public void setOutboundDeclarationNumber(String outboundDeclarationNumber) {
		this.outboundDeclarationNumber = outboundDeclarationNumber;
	}

}
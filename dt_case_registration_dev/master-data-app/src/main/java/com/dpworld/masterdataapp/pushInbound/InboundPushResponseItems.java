package com.dpworld.masterdataapp.pushInbound;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class InboundPushResponseItems {

	@XmlElement(name = "hs_code")
	private String hsCode;

	@XmlElement(name = "bar_code")
	private String barCode;

	@XmlElement(name = "status")
	private String status;

	@XmlElement(name = "Message")
	private String message;
	
	@XmlElements({@XmlElement(name = "Inbound_Declaration_Number"), @XmlElement(name = "Indound_Declaration_Number")})
	private String inboundDeclarationNumber;

	@XmlElement(name = "Inbound_Reference_Number")
	private String inboundReferenceNumber;

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

	public String getInboundDeclarationNumber() {
		return inboundDeclarationNumber;
	}

	public void setInboundDeclarationNumber(String inboundDeclarationNumber) {
		this.inboundDeclarationNumber = inboundDeclarationNumber;
	}

	public String getInboundReferenceNumber() {
		return inboundReferenceNumber;
	}

	public void setInboundReferenceNumber(String inboundReferenceNumber) {
		this.inboundReferenceNumber = inboundReferenceNumber;
	}

}
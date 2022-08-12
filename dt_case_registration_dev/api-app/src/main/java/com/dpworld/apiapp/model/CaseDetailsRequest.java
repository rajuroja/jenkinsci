package com.dpworld.apiapp.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CaseDetailsRequest {

	@NotEmpty(message = "Name must not be empty.")
	private String firstName;

	@Email(message = "Email is invalid")
	private String email;
	
	@NotEmpty(message="Mobile number is invalid")
	private String mobNo;
    
	@NotNull
	int caseType;
	
	@NotNull
	int complainType;
	
	@NotNull
	int portType;
	
	@NotNull
	int terminal;
	
	String vesselName;

	String rotationNum;
	
	String invoiceNum;
	
	String containerNum;
//	
//	String desc;
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobNo() {
		return mobNo;
	}

	public void setMobNo(String mobNo) {
		this.mobNo = mobNo;
	}

	public int getCaseType() {
		return caseType;
	}

	public void setCaseType(int caseType) {
		this.caseType = caseType;
	}

	public int getComplainType() {
		return complainType;
	}

	public void setComplainType(int complainType) {
		this.complainType = complainType;
	}

	public int getPortType() {
		return portType;
	}

	public void setPortType(int portType) {
		this.portType = portType;
	}

	public int getTerminal() {
		return terminal;
	}

	public void setTerminal(int terminal) {
		this.terminal = terminal;
	}

	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public String getRotationNum() {
		return rotationNum;
	}

	public void setRotationNum(String rotationNum) {
		this.rotationNum = rotationNum;
	}

	public String getInvoiceNum() {
		return invoiceNum;
	}

	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}

	public String getContainerNum() {
		return containerNum;
	}

	public void setContainerNum(String containerNum) {
		this.containerNum = containerNum;
	}
	 /* 
	 * public String getDesc() { return desc; }
	 * 
	 * public void setDesc(String desc) { this.desc = desc; }
	 */

}

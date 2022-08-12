package com.dpworld.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "DT_CASE_REGISTRATION_DETAILS")
public class CaseRegistrationDetails extends CommonFields {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_dt_case_reg_details")
	@SequenceGenerator(name = "seq_dt_case_reg_details", sequenceName = "seq_dt_case_reg_details", allocationSize = 1)
	@Column(name = "CASE_REGISTRATION_ID")
	private long caseRegId;

	@Column(name = "CRM_CASE_REF_ID")
	private long complainId;

	@Column(name = "CONTACT_PERSON")
	private String firstName;

	@Column(name = "CONTACT_EMAIL")
	private String email;

	@Column(name = "CONTACT_NUMBER")
	private String mobNo;

	@Column(name = "CASE_TYPE")
	int caseType;

//	@Column(name = "COMPLAIN_TYPE")
//	int complainType;

	@Column(name = "PORT")
	int portType;

	@Column(name = "TERMINAL")
	int terminal;

	@Column(name = "VESSEL_NAME")
	String vesselName;

	@Column(name = "ROTATION_NUMBER")
	String rotationNum;

	@Column(name = "INVOICE_NUMBER")
	String invoiceNum;

	@Column(name = "CONTAINER_NUMBER")
	String containerNum;
//	
//	@Column(name = "DESCRIPTION")
//	String desc;

	public long getComplainId() {
		return complainId;
	}

	public void setComplainId(long complainId) {
		this.complainId = complainId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public long getCaseRegId() {
		return caseRegId;
	}

	public void setCaseRegId(long caseRegId) {
		this.caseRegId = caseRegId;
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

	/*
	 * public int getComplainType() { return complainType; }
	 * 
	 * public void setComplainType(int complainType) { this.complainType =
	 * complainType; }
	 */

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

package com.dpworld.persistence.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.springframework.format.annotation.DateTimeFormat;

@Entity @OptimisticLocking(type = OptimisticLockType.ALL) @DynamicUpdate
@Table(name = "MISMATCH_REPORT_DATA")
public class MismatchReportData implements Serializable {

	// CAUTION : CHANGING NAMES OF FIELDS IN THIS ENTITY WILL NEED CHANGE NAMES IN
	// mismatch_report.jrxml

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MISMATCH_REPORT_DATA_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mismatch_report_data")
	@SequenceGenerator(name = "seq_mismatch_report_data", sequenceName = "seq_mismatch_report_data", allocationSize = 1)
	private long mismatchReportDataId;

	@ManyToOne
	@JoinColumn(name = "REPORT_ID")
	private Report report;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date docDate;

	private String inbDeclarationNumber;

	private String inbBarCode;

	private String inbDescription;

	private String inbHsCode;

	private Double inbQuantity;

	private Double inbWeight;

	private Double inbCifValue;

	private String outbInbDeclarationNumber;

	private String outbHsCode;

	private String outbBarCode;

	private String outbDescription;

	private Double outbQuantity;

	private Double outbWeight;

	private Double outbCifValue;

	public long getMismatchReportDataId() {
		return mismatchReportDataId;
	}

	public void setMismatchReportDataId(long mismatchReportDataId) {
		this.mismatchReportDataId = mismatchReportDataId;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public Date getDocDate() {
		return docDate;
	}

	public void setDocDate(Date docDate) {
		this.docDate = docDate;
	}

	public String getInbBarCode() {
		return inbBarCode;
	}

	public void setInbBarCode(String inbBarCode) {
		this.inbBarCode = inbBarCode;
	}

	public String getInbDescription() {
		return inbDescription;
	}

	public void setInbDescription(String inbDescription) {
		this.inbDescription = inbDescription;
	}

	public String getInbHsCode() {
		return inbHsCode;
	}

	public void setInbHsCode(String inbHsCode) {
		this.inbHsCode = inbHsCode;
	}

	public Double getInbQuantity() {
		return inbQuantity;
	}

	public void setInbQuantity(Double inbQuantity) {
		this.inbQuantity = inbQuantity;
	}

	public Double getInbWeight() {
		return inbWeight;
	}

	public void setInbWeight(Double inbWeight) {
		this.inbWeight = inbWeight;
	}

	public Double getInbCifValue() {
		return inbCifValue;
	}

	public void setInbCifValue(Double inbCifValue) {
		this.inbCifValue = inbCifValue;
	}

	public String getOutbHsCode() {
		return outbHsCode;
	}

	public void setOutbHsCode(String outbHsCode) {
		this.outbHsCode = outbHsCode;
	}

	public String getOutbBarCode() {
		return outbBarCode;
	}

	public void setOutbBarCode(String outbBarCode) {
		this.outbBarCode = outbBarCode;
	}

	public String getOutbDescription() {
		return outbDescription;
	}

	public void setOutbDescription(String outbDescription) {
		this.outbDescription = outbDescription;
	}

	public Double getOutbQuantity() {
		return outbQuantity;
	}

	public void setOutbQuantity(Double outbQuantity) {
		this.outbQuantity = outbQuantity;
	}

	public Double getOutbWeight() {
		return outbWeight;
	}

	public void setOutbWeight(Double outbWeight) {
		this.outbWeight = outbWeight;
	}

	public Double getOutbCifValue() {
		return outbCifValue;
	}

	public void setOutbCifValue(Double outbCifValue) {
		this.outbCifValue = outbCifValue;
	}
	
	public String getInbDeclarationNumber() {
		return inbDeclarationNumber;
	}

	public void setInbDeclarationNumber(String inbDeclarationNumber) {
		this.inbDeclarationNumber = inbDeclarationNumber;
	}

	public String getOutbInbDeclarationNumber() {
		return outbInbDeclarationNumber;
	}

	public void setOutbInbDeclarationNumber(String outbInbDeclarationNumber) {
		this.outbInbDeclarationNumber = outbInbDeclarationNumber;
	}

}

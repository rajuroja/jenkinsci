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

@Entity
@Table(name = "GENERIC_REPORT_DATA")
public class GenericReportData implements Serializable {

	// CAUTION : CHANGING NAMES OF FIELDS IN THIS ENTITY WILL NEED CHANGE NAMES IN
	// generic_report.jrxml

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "GENERIC_REPORT_DATA_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generic_report_data")
	@SequenceGenerator(name = "seq_generic_report_data", sequenceName = "seq_generic_report_data", allocationSize = 1)
	private long genericReportDataId;

	@ManyToOne
	@JoinColumn(name = "REPORT_ID")
	private Report report;

	private String invoiceReferenceNumber;

	private Date invoiceCreatedDate;

	private Integer invoicePages;

	private String consigneeName;

	private String currency;

	private String hsCode;

	private String hsCodeDescription;

	private String goodsCondition;

	private Double totalQuantity;

	private Double grandTotal;

	private Double cifAmount;

	private Double totalWeight;

	private String inbDeclarationNumber;

	private String authorities;

	public long getGenericReportDataId() {
		return genericReportDataId;
	}

	public void setGenericReportDataId(long genericReportDataId) {
		this.genericReportDataId = genericReportDataId;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public String getInvoiceReferenceNumber() {
		return invoiceReferenceNumber;
	}

	public void setInvoiceReferenceNumber(String invoiceReferenceNumber) {
		this.invoiceReferenceNumber = invoiceReferenceNumber;
	}

	public Date getInvoiceCreatedDate() {
		return invoiceCreatedDate;
	}

	public void setInvoiceCreatedDate(Date invoiceCreatedDate) {
		this.invoiceCreatedDate = invoiceCreatedDate;
	}

	public Integer getInvoicePages() {
		return invoicePages;
	}

	public void setInvoicePages(Integer invoicePages) {
		this.invoicePages = invoicePages;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getHsCode() {
		return hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	public String getHsCodeDescription() {
		return hsCodeDescription;
	}

	public void setHsCodeDescription(String hsCodeDescription) {
		this.hsCodeDescription = hsCodeDescription;
	}

	public String getGoodsCondition() {
		return goodsCondition;
	}

	public void setGoodsCondition(String goodsCondition) {
		this.goodsCondition = goodsCondition;
	}

	public Double getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(Double totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public Double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public Double getCifAmount() {
		return cifAmount;
	}

	public void setCifAmount(Double cifAmount) {
		this.cifAmount = cifAmount;
	}

	public Double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public String getInbDeclarationNumber() {
		return inbDeclarationNumber;
	}

	public void setInbDeclarationNumber(String inbDeclarationNumber) {
		this.inbDeclarationNumber = inbDeclarationNumber;
	}

	public String getAuthorities() {
		return authorities == null ? "" : authorities;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

	@Override
	public String toString() {
		return "GenericReportData [genericReportDataId=" + genericReportDataId + ", report=" + report + ", invoiceReferenceNumber=" + invoiceReferenceNumber + ", invoiceCreatedDate="
				+ invoiceCreatedDate + ", invoicePages=" + invoicePages + ", consigneeName=" + consigneeName + ", currency=" + currency + ", hsCode=" + hsCode + ", hsCodeDescription="
				+ hsCodeDescription + ", goodsCondition=" + goodsCondition + ", totalQuantity=" + totalQuantity + ", grandTotal=" + grandTotal + ", cifAmount=" + cifAmount + ", totalWeight="
				+ totalWeight + ", inbDeclarationNumber=" + inbDeclarationNumber + ", authorities=" + authorities + "]";
	}

}

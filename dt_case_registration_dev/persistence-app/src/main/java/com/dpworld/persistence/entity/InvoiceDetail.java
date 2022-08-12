package com.dpworld.persistence.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@OptimisticLocking(type = OptimisticLockType.DIRTY)
@DynamicUpdate
@Table(name = "INVOICE_DETAILS", indexes = { @Index(columnList = "REFERENCE_NUMBER"), @Index(columnList = "COMPANY_CODE") })
public class InvoiceDetail extends ECommonFields {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "INVOICE_DETAIL_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_invoice_details")
	@SequenceGenerator(name = "seq_invoice_details", sequenceName = "seq_invoice_details", allocationSize = 1)
	private Long invoiceDetailId;

	@Column(name = "REFERENCE_NUMBER", length = 20)
	private String referenceNumber;

	@Column(name = "TRANSACTION_NUMBER", length = 20)
	private String transactionNumber; // Pending

	@Column(name = "FROM_DATE")
	private Date fromDate;

	@Column(name = "TO_DATE")
	private Date toDate;

	@Column(name = "VAT_VALUE")
	private Double vatValue;

	@Column(name = "COMPANY_CODE")
	private String companyCode;

	@Column(name = "COMPANY_NAME", length = 255)
	private String companyName;

	@Column(name = "COMPANY_ADDRESS", length = 255)
	private String companyAddress;

	@Column(name = "TRN_NUMBER", length = 20)
	private String trnNumber;

	@JsonBackReference
	@OneToMany(mappedBy = "invoiceDetail", fetch = FetchType.LAZY)
	private List<InvoiceOutbounds> invoiceOutbounds;

	@Column(name = "SUB_TOTAL")
	private Double subTotal;

	@Column(name = "GRAND_TOTAL")
	private Double grandTotal;

	@Column(name = "TOTAL_SOLD_AMOUNT")
	private Double totalSoldAmount;

	@Column(name = "TOTAL_DISCOUNT_AMOUNT")
	private Double totalDiscountAmount;

	@Column(name = "TOTAL_DUTY_AMOUNT")
	private Double totalDutyAmount;

	@Column(name = "TOTAL_WEIGHT")
	private Double totalWeight;

	@Column(name = "TOTAL_QUANTITY")
	private Double totalQuantity;

	@Column(name = "INVOICE_PAGES")
	private Integer invoicePages;

	@JsonIgnore
	@OneToMany(mappedBy = "invoiceDetail", fetch = FetchType.LAZY)
	private List<Outbound> outbounds;

	private String currency;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public List<Outbound> getOutbounds() {
		return outbounds;
	}

	public void setOutbounds(List<Outbound> outbounds) {
		this.outbounds = outbounds;
	}

	public Long getInvoiceDetailId() {
		return invoiceDetailId;
	}

	public void setInvoiceDetailId(Long invoiceDetailId) {
		this.invoiceDetailId = invoiceDetailId;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Double getVatValue() {
		return vatValue;
	}

	public void setVatValue(Double vatValue) {
		this.vatValue = vatValue;
	}

	public Boolean getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(Boolean isCancel) {
		this.isCancel = isCancel;
	}

	public List<InvoiceOutbounds> getInvoiceOutbounds() {
		return invoiceOutbounds;
	}

	public void setInvoiceOutbounds(List<InvoiceOutbounds> invoiceOutbounds) {
		this.invoiceOutbounds = invoiceOutbounds;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getTrnNumber() {
		return trnNumber;
	}

	public void setTrnNumber(String trnNumber) {
		this.trnNumber = trnNumber;
	}

	public Double getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(Double totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public Integer getInvoicePages() {
		return invoicePages;
	}

	public void setInvoicePages(Integer invoicePages) {
		this.invoicePages = invoicePages;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	public Double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public Double getTotalSoldAmount() {
		return totalSoldAmount;
	}

	public void setTotalSoldAmount(Double totalSoldAmount) {
		this.totalSoldAmount = totalSoldAmount;
	}

	public Double getTotalDiscountAmount() {
		return totalDiscountAmount;
	}

	public void setTotalDiscountAmount(Double totalDiscountAmount) {
		this.totalDiscountAmount = totalDiscountAmount;
	}

	public Double getTotalDutyAmount() {
		return totalDutyAmount;
	}

	public void setTotalDutyAmount(Double totalDutyAmount) {
		this.totalDutyAmount = totalDutyAmount;
	}

	public Double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}

}
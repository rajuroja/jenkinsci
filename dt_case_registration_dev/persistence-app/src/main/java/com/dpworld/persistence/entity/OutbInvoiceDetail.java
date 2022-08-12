package com.dpworld.persistence.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

@Entity @OptimisticLocking(type = OptimisticLockType.ALL) @DynamicUpdate
@Table(name = "OUTB_INVOICE_DETAILS")
public class OutbInvoiceDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_outb_invoice_details")
	@SequenceGenerator(name = "seq_outb_invoice_details", sequenceName = "seq_outb_invoice_details", allocationSize = 1)
	private long id;

	@JsonBackReference
	@NotNull
	@OneToOne
	@JoinColumn(name = "OUTB_CARGO_INFORMATION_ID", referencedColumnName = "ID")
	private OutbCargoInformation outbCargoInformationId;

	@Column(name = "INVOICE_NUMBER", length = 10)
	private String invoiceNumber;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "dd-MM-yyyy")
	@Column(name = "INVOICE_DATE")
	private LocalDate invoiceDate;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "CURRENCY_ID", referencedColumnName = "CURRENCY_ID")
	private CurrencyDetails currencyId;

	@Column(name = "CIF_VALUE")
	private Double cifValue;

	@Column(name = "DISCOUNT_PERCENT")
	private Double discountPercent;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public OutbCargoInformation getOutbCargoInformationId() {
		return outbCargoInformationId;
	}

	public void setOutbCargoInformationId(OutbCargoInformation outbCargoInformationId) {
		this.outbCargoInformationId = outbCargoInformationId;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public LocalDate getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(LocalDate invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public CurrencyDetails getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(CurrencyDetails currencyId) {
		this.currencyId = currencyId;
	}

	public Double getCifValue() {
		return cifValue;
	}

	public void setCifValue(Double cifValue) {
		this.cifValue = cifValue;
	}

	public Double getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(Double discountPercent) {
		this.discountPercent = discountPercent;
	}

}

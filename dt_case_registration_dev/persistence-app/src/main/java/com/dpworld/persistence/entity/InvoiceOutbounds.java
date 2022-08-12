package com.dpworld.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

@Entity @OptimisticLocking(type = OptimisticLockType.ALL) @DynamicUpdate
@Table(name = "INVOICE_OUTBOUNDS")
public class InvoiceOutbounds implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "INVOICE_OUTBOUNDS_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_invoice_outb")
	@SequenceGenerator(name = "seq_invoice_outb", sequenceName = "seq_invoice_outb", allocationSize = 1)
	private Long invoiceOutboundsId;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "INVOICE_ID", referencedColumnName = "INVOICE_DETAIL_ID")
	private InvoiceDetail invoiceDetail;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "HS_CODE_ID", referencedColumnName = "HS_CODE_ID")
	private HsCode hsCode;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "INVENTORY_TYPE", referencedColumnName = "SHIPMENT_SOLD_TYPE_ID")
	private ShipmentSoldType shipmentSoldType;

	@Column(name = "QUANTITY")
	private Double quantity;

	@Column(name = "SOLD_VALUE")
	private Double soldValue;

	@Column(name = "DISCOUNT_VALUE")
	private Double discountValue;

	@Column(name = "DUTY_VALUE")
	private Double dutyValue;

	@Column(name = "NET_AMOUNT")
	private Double netAmount;

	public Long getInvoiceOutboundsId() {
		return invoiceOutboundsId;
	}

	public void setInvoiceOutboundsId(Long invoiceOutboundsId) {
		this.invoiceOutboundsId = invoiceOutboundsId;
	}

	public InvoiceDetail getInvoiceDetail() {
		return invoiceDetail;
	}

	public void setInvoiceDetail(InvoiceDetail invoiceDetail) {
		this.invoiceDetail = invoiceDetail;
	}

	public HsCode getHsCode() {
		return hsCode;
	}

	public void setHsCode(HsCode hsCode) {
		this.hsCode = hsCode;
	}

	public ShipmentSoldType getShipmentSoldType() {
		return shipmentSoldType;
	}

	public void setShipmentSoldType(ShipmentSoldType shipmentSoldType) {
		this.shipmentSoldType = shipmentSoldType;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getSoldValue() {
		return soldValue;
	}

	public void setSoldValue(Double soldValue) {
		this.soldValue = soldValue;
	}

	public Double getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(Double discountValue) {
		this.discountValue = discountValue;
	}

	public Double getDutyValue() {
		return dutyValue;
	}

	public void setDutyValue(Double dutyValue) {
		this.dutyValue = dutyValue;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

}
package com.dpworld.persistence.vo;

import java.util.Date;

import com.dpworld.persistence.entity.ReturnType;

public class SearchInventory {

	public SearchInventory(Long id, String referenceNumber, Date createdOn, String inventoryType, Long invoiceDetailId) {
		super();
		this.id = id;
		this.referenceNumber = referenceNumber;
		this.createdOn = createdOn;
		this.inventoryType = inventoryType;
		this.invoiceDetailId = invoiceDetailId;
	}

	public SearchInventory(Long id, String referenceNumber, Date createdOn, String inventoryType, ReturnType returnType) {
		super();
		this.id = id;
		this.referenceNumber = referenceNumber;
		this.createdOn = createdOn;
		this.inventoryType = inventoryType;
		this.returnType = returnType;
	}

	private Long id;

	private String referenceNumber;

	private Date createdOn;

	private String inventoryType;

	private ReturnType returnType;

	private Long invoiceDetailId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getInventoryType() {
		return inventoryType;
	}

	public void setInventoryType(String inventoryType) {
		this.inventoryType = inventoryType;
	}

	public ReturnType getReturnType() {
		return returnType;
	}

	public void setReturnType(ReturnType returnType) {
		this.returnType = returnType;
	}

	public Long getInvoiceDetailId() {
		return invoiceDetailId;
	}

	public void setInvoiceDetailId(Long invoiceDetailId) {
		this.invoiceDetailId = invoiceDetailId;
	}

}

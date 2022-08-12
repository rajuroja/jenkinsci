package com.dpworld.masterdataapp.model.response;

public class InvoiceCargoInfo {

	public InvoiceCargoInfo() {
		super();
	}

	public InvoiceCargoInfo(String hsCode, String inventoryType, Double quantity, Double soldAmount, Double discountAmount, Double dutyAmount, Double netAmount, String itemDescription, Double weight) {
		super();
		this.hsCode = hsCode;
		this.inventoryType = inventoryType;
		this.quantity = quantity;
		this.soldAmount = soldAmount;
		this.discountAmount = discountAmount;
		this.dutyAmount = dutyAmount;
		this.netAmount = netAmount;
		this.itemDescription = itemDescription;
		this.weight = weight;
	}

	private String hsCode = "";

	private String inventoryType = "";

	private Double quantity = 0d;

	private Double soldAmount = 0d;

	private Double discountAmount = 0d;

	private Double dutyAmount = 0d;

	private Double netAmount = 0d;

	private String itemDescription = "";

	private Double weight = 0d;

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getHsCode() {
		return hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	public String getInventoryType() {
		return inventoryType;
	}

	public void setInventoryType(String inventoryType) {
		this.inventoryType = inventoryType;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getSoldAmount() {
		return soldAmount;
	}

	public void setSoldAmount(Double soldAmount) {
		this.soldAmount = soldAmount;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Double getDutyAmount() {
		return dutyAmount;
	}

	public void setDutyAmount(Double dutyAmount) {
		this.dutyAmount = dutyAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	@Override
	public String toString() {
		return "InvoiceCargoInfo [hsCode=" + hsCode + ", inventoryType=" + inventoryType + ", quantity=" + quantity + ", soldAmount=" + soldAmount + ", discountAmount=" + discountAmount
				+ ", dutyAmount=" + dutyAmount + ", netAmount=" + netAmount + ", itemDescription=" + itemDescription + "]";
	}

}

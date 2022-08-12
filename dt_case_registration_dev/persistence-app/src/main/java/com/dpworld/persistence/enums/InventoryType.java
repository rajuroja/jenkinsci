package com.dpworld.persistence.enums;

public enum InventoryType {

	INBOUND("Inbound"), OUTBOUND("Outbound"), RETURNGOODS("ReturnGoods"), RETURNLOCALGOODS("ReturnLocalGoods"), BARCODE("BarCode");

	private String inventoryType;

	private InventoryType(String inventoryType) {
		this.inventoryType = inventoryType;
	}

	public String getInventoryType() {
		return inventoryType;
	}

}

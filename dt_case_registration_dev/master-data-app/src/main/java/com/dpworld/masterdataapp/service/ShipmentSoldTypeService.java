package com.dpworld.masterdataapp.service;

import java.util.List;

import com.dpworld.persistence.entity.ShipmentSoldType;

public interface ShipmentSoldTypeService {

	ShipmentSoldType findByShipmentSoldType(String inventoryType);

	ShipmentSoldType getByShipmentSoldTypeId(Byte shipmentSoldTypeId);
	
	List<ShipmentSoldType> getAll();

}

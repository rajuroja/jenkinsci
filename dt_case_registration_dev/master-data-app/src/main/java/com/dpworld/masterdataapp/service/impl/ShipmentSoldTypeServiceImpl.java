package com.dpworld.masterdataapp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dpworld.masterdataapp.service.ShipmentSoldTypeService;
import com.dpworld.persistence.entity.ShipmentSoldType;
import com.dpworld.persistence.repository.ShipmentSoldTypeRepository;

@Service
public class ShipmentSoldTypeServiceImpl implements ShipmentSoldTypeService {

	@Autowired
	private ShipmentSoldTypeRepository shipmentSoldTypeRepository;

	@Override
	public ShipmentSoldType findByShipmentSoldType(String string) {
		List<ShipmentSoldType> soldTypes = shipmentSoldTypeRepository.findAllByShipmentSoldType(string);
		return (soldTypes != null && !soldTypes.isEmpty()) ? soldTypes.get(0) : null;
	}

	@Override
	public ShipmentSoldType getByShipmentSoldTypeId(Byte shipmentSoldTypeId) {
		return shipmentSoldTypeRepository.findByShipmentSoldTypeId(shipmentSoldTypeId);
	}

	@Override
	public List<ShipmentSoldType> getAll() {
		return shipmentSoldTypeRepository.findAll();
	}
}

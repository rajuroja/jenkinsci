package com.dpworld.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.ShipmentSoldType;

@Repository
public interface ShipmentSoldTypeRepository extends JpaRepository<ShipmentSoldType, Byte> {

	@Query("Select sst from ShipmentSoldType sst where sst.shipmentSoldType = ?1")
	List<ShipmentSoldType> findAllByShipmentSoldType(String shipmentSoldType);
	
	ShipmentSoldType findByShipmentSoldTypeId(Byte shipmentSoldTypeId);
}

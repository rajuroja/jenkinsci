package com.dpworld.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.InbShipmentArrivalInformation;

@Repository
public interface InbShipmentArrivalInfoRepository extends JpaRepository<InbShipmentArrivalInformation, Long>{
	
	@Query("Select shipment from InbShipmentArrivalInformation shipment where shipment.id=?1")
	InbShipmentArrivalInformation findById(long id);
	
	@Query("SELECT shipment FROM InbShipmentArrivalInformation shipment WHERE shipment.id = ?1 AND shipment.inbound IS NOT NULL")
	InbShipmentArrivalInformation findByIdAndWithInbound(long id);
}

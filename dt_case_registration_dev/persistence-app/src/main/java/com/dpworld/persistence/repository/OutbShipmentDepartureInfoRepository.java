package com.dpworld.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.OutbShipmentDepartureInformation;

@Repository
public interface OutbShipmentDepartureInfoRepository extends JpaRepository<OutbShipmentDepartureInformation, Long> {

	@Query("SELECT departure FROM OutbShipmentDepartureInformation departure WHERE departure.id = ?1 AND departure.outbound IS NOT NULL")
	OutbShipmentDepartureInformation findByIdAndWithOutbound(long id);

	@Query("Select departure from OutbShipmentDepartureInformation departure where departure.id=?1")
	OutbShipmentDepartureInformation findById(long id);

}

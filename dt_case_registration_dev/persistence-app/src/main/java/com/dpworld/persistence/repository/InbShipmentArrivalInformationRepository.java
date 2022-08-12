package com.dpworld.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.InbShipmentArrivalInformation;

@Repository
public interface InbShipmentArrivalInformationRepository extends JpaRepository<InbShipmentArrivalInformation, Long> {

}

package com.dpworld.masterdataapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dpworld.masterdataapp.service.InbShipmentArrivalInformationService;
import com.dpworld.persistence.repository.InbShipmentArrivalInformationRepository;

@Service
public class InbShipmentArrivalInformationServiceImpl implements InbShipmentArrivalInformationService {

	@Autowired
	private InbShipmentArrivalInformationRepository inbShipmentArrivalInformationRepository;
	
}
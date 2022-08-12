package com.dpworld.masterdataapp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dpworld.masterdataapp.service.InbCargoInformationService;
import com.dpworld.persistence.repository.InbBarcodeDetailsRepository;
import com.dpworld.persistence.repository.InbCargoInformationRepository;
import com.dpworld.persistence.repository.InbInvoiceDetailRepository;

@Service
public class InbCargoInformationServiceImpl implements InbCargoInformationService {

	@Autowired
	private InbCargoInformationRepository inbCargoInformationRepository;

	@Autowired
	private InbInvoiceDetailRepository inbInvoiceDetailRepository;

	@Autowired
	private InbBarcodeDetailsRepository inbBarcodeDetailsRepository;

	@Override
	@Transactional
	public void deleteAllByIds(List<Long> ids) {
		inbInvoiceDetailRepository.deleteAllByCargoInformationIdsIn(ids);
		inbBarcodeDetailsRepository.deleteAllByCargoInformationIdsIn(ids);
		inbCargoInformationRepository.deleteAllByIdsIn(ids);
	}

}

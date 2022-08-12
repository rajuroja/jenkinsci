package com.dpworld.masterdataapp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dpworld.masterdataapp.service.OutbCargoInformationService;
import com.dpworld.persistence.repository.OutbBarcodeDetailsRepository;
import com.dpworld.persistence.repository.OutbCargoInformationRepository;
import com.dpworld.persistence.repository.OutbInvoiceDetailRepository;

@Service
public class OutbCargoInformationServiceImpl implements OutbCargoInformationService {

	@Autowired
	private OutbCargoInformationRepository outbCargoInformationRepository;

	@Autowired
	private OutbInvoiceDetailRepository outbInvoiceDetailRepository;

	@Autowired
	private OutbBarcodeDetailsRepository outbBarcodeDetailsRepository;

	@Override
	public void deleteAllByIds(List<Long> ids) {
		outbInvoiceDetailRepository.deleteAllByCargoInformationIdsIn(ids);
		outbBarcodeDetailsRepository.deleteAllByCargoInformationIdsIn(ids);
		outbCargoInformationRepository.deleteAllByIdsIn(ids);
	}

}

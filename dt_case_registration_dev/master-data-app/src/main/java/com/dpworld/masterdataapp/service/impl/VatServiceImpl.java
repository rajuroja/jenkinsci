package com.dpworld.masterdataapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dpworld.masterdataapp.service.VatService;
import com.dpworld.persistence.entity.Vat;
import com.dpworld.persistence.repository.VatRepository;

@Service
public class VatServiceImpl implements VatService {

	@Autowired
	private VatRepository vatRepository;
	
	@Override
	public Vat getLatest() {
		return vatRepository.findFirstByOrderByCreatedOnDesc();
	}
}
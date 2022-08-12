package com.dpworld.masterdataapp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dpworld.masterdataapp.service.DeclarationTypeService;
import com.dpworld.persistence.entity.DeclarationType;
import com.dpworld.persistence.repository.DeclarationTypeRepository;

@Service
public class DeclarationTypeServiceImpl implements DeclarationTypeService {

	@Autowired
	private DeclarationTypeRepository declarationTypeRepository;

	@Override
	public DeclarationType getByDeclarationTypeId(Integer declarationTypeId) {
		return declarationTypeRepository.findByDeclarationTypeId(declarationTypeId);
	}

	@Override
	public List<DeclarationType> getAll() {
		return declarationTypeRepository.findAll();
	}

}

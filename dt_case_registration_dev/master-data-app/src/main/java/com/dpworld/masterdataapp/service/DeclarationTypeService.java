package com.dpworld.masterdataapp.service;

import java.util.List;

import com.dpworld.persistence.entity.DeclarationType;

public interface DeclarationTypeService {

	DeclarationType getByDeclarationTypeId(Integer declarationTypeId);
	
	List<DeclarationType> getAll();
}

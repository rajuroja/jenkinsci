package com.dpworld.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.DeclarationType;

@Repository
public interface DeclarationTypeRepository extends CrudRepository<DeclarationType, Integer> {

	DeclarationType findByDeclarationTypeId(Integer declarationTypeId);

	List<DeclarationType> findAll();
}

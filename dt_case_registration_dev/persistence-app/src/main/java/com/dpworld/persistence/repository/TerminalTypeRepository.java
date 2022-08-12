package com.dpworld.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.TerminalType;

@Repository
public interface TerminalTypeRepository extends CrudRepository<TerminalType, Integer> {

	TerminalType findByTerminalId(Integer terminalTypeId);

	List<TerminalType> findByPortId(Integer portId);

	List<TerminalType> findAll();
}

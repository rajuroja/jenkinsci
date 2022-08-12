package com.dpworld.apiapp.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dpworld.apiapp.service.CaseService;
import com.dpworld.persistence.entity.CaseType;
import com.dpworld.persistence.entity.ComplainType;
import com.dpworld.persistence.entity.PortType;
import com.dpworld.persistence.entity.TerminalType;
import com.dpworld.persistence.repository.CaseTypeRepository;
import com.dpworld.persistence.repository.ComplainTypeRepository;
import com.dpworld.persistence.repository.PortTypeRepository;
import com.dpworld.persistence.repository.TerminalTypeRepository;

@Service
public class CaseServiceImpl implements CaseService {

	@Autowired
	CaseTypeRepository caseTypeRepo;

	@Autowired
	ComplainTypeRepository complainTypeRepo;

	@Autowired
	PortTypeRepository portTypeRepo;

	@Autowired
	TerminalTypeRepository terminalTypeRepo;

	@Override
	public Map<String, Object> getCaseLOVs() throws Exception {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<CaseType> dataList = caseTypeRepo.findAll();
		dataList.forEach(data -> {
			dataMap.put(String.valueOf(data.getCaseTypeId()), data.getCaseTypeName());
		});
		return dataMap;
	}

	@Override
	public Map<String, Object> getComplainTypeLov() throws Exception {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<ComplainType> dataList = complainTypeRepo.findAll();
		dataList.forEach(data -> {
			dataMap.put(String.valueOf(data.getComplainTypeId()), data.getComplainTypeName());
		});
		return dataMap;
	}

	@Override
	public Map<String, Object> getPortTypeLovs(int caseType) throws Exception {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<PortType> dataList = portTypeRepo.findAll();
		dataList.forEach(data -> {
			dataMap.put(String.valueOf(data.getPortId()), data.getPortName());
		});
		return dataMap;
	}

	@Override
	public Map<String, Object> getTerminalLovs(int portTypeId) throws Exception {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<TerminalType> dataList = terminalTypeRepo.findByPortId(portTypeId);
		dataList.forEach(data -> {
			dataMap.put(String.valueOf(data.getTerminalId()), data.getTerminalName());
		});

		return dataMap;
	}

}

package com.dpworld.masterdataapp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dpworld.masterdataapp.service.PushInventoryHistoryService;
import com.dpworld.masterdataapp.service.PushInventoryService;
import com.dpworld.persistence.entity.PushInventory;
import com.dpworld.persistence.entity.PushInventoryHistory;
import com.dpworld.persistence.repository.PushInventoryHistoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PushInventoryHistoryServiceImpl implements PushInventoryHistoryService {

	@Autowired
	private PushInventoryService pushInventoryService;

	@Autowired
	private PushInventoryHistoryRepository pushInventoryHistoryRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Transactional
	public Boolean savePushedRecords() {

		List<PushInventory> fetchPushedInventory = pushInventoryService.fetchPushedInventory();

		PushInventoryHistory pushInventoryHistory = null;
		for (PushInventory pushInventory : fetchPushedInventory) {

			pushInventoryHistory = objectMapper.convertValue(pushInventory, PushInventoryHistory.class);
			pushInventoryHistoryRepository.save(pushInventoryHistory);
			pushInventoryService.deletePushedInventory(pushInventory);
		}
		return true;
	}

}
package com.dpworld.masterdataapp.service;

import java.util.List;

import com.dpworld.common.utils.CustomException;
import com.dpworld.masterdataapp.model.request.SearchInventoryFilterRequest;
import com.dpworld.persistence.vo.SearchInventory;

public interface SearchInventoryService {

	List<SearchInventory> search(SearchInventoryFilterRequest vo, String serviceId) throws CustomException, Exception;

	List<SearchInventory> findAll(String serviceId) throws CustomException, Exception;

}

package com.dpworld.masterdataapp.controller;

import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.FAILED;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.INVALID_TYPE_OF_INVENTORY;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dpworld.common.model.ApiResponse;
import com.dpworld.common.utils.CustomException;
import com.dpworld.masterdataapp.constants.MasterDataAppConstants;
import com.dpworld.masterdataapp.model.request.SearchInventoryFilterRequest;
import com.dpworld.masterdataapp.service.SearchInventoryService;
import com.dpworld.persistence.vo.SearchInventory;

@RestController
@RequestMapping("/search-inventory")
public class SearchInventoryController {

	private static Logger logger = LoggerFactory.getLogger(SearchInventoryController.class);

	@Autowired
	private SearchInventoryService searchInventoryService;

	@GetMapping("/list")
	public ApiResponse<?> list(@RequestParam("serviceId") String serviceId) {
		//logger.info("ENTRY :: listInventory with serviceId: {}", serviceId);
		try {
			List<SearchInventory> list = searchInventoryService.findAll(serviceId);
			//logger.info("EXIT:: listInventory");
			if(list == null || list.isEmpty()) {
				logger.error("ERROR:: listInventory:: Error occured while list inventory : {}", MasterDataAppConstants.NO_AGENT_CODES_FOUND);
				return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), FAILED + MasterDataAppConstants.LIST_INVENTORY_FAILED);
			}
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), "success", list);
		} catch (CustomException e) {
			logger.error("ERROR:: listInventory:: Error occured while list inventory : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), FAILED + MasterDataAppConstants.LIST_INVENTORY_FAILED);
		} catch (Exception e) {
			logger.error("ERROR:: listInventory:: Error occured while list inventory : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.INTERNAL_SERVER_ERROR.name(), FAILED + MasterDataAppConstants.LIST_INVENTORY_FAILED);
		}
	}

	@PostMapping("/search")
	public ApiResponse<?> search(@RequestBody SearchInventoryFilterRequest vo, @RequestParam("serviceId") String serviceId) {
		//logger.info("ENTRY :: searchInventory : Search inventory with request: {}, serviceId: {}", vo, serviceId);
		if (vo.getTypeOfInventory() != null && !vo.getTypeOfInventory().trim().isEmpty() && !vo.getTypeOfInventory().equalsIgnoreCase("inbound")
				&& !vo.getTypeOfInventory().equalsIgnoreCase("outbound") && !vo.getTypeOfInventory().equalsIgnoreCase("returnGoods") && !vo.getTypeOfInventory().equalsIgnoreCase("returnLocalGoods")) {
			logger.error("ERROR:: searchInventory:: {}", INVALID_TYPE_OF_INVENTORY);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), INVALID_TYPE_OF_INVENTORY);
		}
		try {
			List<SearchInventory> list = searchInventoryService.search(vo, serviceId);
			//logger.info("EXIT:: searchInventory");
			return new ApiResponse<>(TRUE, HttpStatus.OK.name(), "success", list);
		} catch (CustomException e) {
			logger.error("ERROR:: listInventory:: Error occured while search inventory : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.BAD_REQUEST.name(), FAILED + MasterDataAppConstants.LIST_INVENTORY_FAILED);
		} catch (Exception e) {
			logger.error("ERROR:: searchInventory:: Error occured while search inventory : {}", e);
			return new ApiResponse<>(FALSE, HttpStatus.INTERNAL_SERVER_ERROR.name(), FAILED + MasterDataAppConstants.LIST_INVENTORY_FAILED);
		}
	}

}

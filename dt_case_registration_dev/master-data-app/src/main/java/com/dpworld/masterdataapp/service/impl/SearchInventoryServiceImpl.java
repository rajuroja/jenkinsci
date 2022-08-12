package com.dpworld.masterdataapp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dpworld.common.utils.CustomException;
import com.dpworld.masterdataapp.constants.MasterDataAppConstants;
import com.dpworld.masterdataapp.model.request.SearchInventoryFilterRequest;
import com.dpworld.masterdataapp.service.InboundService;
import com.dpworld.masterdataapp.service.OutboundService;
import com.dpworld.masterdataapp.service.SearchInventoryService;
import com.dpworld.masterdataapp.utility.TmWmsUtils;
import com.dpworld.persistence.repository.InboundRepository;
import com.dpworld.persistence.repository.OutboundRepository;
import com.dpworld.persistence.vo.SearchInventory;

@Service
public class SearchInventoryServiceImpl implements SearchInventoryService {

	@Autowired
	private InboundService inboundService;

	@Autowired
	private InboundRepository inboundRepository;

	@Autowired
	private OutboundService outboundService;

	@Autowired
	private OutboundRepository outBoundRepository;

	@Autowired
	private TmWmsUtils tmWmsUtils;
	
	@Override
	public List<SearchInventory> search(SearchInventoryFilterRequest vo, String serviceId) throws Exception {
		List<SearchInventory> list = new ArrayList<>();
		List<String> agentCodes = tmWmsUtils.getImporterAgentCodesByLoggedInUser(serviceId);
		if (agentCodes != null && !agentCodes.isEmpty()) {
			if (vo.getTypeOfInventory() != null && !vo.getTypeOfInventory().trim().isEmpty()) {
				if (vo.getTypeOfInventory().trim().equalsIgnoreCase("inbound") || vo.getTypeOfInventory().trim().equalsIgnoreCase("returnGoods") || vo.getTypeOfInventory().trim().equalsIgnoreCase("returnLocalGoods"))
					list.addAll(inboundService.filterSearchInventory(vo, agentCodes, false));
				if (vo.getTypeOfInventory().trim().equalsIgnoreCase("outbound"))
					list.addAll(outboundService.filterSearchInventory(vo, agentCodes, false));
			} else {
				list.addAll(inboundService.filterSearchInventory(vo, agentCodes, false));
				list.addAll(outboundService.filterSearchInventory(vo, agentCodes, false));
			}
			if (list != null && !list.isEmpty())
				list.sort((o1, o2) -> o2.getCreatedOn().compareTo(o1.getCreatedOn()));
			return list;
		}
		throw new CustomException(MasterDataAppConstants.NO_AGENT_CODES_FOUND);
	}

	@Override
	public List<SearchInventory> findAll(String serviceId) throws Exception {
		List<String> agentCodes = tmWmsUtils.getImporterAgentCodesByLoggedInUser(serviceId);
		if (agentCodes != null && !agentCodes.isEmpty()) {
			List<SearchInventory> list1 = inboundRepository.listSearchInventory(agentCodes);
			list1.addAll(outBoundRepository.listSearchInventory(agentCodes));
			list1.sort((o1, o2) -> o2.getCreatedOn().compareTo(o1.getCreatedOn()));
			return list1;
		}
		return null;
	}

}

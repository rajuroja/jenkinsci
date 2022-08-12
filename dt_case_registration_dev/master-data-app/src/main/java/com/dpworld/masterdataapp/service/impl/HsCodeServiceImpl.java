package com.dpworld.masterdataapp.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dpworld.common.utils.CustomException;
import com.dpworld.common.utils.Utils;
import com.dpworld.masterdataapp.model.response.HsCodeResponse;
import com.dpworld.masterdataapp.service.AuthorityService;
import com.dpworld.masterdataapp.service.CurrencyDetailsService;
import com.dpworld.masterdataapp.service.HsCodeAuthorityService;
import com.dpworld.masterdataapp.service.HsCodeService;
import com.dpworld.masterdataapp.service.WmsApiService;
import com.dpworld.masterdataapp.syncHsCode.SyncHsCodeRequestItems;
import com.dpworld.masterdataapp.syncHsCode.SyncHsCodeRequestRoot;
import com.dpworld.masterdataapp.syncHsCode.SyncHsCodeResponseRoot;
import com.dpworld.masterdataapp.syncHsCode.SyncHsCodeResponseItems;
import com.dpworld.persistence.entity.Authority;
import com.dpworld.persistence.entity.CurrencyDetails;
import com.dpworld.persistence.entity.HsCode;
import com.dpworld.persistence.entity.HsCodeAuthority;
import com.dpworld.persistence.repository.HsCodeRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class HsCodeServiceImpl implements HsCodeService {

	private static Logger logger = LoggerFactory.getLogger(HsCodeServiceImpl.class);
	
	@Value("${wms.defaults.currency}")
	private String defaultCurrency;
	
	@Autowired
	private HsCodeRepository hsCodeRepository;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private CurrencyDetailsService currencyDetailsService;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private HsCodeAuthorityService hsCodeAuthorityService;
	
	@Autowired
	private WmsApiService wmsApiService;

	@Override
	public HsCode getByHsCodeId(Long hsCodeId) {
		return hsCodeRepository.findByHsCodeId(hsCodeId);
	}

	@Override
	public List<HsCodeResponse> getByHsCodeContains(String hscode) {
		List<HsCode> hsCodes = hsCodeRepository.findTop20ByHsCodeContains(hscode);
		List<HsCodeResponse> hsCodeResponse = objectMapper.convertValue(hsCodes, new TypeReference<List<HsCodeResponse>>() {
		});
		return hsCodeResponse;
	}

	public HsCode findByHsCode(String hsCode) {
		List<HsCode> codes = hsCodeRepository.findAllByHsCode(hsCode);
		return (codes != null && !codes.isEmpty()) ? codes.get(0) : null;
	}

	@Override
	public void syncMasterData(String fromDate, String toDate) throws CustomException {
		logger.info("Started page : 1");
		Integer currentPage = 1;
		SyncHsCodeRequestItems itemRequest = new SyncHsCodeRequestItems(fromDate, toDate, "1");
		SyncHsCodeRequestRoot itemRequestRoot = new SyncHsCodeRequestRoot(itemRequest);
		SyncHsCodeResponseRoot response = null;
		List<SyncHsCodeResponseItems> xmlItems = new ArrayList<>();
		try {
			response = wmsApiService.getHsCodeMasterData(itemRequestRoot);
			logger.info("Completed page : 1" + ", records : " + (response.getItems() != null ? response.getItems().size() : 0));
			if (response.getItems() != null && !response.getItems().isEmpty()) {
				xmlItems.addAll(response.getItems());
				Integer totalPages = response.getPageCount() != null && !response.getPageCount().trim().isEmpty() ? Integer.parseInt(response.getPageCount()) : 0;
				if (totalPages > 1) {
					for (int i = 2; i <= totalPages; i++) {
						currentPage = i;
						itemRequest.setPage(i + "");
						itemRequestRoot.setItems(itemRequest);
						logger.info("Started page : " + i);
						response = wmsApiService.getHsCodeMasterData(itemRequestRoot);
						if (response.getItems() != null && !response.getItems().isEmpty())
							xmlItems.addAll(response.getItems());
						logger.info("Completed page : " + i + ", records : " + response.getItems().size());
					}
				}
			}
		} catch (CustomException e) {
			logger.error("ERROR :: Error occurred on HS Code sync on page number : {}, {}", currentPage, e.getMessage());
			throw e;
		} finally {
			syncUpdateHsCodes(xmlItems);
		}
	}
	
	private void syncUpdateHsCodes(List<SyncHsCodeResponseItems> xmlItems) {
		Set<String> authorities = xmlItems.stream().map(SyncHsCodeResponseItems::getAuthorityCode).filter(p -> (p != null && !p.trim().isEmpty())).collect(Collectors.toSet());
		authorityService.createOrUpdateAuthorities(authorities);
		CurrencyDetails currency = currencyDetailsService.findDefaultCurrency();
		Map<String, List<SyncHsCodeResponseItems>> map1 = new HashMap<>();
		xmlItems.forEach(object -> map1.computeIfAbsent(object.getHsCode(), k -> new ArrayList<>()).add(object));

		Double duty = 0d;
		for (Entry<String, List<SyncHsCodeResponseItems>> entry : map1.entrySet()) {
			System.out.println(entry.getKey());
			for (SyncHsCodeResponseItems item : entry.getValue()) {
				System.out.println(item.toString());
			}
			System.out.println("==============================================================");
			System.out.println("==============================================================");
			HsCode hsCode = findByHsCode(entry.getKey());
			if (hsCode == null) {
				hsCode = new HsCode();
				hsCode.setHsCode(entry.getKey());
				hsCode.setCurrencyId(currency);
				hsCodeRepository.save(hsCode);
			}
			hsCode.setHsCodeDescription(entry.getValue().get(0).getItemDescription());
			for (SyncHsCodeResponseItems item : entry.getValue()) {
				duty = 0d;
				if (item.getDuty() != null && !item.getDuty().isEmpty() && !item.getDuty().equalsIgnoreCase("Exempt"))
					duty = Utils.parseDouble(item.getDuty());

				if (item.getAuthorityCode() != null && !item.getAuthorityCode().trim().isEmpty()) {
					Authority authority = authorityService.findByAuthorityShortName(item.getAuthorityCode());
					if (authority != null) {
						HsCodeAuthority hsCodeAuthority = hsCodeAuthorityService.findByHsCodeAndAuthority(hsCode, authority);
						if (hsCodeAuthority == null) {
							hsCodeAuthority = new HsCodeAuthority();
							hsCodeAuthority.setAuthority(authority);
							hsCodeAuthority.setHsCode(hsCode);
						}
						hsCodeAuthority.setDutyValue(duty);
						hsCodeAuthorityService.save(hsCodeAuthority);
					}
				} else {
					hsCode.setDutyValue(duty);
				}
			}
			hsCodeRepository.save(hsCode);
		}
	}

	@Override
	public Double getDutyValue(HsCode hscode) {
		HsCodeAuthority hsca = null;
		if (hscode.getHsCodeAuthorities() != null && !hscode.getHsCodeAuthorities().isEmpty())
			hsca = hscode.getHsCodeAuthorities().stream().max(Comparator.comparing(HsCodeAuthority::getDutyValue)).get();
		if (hsca != null)
			return (hscode.getDutyValue() > hsca.getDutyValue()) ? hscode.getDutyValue() : hsca.getDutyValue();
		return hscode.getDutyValue();
	}
}

package com.dpworld.masterdataapp.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dpworld.common.utils.CustomException;
import com.dpworld.common.utils.Utils;
import com.dpworld.masterdataapp.model.request.BarCodeAddRequest;
import com.dpworld.masterdataapp.model.response.BarCodeResponse;
import com.dpworld.masterdataapp.pushBarcode.BarcodePushRequestItems;
import com.dpworld.masterdataapp.pushBarcode.BarcodePushRequestRoot;
import com.dpworld.masterdataapp.pushBarcode.BarcodePushResponseRoot;
import com.dpworld.masterdataapp.service.BarCodeService;
import com.dpworld.masterdataapp.service.HsCodeService;
import com.dpworld.masterdataapp.service.PushInventoryService;
import com.dpworld.masterdataapp.service.WmsApiService;
import com.dpworld.persistence.entity.BarCode;
import com.dpworld.persistence.entity.HsCode;
import com.dpworld.persistence.entity.PushInventory;
import com.dpworld.persistence.enums.OperationType;
import com.dpworld.persistence.repository.BarCodeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BarCodeServiceImpl implements BarCodeService {

	private static final Logger logger = LoggerFactory.getLogger(BarCodeServiceImpl.class);

	@Autowired
	private BarCodeRepository barCodeRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private HsCodeService hsCodeService;

	@Autowired
	private WmsApiService wmsApiService;
	
	@Autowired
	private PushInventoryService pushInventoryService;

	@Override
	public BarCodeResponse getByBarCode(String barCodeStr, Long hsCodeId) {

		HsCode hsCode = hsCodeService.getByHsCodeId(hsCodeId);
		BarCode barCode = barCodeRepository.findByBarCodeAndHsCodeId(barCodeStr, hsCode);
		BarCodeResponse barCodeResponse = objectMapper.convertValue(barCode, BarCodeResponse.class);
		return barCodeResponse;
	}

	@Override
	public StringBuilder validateAddBarCodeRequest(BarCodeAddRequest barCodeRequest, boolean isUpdate) {
		StringBuilder message = new StringBuilder();

		if (barCodeRequest.getBarCode() == null || barCodeRequest.getBarCode().trim().isEmpty())
			message.append("Please enter the barcode. \n");

		if (barCodeRequest.getBarCodeDescription() == null || barCodeRequest.getBarCodeDescription().trim().isEmpty())
			message.append("Please enter barcode description. \n");

		if (barCodeRequest.getHsCodeId() == 0)
			message.append("Please enter HsCode. \n");
		else {
			HsCode hsCode = hsCodeService.getByHsCodeId(barCodeRequest.getHsCodeId());
			if (hsCode == null)
				message.append("Hscode details not found. \n");
			else {
				BarCode barCode = barCodeRepository.findByBarCodeAndHsCodeId(barCodeRequest.getBarCode(), hsCode);
				if (barCode != null)
					message.append("Barcode already exist under Hscode: " + hsCode.getHsCode() + ". \n");
			}
		}
		return message;
	}

	@Override
	public BarCode addOrUpdateBarCode(BarCodeAddRequest barCodeRequest, boolean isUpdate) throws DataIntegrityViolationException {

		//logger.info("ENTRY:: addOrUpdateBarCode :: Add or Update barcode details.");

		HsCode hsCode = hsCodeService.getByHsCodeId(barCodeRequest.getHsCodeId());
		BarCode barCode = barCodeRepository.findByBarCodeAndHsCodeId(barCodeRequest.getBarCode(), hsCode);
		
		if (barCode == null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			barCode = objectMapper.convertValue(barCodeRequest, BarCode.class);
			barCode.setCreatedOn(new Date());
			barCode.setCreatedBy(auth.getName());
			barCode.setIsCancel(false);
			barCode.setHsCodeId(hsCode);
		}
		barCode.setBarCodeDescription(barCodeRequest.getBarCodeDescription());
		barCode = barCodeRepository.save(barCode);
		pushItemDetailsToOic(barCode);
		return barCode;
	}
	
	@Override
	public BarCode getByBarCodeId(Long barCodeId) {
		return barCodeRepository.findByBarCodeId(barCodeId);
	}
	
	@Override
	public Boolean pushItemDetailsToOic(BarCode barCode) {
		BarcodePushRequestRoot root = new BarcodePushRequestRoot();
		HsCode hscode = barCode.getHsCodeId();
		root.setItems(Arrays.asList(new BarcodePushRequestItems(hscode.getHsCode(), barCode.getBarCode(), hscode.getHsCodeDescription(), Utils.doubleToString(hscode.getDutyValue()))));
		PushInventory pushInventory = pushInventoryService.createItemDetailsPushInventory(root, barCode, OperationType.CREATE);
		try {
			BarcodePushResponseRoot responseRoot = wmsApiService.barCodePushData(root);
			pushInventoryService.updateBarCodePushInventoryStatus(pushInventory, responseRoot);
			if (responseRoot.getStatus() != null && !responseRoot.getStatus().trim().isEmpty() && responseRoot.getStatus().equalsIgnoreCase("SUCCESS")) {
				return true;
			} else {
				return false;
			}
		} catch (CustomException e) {
			return false;
		}
	}

	@Override
	public void save(BarCode barcode) {
		barCodeRepository.save(barcode);		
	}

	@Override
	public void syncBarCode() {
		List<BarCode> list = barCodeRepository.findAllNotPushedInOic();
		if (list != null && !list.isEmpty()) {
			for (BarCode barCode : list) {
				if (pushItemDetailsToOic(barCode))
					barCode.setPushedToOic(true);
				else
					barCode.setPushedToOic(false);
				barCodeRepository.save(barCode);
			}
		}
	}

}

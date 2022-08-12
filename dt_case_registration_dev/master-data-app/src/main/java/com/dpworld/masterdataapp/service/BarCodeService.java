package com.dpworld.masterdataapp.service;

import org.springframework.dao.DataIntegrityViolationException;

import com.dpworld.masterdataapp.model.request.BarCodeAddRequest;
import com.dpworld.masterdataapp.model.response.BarCodeResponse;
import com.dpworld.persistence.entity.BarCode;

public interface BarCodeService {

	BarCodeResponse getByBarCode(String barCode, Long hsCodeId);

	StringBuilder validateAddBarCodeRequest(BarCodeAddRequest barCodeRequest, boolean isUpdate);

	BarCode addOrUpdateBarCode(BarCodeAddRequest barCodeRequest, boolean isUpdate)
			throws DataIntegrityViolationException;

	BarCode getByBarCodeId(Long barCodeId);

	Boolean pushItemDetailsToOic(BarCode barCode);

	void save(BarCode barcode);

	void syncBarCode();

}

package com.dpworld.masterdataapp.service;

import java.util.List;

import com.dpworld.masterdataapp.model.response.HsCodeResponse;
import com.dpworld.persistence.entity.HsCode;

public interface HsCodeService {

	HsCode getByHsCodeId(Long hsCodeId);
	
	List<HsCodeResponse> getByHsCodeContains(String hscode);

	HsCode findByHsCode(String hsCode);

	void syncMasterData(String fromDate, String toDate);

	Double getDutyValue(HsCode hscode);
}

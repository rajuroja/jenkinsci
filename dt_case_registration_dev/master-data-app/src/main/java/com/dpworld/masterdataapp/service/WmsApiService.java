package com.dpworld.masterdataapp.service;

import com.dpworld.common.utils.CustomException;
import com.dpworld.masterdataapp.model.request.ValidateUserServiceRequest;
import com.dpworld.masterdataapp.pushBarcode.BarcodePushRequestRoot;
import com.dpworld.masterdataapp.pushBarcode.BarcodePushResponseRoot;
import com.dpworld.masterdataapp.pushInbound.InboundPushRequestRoot;
import com.dpworld.masterdataapp.pushInbound.InboundPushResponseRoot;
import com.dpworld.masterdataapp.pushOutbound.OutboundPushRequestRoot;
import com.dpworld.masterdataapp.pushOutbound.OutboundPushResponseRoot;
import com.dpworld.masterdataapp.pushReturnGoods.ReturnGoodsPushRequestRoot;
import com.dpworld.masterdataapp.pushReturnGoods.ReturnGoodsPushResponseRoot;
import com.dpworld.masterdataapp.pushReturnLocalGoods.ReturnLocalGoodsPushRequestRoot;
import com.dpworld.masterdataapp.pushReturnLocalGoods.ReturnLocalGoodsPushResponseRoot;
import com.dpworld.masterdataapp.syncHsCode.SyncHsCodeRequestRoot;
import com.dpworld.masterdataapp.syncHsCode.SyncHsCodeResponseRoot;
import com.fasterxml.jackson.databind.JsonNode;

public interface WmsApiService {

	JsonNode fetchUserCompanyInfo(String username, String serviceId) throws CustomException, Exception;

	JsonNode validateUserService(ValidateUserServiceRequest validateUserServiceRequest) throws Exception;

	SyncHsCodeResponseRoot getHsCodeMasterData(SyncHsCodeRequestRoot requestRoot) throws CustomException;

	String getReferenceCode(String type) throws Exception;

	InboundPushResponseRoot inboundPushData(InboundPushRequestRoot updateRequest) throws CustomException;

	ReturnGoodsPushResponseRoot returnGoodsPushData(ReturnGoodsPushRequestRoot root) throws CustomException;

	OutboundPushResponseRoot outboundPushData(OutboundPushRequestRoot updateRequest) throws CustomException;

	ReturnLocalGoodsPushResponseRoot returnLocalGoodsPushData(ReturnLocalGoodsPushRequestRoot root) throws CustomException;

	BarcodePushResponseRoot barCodePushData(BarcodePushRequestRoot root) throws CustomException;

}

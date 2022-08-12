package com.dpworld.masterdataapp.service;

import java.util.List;

import javax.xml.stream.XMLStreamException;

import com.dpworld.common.utils.CustomException;
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
import com.dpworld.persistence.entity.BarCode;
import com.dpworld.persistence.entity.InbCargoInformation;
import com.dpworld.persistence.entity.Inbound;
import com.dpworld.persistence.entity.OutbCargoInformation;
import com.dpworld.persistence.entity.Outbound;
import com.dpworld.persistence.entity.PushInventory;
import com.dpworld.persistence.enums.OperationType;
import com.dpworld.persistence.vo.UpdatePushInventoryStatusResponse;

public interface PushInventoryService {

	List<PushInventory> fetchPushedInventory();

	void deletePushedInventory(PushInventory pushInventory);

	void save(PushInventory pushInventory);

	void resyncPushInventory() throws CustomException, XMLStreamException;

	void updatePushInventoryStatus(Long id);
	
	//
	
	ReturnLocalGoodsPushRequestRoot createOrUpdateReturnLocalGoodsPushRequest(Inbound inbound, List<InbCargoInformation> oldCargoList, List<Long> deletedCargosIds, List<Long> deletedBarcodeDetails);
	
	ReturnLocalGoodsPushRequestRoot cancelReturnLocalGoodsPushRequest(Inbound inbound, List<InbCargoInformation> cancelledCargoList);
	
	PushInventory createReturnLocalGoodsPushInventory(ReturnLocalGoodsPushRequestRoot requestRoot, Inbound inbound, OperationType cancel);
	
	UpdatePushInventoryStatusResponse updateReturnLocalGoodsPushInventoryStatus(PushInventory pushInventory, ReturnLocalGoodsPushResponseRoot responseRoot);

	//
	
	ReturnGoodsPushRequestRoot createOrUpdateReturnGoodsPushRequest(Inbound inbound, List<InbCargoInformation> oldCargoList, List<Long> deletedCargosIds, List<Long> deletedBarcodeDetails);
	
	ReturnGoodsPushRequestRoot cancelReturnGoodsPushRequest(Inbound inbound, List<InbCargoInformation> cancelledCargoList);
	
	PushInventory createReturnGoodsPushInventory(ReturnGoodsPushRequestRoot requestRoot, Inbound inbound, OperationType operationType);
	
	UpdatePushInventoryStatusResponse updateReturnGoodsPushInventoryStatus(PushInventory pushInventory, ReturnGoodsPushResponseRoot responseRoot);
	
	//
	
	InboundPushRequestRoot createOrUpdateInboundPushRequest(Inbound inbound, List<InbCargoInformation> oldCargoList, List<Long> deletedCargosIds, List<Long> deletedBarcodeDetails);
	
	InboundPushRequestRoot cancelInboundPushRequest(Inbound inbound, List<InbCargoInformation> cancelledCargoList);

	PushInventory createInboundPushInventory(InboundPushRequestRoot requestRoot, Inbound inbound, OperationType operationType);
	
	UpdatePushInventoryStatusResponse updateInboundPushInventoryStatus(PushInventory pushInventory, InboundPushResponseRoot responseRoot);
	
	//
	
	OutboundPushRequestRoot createOrUpdateOutboundPushRequest(Outbound outbound, List<OutbCargoInformation> oldCargoList, List<Long> deletedCargosIds, List<Long> deletedBarcodeDetails);
	
	OutboundPushRequestRoot cancelOutboundPushRequest(Outbound outbound, List<OutbCargoInformation> cancelledCargoList);
	
	PushInventory createOutboundPushInventory(OutboundPushRequestRoot requestRoot, Outbound outbound, OperationType cancel);
	
	UpdatePushInventoryStatusResponse updateOutboundPushInventoryStatus(PushInventory pushInventory, OutboundPushResponseRoot responseRoot);

	//
	
	PushInventory createItemDetailsPushInventory(BarcodePushRequestRoot requestRoot, BarCode barCode, OperationType operationType);

	List<String> updateBarCodePushInventoryStatus(PushInventory pushInventory, BarcodePushResponseRoot responseRoot);

}

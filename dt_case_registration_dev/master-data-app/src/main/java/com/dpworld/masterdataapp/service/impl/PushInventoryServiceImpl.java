package com.dpworld.masterdataapp.service.impl;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dpworld.common.utils.CustomException;
import com.dpworld.common.utils.Utils;
import com.dpworld.masterdataapp.constants.MasterDataAppConstants;
import com.dpworld.masterdataapp.constants.TransportModeConstants;
import com.dpworld.masterdataapp.pushBarcode.BarcodePushRequestItems;
import com.dpworld.masterdataapp.pushBarcode.BarcodePushRequestRoot;
import com.dpworld.masterdataapp.pushBarcode.BarcodePushResponseRoot;
import com.dpworld.masterdataapp.pushInbound.InboundPushRequestItems;
import com.dpworld.masterdataapp.pushInbound.InboundPushRequestRoot;
import com.dpworld.masterdataapp.pushInbound.InboundPushResponseItems;
import com.dpworld.masterdataapp.pushInbound.InboundPushResponseRoot;
import com.dpworld.masterdataapp.pushOutbound.OutboundPushRequestItems;
import com.dpworld.masterdataapp.pushOutbound.OutboundPushRequestRoot;
import com.dpworld.masterdataapp.pushOutbound.OutboundPushResponseItems;
import com.dpworld.masterdataapp.pushOutbound.OutboundPushResponseRoot;
import com.dpworld.masterdataapp.pushReturnGoods.ReturnGoodsPushRequestItems;
import com.dpworld.masterdataapp.pushReturnGoods.ReturnGoodsPushRequestRoot;
import com.dpworld.masterdataapp.pushReturnGoods.ReturnGoodsPushResponseItems;
import com.dpworld.masterdataapp.pushReturnGoods.ReturnGoodsPushResponseRoot;
import com.dpworld.masterdataapp.pushReturnLocalGoods.ReturnLocalGoodsPushRequestItems;
import com.dpworld.masterdataapp.pushReturnLocalGoods.ReturnLocalGoodsPushRequestRoot;
import com.dpworld.masterdataapp.pushReturnLocalGoods.ReturnLocalGoodsPushResponseItems;
import com.dpworld.masterdataapp.pushReturnLocalGoods.ReturnLocalGoodsPushResponseRoot;
import com.dpworld.masterdataapp.service.PushInventoryService;
import com.dpworld.masterdataapp.service.WmsApiService;
import com.dpworld.persistence.entity.BarCode;
import com.dpworld.persistence.entity.HsCode;
import com.dpworld.persistence.entity.InbBarCodeDetails;
import com.dpworld.persistence.entity.InbCargoInformation;
import com.dpworld.persistence.entity.InbDeclarationInformation;
import com.dpworld.persistence.entity.InbInvoiceDetail;
import com.dpworld.persistence.entity.InbShipmentArrivalInformation;
import com.dpworld.persistence.entity.Inbound;
import com.dpworld.persistence.entity.OutbBarCodeDetails;
import com.dpworld.persistence.entity.OutbCargoInformation;
import com.dpworld.persistence.entity.OutbDeclarationInformation;
import com.dpworld.persistence.entity.OutbInvoiceDetail;
import com.dpworld.persistence.entity.OutbShipmentDepartureInformation;
import com.dpworld.persistence.entity.Outbound;
import com.dpworld.persistence.entity.PushInventory;
import com.dpworld.persistence.entity.PushInventoryItem;
import com.dpworld.persistence.entity.StatusType;
import com.dpworld.persistence.enums.InventoryType;
import com.dpworld.persistence.enums.OperationType;
import com.dpworld.persistence.repository.PushInventoryItemRepository;
import com.dpworld.persistence.repository.PushInventoryRepository;
import com.dpworld.persistence.vo.UpdatePushInventoryStatusResponse;

@Service
public class PushInventoryServiceImpl implements PushInventoryService {
	
	private static Logger logger = LoggerFactory.getLogger(PushInventoryServiceImpl.class);

	@Autowired
	private PushInventoryRepository pushInventoryRepository;

	@Autowired
	private PushInventoryItemRepository pushInventoryItemRepository;

	@Autowired
	private WmsApiService wmsApiService;
	
	private InboundPushRequestItems createInboundPushRequestItem(Inbound inbound, InbCargoInformation cargo, InbBarCodeDetails barcode, Double adjustmentQuantity) {
		InboundPushRequestItems item = new InboundPushRequestItems();
		HsCode hscode = null;
		
		if (inbound.getInbShipmentArrivalInformation() != null) {
			InbShipmentArrivalInformation arrivalInfo = inbound.getInbShipmentArrivalInformation();
			item.setArrivedFrom(arrivalInfo.getArrivedFromCountryId() != null ? arrivalInfo.getArrivedFromCountryId().getCountryName() : "");
			item.setArrivalDate(arrivalInfo.getArrivalDate() + "");
			item.setMarketType(arrivalInfo.getMarketTypeId() != null ? arrivalInfo.getMarketTypeId().getMarketType() : "");
			item.setModeofTransport((arrivalInfo.getTransportMode() == 1 ? TransportModeConstants.SEA
					: (arrivalInfo.getTransportMode() == 2 ? TransportModeConstants.LAND : (arrivalInfo.getTransportMode() == 3 ? TransportModeConstants.AIR : ""))));
			item.setVoyageNumber(arrivalInfo.getVoyageNumber() != null ? arrivalInfo.getVoyageNumber() : "");
			item.setCarrier(arrivalInfo.getCarrier() != null ? arrivalInfo.getCarrier() : "");
		} else {
			item.setArrivedFrom("");
			item.setArrivalDate("");
			item.setMarketType("");
			item.setModeofTransport("");
			item.setVoyageNumber("");
			item.setCarrier("");
		}
		if (inbound.getInbDeclarationInformation() != null) {
			InbDeclarationInformation declInfo = inbound.getInbDeclarationInformation();
			item.setDeclarationNumber(declInfo.getDeclarationNumber());
			item.setDeclarationType(declInfo.getDeclarationTypeId() != null ? declInfo.getDeclarationTypeId().getDeclarationType() : "");
			item.setLocalGoodsPassNumber(declInfo.getLocalGoodsPassNumber() != null ? declInfo.getLocalGoodsPassNumber() + "" : "");
			item.setConsigneeName(declInfo.getConsigneeName());
			item.setBusinessCode(declInfo.getBusinessCode());
		} else {
			item.setDeclarationNumber("");
			item.setDeclarationType("");
			item.setLocalGoodsPassNumber("");
			item.setConsigneeName("");
			item.setBusinessCode("");
		}
		item.setPackageType("");
		item.setPallet("");
		item.setQuantity(Utils.doubleToString(adjustmentQuantity));

		if (barcode != null)
			item.setWeight(Utils.doubleToString(barcode.getPiecesNetWeight()));
		else
			item.setWeight(Utils.doubleToString(cargo.getPiecesNetWeight()));

		item.setCarton("");
		item.setCountryOfManufacture(cargo.getManufacturerCountryId() != null ? cargo.getManufacturerCountryId().getCountryName() : "");
		item.setPackageDescription(cargo.getPackageDescription());
		item.setGoodsCondition(cargo.getGoodsConditionId() != null ? cargo.getGoodsConditionId().getGoodsCondition() : "");
		if (cargo.getHsCodeId() != null) {
			hscode = cargo.getHsCodeId();
			item.setHsCode(hscode.getHsCode());
			item.setHsCodeDescription(hscode.getHsCodeDescription());
			item.setAuthority(hscode.getAuthoritiyCodes());
		} else {
			item.setHsCode("");
			item.setHsCodeDescription("");
			item.setAuthority("");
			item.setSkuBarcode("");
		}
		if (hscode != null && barcode != null)
			item.setSkuBarcode(hscode.getHsCode() + "/" + barcode.getBarCodeId().getBarCode());
		else if (hscode != null)
			item.setSkuBarcode(hscode.getHsCode());
		else
			item.setSkuBarcode("");
		if (cargo.getInbInvoiceDetail() != null) {
			InbInvoiceDetail invoice = cargo.getInbInvoiceDetail();
			item.setInvoiceNumber(invoice.getInvoiceNumber());
			item.setInvoiceDate(invoice.getInvoiceDate() + "");
			item.setCurrency(invoice.getCurrencyId() != null ? invoice.getCurrencyId().getCurrencyName() : "");
			item.setCifValue(Utils.doubleToString(invoice.getCifValue()));
		} else {
			item.setInvoiceNumber("");
			item.setInvoiceDate("");
			item.setCurrency("");
			item.setCifValue("0");
		}
		item.setInboundReferenceNumber(inbound.getReferenceNumber());
		item.setCustomsDeclaration("");
		item.setCertificateOfOrigin("");
		item.setCommercialInvoice("");
		item.setPackingList("");
		return item;
	}

	private ReturnGoodsPushRequestItems createReturnGoodsPushRequestItem(Inbound inbound, InbCargoInformation cargo, InbBarCodeDetails barcode, Double adjustmentQuantity) {
		HsCode hscode = null;
		ReturnGoodsPushRequestItems item = new ReturnGoodsPushRequestItems();
		
		if (inbound.getInbShipmentArrivalInformation() != null) {
			InbShipmentArrivalInformation arrivalInfo = inbound.getInbShipmentArrivalInformation();
			item.setArrivedFrom(arrivalInfo.getArrivedFromCountryId() != null ? arrivalInfo.getArrivedFromCountryId().getCountryName() : "");
			item.setArrivalDate(arrivalInfo.getArrivalDate() + "");
			item.setMarketType(arrivalInfo.getMarketTypeId() != null ? arrivalInfo.getMarketTypeId().getMarketType() : "");
			item.setModeofTransport((arrivalInfo.getTransportMode() == 1 ? TransportModeConstants.SEA
					: (arrivalInfo.getTransportMode() == 2 ? TransportModeConstants.LAND : (arrivalInfo.getTransportMode() == 3 ? TransportModeConstants.AIR : ""))));
			item.setVoyageNumber(arrivalInfo.getVoyageNumber() != null ? arrivalInfo.getVoyageNumber() : "");
			item.setCarrier(arrivalInfo.getCarrier() != null ? arrivalInfo.getCarrier() : "");
		} else {
			item.setArrivedFrom("");
			item.setArrivalDate("");
			item.setMarketType("");
			item.setModeofTransport("");
			item.setVoyageNumber("");
			item.setCarrier("");
		}
		if (inbound.getInbDeclarationInformation() != null) {
			InbDeclarationInformation declInfo = inbound.getInbDeclarationInformation();
			item.setInbDeclarationNumber(declInfo.getDeclarationNumber());
			item.setDeclarationType(declInfo.getDeclarationTypeId() != null ? declInfo.getDeclarationTypeId().getDeclarationType() : "");
			item.setLocalGoodsPassNumber(declInfo.getLocalGoodsPassNumber() != null ? declInfo.getLocalGoodsPassNumber() + "" : "");
			item.setConsigneeName(declInfo.getConsigneeName());
			item.setBusinessCode(declInfo.getBusinessCode());
		} else {
			item.setInbDeclarationNumber(""); // PENDING
			item.setDeclarationType("");
			item.setLocalGoodsPassNumber("");
			item.setConsigneeName("");
			item.setBusinessCode("");
		}
		item.setOutboundDeclarationNumber("");
		item.setPackageType("");
		item.setPallet("");
		item.setQuantity(Utils.doubleToString(adjustmentQuantity));

		if (barcode != null)
			item.setWeight(Utils.doubleToString(barcode.getPiecesNetWeight()));
		else
			item.setWeight(Utils.doubleToString(cargo.getPiecesNetWeight()));

		item.setCarton("");
		item.setCountryOfManufacture(cargo.getManufacturerCountryId() != null ? cargo.getManufacturerCountryId().getCountryName() : "");
		item.setPackageDescription(cargo.getPackageDescription());
		item.setGoodsCondition(cargo.getGoodsConditionId() != null ? cargo.getGoodsConditionId().getGoodsCondition() : "");
		if (cargo.getHsCodeId() != null) {
			hscode = cargo.getHsCodeId();
			item.setHsCode(hscode.getHsCode());
			item.setHsCodeDescription(hscode.getHsCodeDescription());
			item.setPermit("");
			item.setAuthority(hscode.getAuthoritiyCodes());
		} else {
			item.setHsCode("");
			item.setHsCodeDescription("");
			item.setPermit("");
			item.setAuthority("");
		}
		if (hscode != null && barcode != null)
			item.setSkuBarcode(hscode.getHsCode() + "/" + barcode.getBarCodeId().getBarCode());
		else if (hscode != null)
			item.setSkuBarcode(hscode.getHsCode());
		else
			item.setSkuBarcode("");
		if (cargo.getInbInvoiceDetail() != null) {
			InbInvoiceDetail invoice = cargo.getInbInvoiceDetail();
			item.setInvoiceNumber(invoice.getInvoiceNumber() + "");
			item.setInvoiceDate(invoice.getInvoiceDate() + "");
			item.setCurrency(invoice.getCurrencyId() != null ? invoice.getCurrencyId().getCurrencyName() : "");
			item.setCifValue(Utils.doubleToString(invoice.getCifValue()));
		} else {
			item.setInvoiceNumber("");
			item.setInvoiceDate("");
			item.setCurrency("");
			item.setCifValue("0");
		}
		item.setReturngoodsReferenceNumber(inbound.getReferenceNumber());
		item.setCustomsDeclaration("");
		item.setCertificateOfOrigin("");
		item.setCommercialInvoice("");
		item.setPackingList("");
		return item;
	}

	private ReturnLocalGoodsPushRequestItems createReturnLocalGoodsPushRequestItem(Inbound inbound, InbCargoInformation cargo, InbBarCodeDetails barcode, Double adjustmentQuantity) {
		HsCode hscode = null;
		ReturnLocalGoodsPushRequestItems item = new ReturnLocalGoodsPushRequestItems();
		
		if (inbound.getInbShipmentArrivalInformation() != null) {
			InbShipmentArrivalInformation arrivalInfo = inbound.getInbShipmentArrivalInformation();
			item.setArrivedFrom(arrivalInfo.getArrivedFromCountryId() != null ? arrivalInfo.getArrivedFromCountryId().getCountryName() : "");
			item.setArrivalDate(arrivalInfo.getArrivalDate() + "");
			item.setMarketType(arrivalInfo.getMarketTypeId() != null ? arrivalInfo.getMarketTypeId().getMarketType() : "");
			item.setModeofTransport((arrivalInfo.getTransportMode() == 1 ? TransportModeConstants.SEA
					: (arrivalInfo.getTransportMode() == 2 ? TransportModeConstants.LAND : (arrivalInfo.getTransportMode() == 3 ? TransportModeConstants.AIR : ""))));
			item.setVoyageNumber(arrivalInfo.getVoyageNumber() != null ? arrivalInfo.getVoyageNumber() : "");
			item.setCarrier(arrivalInfo.getCarrier() != null ? arrivalInfo.getCarrier() : "");
		} else {
			item.setArrivedFrom("");
			item.setArrivalDate("");
			item.setMarketType("");
			item.setModeofTransport("");
			item.setVoyageNumber("");
			item.setCarrier("");
		}
		if (inbound.getInbDeclarationInformation() != null) {
			InbDeclarationInformation declInfo = inbound.getInbDeclarationInformation();
			item.setInbDeclarationNumber(declInfo.getDeclarationNumber()); // PENDING
			item.setDeclarationType(declInfo.getDeclarationTypeId() != null ? declInfo.getDeclarationTypeId().getDeclarationType() : "");
			item.setLocalGoodsPassNumber(declInfo.getLocalGoodsPassNumber() != null ? declInfo.getLocalGoodsPassNumber() + "" : "");
			item.setConsigneeName(declInfo.getConsigneeName());
			item.setBusinessCode(declInfo.getBusinessCode());
		} else {
			item.setInbDeclarationNumber(""); // PENDING
			item.setDeclarationType("");
			item.setLocalGoodsPassNumber("");
			item.setConsigneeName("");
			item.setBusinessCode("");
		}
		item.setOutboundDeclarationNumber("");
		item.setPackageType("");
		item.setPallet("");
		item.setQuantity(Utils.doubleToString(adjustmentQuantity));

		if (barcode != null)
			item.setWeight(Utils.doubleToString(barcode.getPiecesNetWeight()));
		else
			item.setWeight(Utils.doubleToString(cargo.getPiecesNetWeight()));

		item.setCarton("");
		item.setCountryOfManufacture(cargo.getManufacturerCountryId() != null ? cargo.getManufacturerCountryId().getCountryName() : "");
		item.setPackageDescription(cargo.getPackageDescription());
		item.setGoodsCondition(cargo.getGoodsConditionId() != null ? cargo.getGoodsConditionId().getGoodsCondition() : "");
		if (cargo.getHsCodeId() != null) {
			hscode = cargo.getHsCodeId();
			item.setHsCode(hscode.getHsCode());
			item.setHsCodeDescription(hscode.getHsCodeDescription());
			item.setPermit("");
			item.setAuthority(hscode.getAuthoritiyCodes());
		} else {
			item.setHsCode("");
			item.setHsCodeDescription("");
			item.setPermit("");
			item.setAuthority("");
		}
		if (hscode != null && barcode != null)
			item.setSkuBarcode(hscode.getHsCode() + "/" + barcode.getBarCodeId().getBarCode());
		else if (hscode != null)
			item.setSkuBarcode(hscode.getHsCode());
		else
			item.setSkuBarcode("");
		if (cargo.getInbInvoiceDetail() != null) {
			InbInvoiceDetail invoice = cargo.getInbInvoiceDetail();
			item.setInvoiceNumber(invoice.getInvoiceNumber() + "");
			item.setInvoiceDate(invoice.getInvoiceDate() + "");
			item.setCurrency(invoice.getCurrencyId() != null ? invoice.getCurrencyId().getCurrencyName() : "");
			item.setCifValue(Utils.doubleToString(invoice.getCifValue()));
		} else {
			item.setInvoiceNumber("");
			item.setInvoiceDate("");
			item.setCurrency("");
			item.setCifValue("0");
		}
		item.setReturnLocalGoodsReferenceNumber(inbound.getReferenceNumber());
		item.setCustomsDeclaration("");
		item.setCertificateOfOrigin("");
		item.setCommercialInvoice("");
		item.setPackingList("");
		return item;
	}

	private OutboundPushRequestItems createOutboundPushRequestItem(Outbound outbound, OutbCargoInformation cargo, OutbBarCodeDetails barcode, double adjustmentQuantity) {
		HsCode hscode = null;
		OutboundPushRequestItems item = new OutboundPushRequestItems();
		
		if (outbound.getOutbShipmentDepartureInformation() != null) {
			OutbShipmentDepartureInformation departureInfo = outbound.getOutbShipmentDepartureInformation();
			item.setArrivedFrom(departureInfo.getCountryDetail() != null ? departureInfo.getCountryDetail().getCountryName() : "");
			item.setArrivalDate(departureInfo.getShipmentDate() + "");
			item.setMarketType("");
			item.setModeofTransport(departureInfo.getTransportMode() + "");
			item.setVoyageNumber(departureInfo.getVoyageNumber() != null ? departureInfo.getVoyageNumber() : "");
			item.setCarrier(departureInfo.getCarrier() != null ? departureInfo.getCarrier() : "");
		} else {
			item.setArrivedFrom("");
			item.setArrivalDate("");
			item.setMarketType("");
			item.setModeofTransport("");
			item.setVoyageNumber("");
			item.setCarrier("");
		}
		if (outbound.getOutbDeclarationInformation() != null) {
			OutbDeclarationInformation declInfo = outbound.getOutbDeclarationInformation();
			item.setInbDeclarationNumber(declInfo.getInbDeclarationNumber());
			item.setOutbDeclarationNumber(declInfo.getOutbDeclarationNumber());
			item.setLocalGoodsPassNumber("");
			item.setConsigneeName(declInfo.getConsigneeName());
			item.setBusinessCode(declInfo.getBusinessCode());
			item.setDeclarationType(declInfo.getOutbDeclarationTypeId() != null ? declInfo.getOutbDeclarationTypeId().getDeclarationType() : "");
		} else {
			item.setInbDeclarationNumber("");
			item.setOutbDeclarationNumber("");
			item.setLocalGoodsPassNumber("");
			item.setConsigneeName("");
			item.setBusinessCode("");
			item.setDeclarationType("");
		}
		item.setPackageType("");
		item.setPallet("");
		item.setQuantity(Utils.doubleToString(adjustmentQuantity));

		if (barcode != null)
			item.setWeight(Utils.doubleToString(barcode.getPiecesNetWeight()));
		else
			item.setWeight(Utils.doubleToString(cargo.getPiecesNetWeight()));

		item.setCarton("");
		item.setCountryOfManufacture(cargo.getManufacturerCountryId() != null ? cargo.getManufacturerCountryId().getCountryName() : "");
		item.setPackageDescription(cargo.getPackageDescription());
		item.setGoodsCondition(cargo.getGoodsConditionId() != null ? cargo.getGoodsConditionId().getGoodsCondition() : "");

		if (cargo.getHsCodeId() != null) {
			hscode = cargo.getHsCodeId();
			item.setHsCode(hscode.getHsCode());
			item.setHsCodeDescription(hscode.getHsCodeDescription());
			item.setAuthority(hscode.getAuthoritiyCodes());
		} else {
			item.setHsCode("");
			item.setHsCodeDescription("");
			item.setAuthority("");
		}

		if (hscode != null && barcode != null)
			item.setSkuBarcode(hscode.getHsCode() + "/" + barcode.getBarCodeId().getBarCode());
		else if (hscode != null)
			item.setSkuBarcode(hscode.getHsCode());
		else
			item.setSkuBarcode("");

		if (cargo.getOutbInvoiceDetail() != null) {
			OutbInvoiceDetail invoice = cargo.getOutbInvoiceDetail();
			item.setInvoiceNumber(invoice.getInvoiceNumber() + "");
			item.setInvoiceDate(invoice.getInvoiceDate() + "");
			item.setCurrency(invoice.getCurrencyId() != null ? invoice.getCurrencyId().getCurrencyName() : "");
			item.setCifValue(Utils.doubleToString(invoice.getCifValue()));
		} else {
			item.setInvoiceNumber("");
			item.setInvoiceDate("");
			item.setCurrency("");
			item.setCifValue("0");
		}
		item.setPermit("");
		item.setOutbReferenceNumber(outbound.getReferenceNumber() != null ? outbound.getReferenceNumber() : "");
		item.setCustomsDeclaration("");
		item.setCertificateOfOrigin("");
		item.setCommercialInvoice("");
		item.setPackingList("");
		return item;
	}

	@Override
	public void save(PushInventory pushInventory) {
		pushInventoryRepository.save(pushInventory);
	}

	@Override
	@Transactional
	public void resyncPushInventory() throws CustomException, XMLStreamException {
		List<PushInventory> pendingPushInventories = pushInventoryRepository.findByStatusNot(StatusType.SUCCESS);
		List<PushInventoryItem> failedItemList = new ArrayList<PushInventoryItem>();
		for (PushInventory pushInventory : pendingPushInventories) {
			failedItemList.clear();
			if (pushInventory.getPushInventoryItems() != null && !pushInventory.getPushInventoryItems().isEmpty()) {
				for (PushInventoryItem item : pushInventory.getPushInventoryItems()) {
					if (!item.getStatus().equals(StatusType.SUCCESS))
						failedItemList.add(item);
				}
				if (!failedItemList.isEmpty())
					retryPushInventory(failedItemList, pushInventory);
			}
			pushInventory.setLastPushedDate(LocalDateTime.now());
			pushInventory.setRetryCount(pushInventory.getRetryCount() != null ? pushInventory.getRetryCount() + 1 : 1);
			pushInventoryRepository.save(pushInventory);
		}
	}

	private void retryPushInventory(List<PushInventoryItem> failedItemList, PushInventory pushInventory) throws XMLStreamException {
		//logger.info("ENTRY :: retryPushInventory failedItemList : {}, pushInventory : {}", failedItemList, pushInventory);
		if (pushInventory.getInventoryType().equals(InventoryType.INBOUND)) {
			retryInboundPushInventory(failedItemList, pushInventory);
		} else if (pushInventory.getInventoryType().equals(InventoryType.RETURNGOODS)) {
			retryReturnGoodsPushInventory(failedItemList, pushInventory);
		} else if (pushInventory.getInventoryType().equals(InventoryType.RETURNLOCALGOODS)) {
			retryReturnLocalGoodsPushInventory(failedItemList, pushInventory);
		} else if (pushInventory.getInventoryType().equals(InventoryType.OUTBOUND)) {
			retryOutboundPushInventory(failedItemList, pushInventory);
		} else if(pushInventory.getInventoryType().equals(InventoryType.BARCODE)) {
			retryBarcodePush(failedItemList, pushInventory);
		}
	}

	private void retryBarcodePush(List<PushInventoryItem> failedItemList, PushInventory pushInventory) throws XMLStreamException {
		BarcodePushRequestRoot root = new BarcodePushRequestRoot();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(BarcodePushRequestItems.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			for (PushInventoryItem item : failedItemList) {
				XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
				xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
				xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
				
				XMLStreamReader xmlStreamReader = xmlInputFactory
		                .createXMLStreamReader(new StringReader(item.getRequestBody()));
				root.setItems(Arrays.asList((BarcodePushRequestItems) jaxbUnmarshaller.unmarshal(xmlStreamReader)));
				BarcodePushResponseRoot responseRoot = wmsApiService.barCodePushData(root);
				updateBarCodePushInventoryStatus(pushInventory, responseRoot);
			}
		} catch (JAXBException e) {
			pushInventory.setStatus(StatusType.FAILED);
			for (PushInventoryItem item : pushInventory.getPushInventoryItems()) {
				if (!item.getStatus().equals(StatusType.SUCCESS))
					item.setStatus(StatusType.FAILED);
			}
		} catch (CustomException e) {
			pushInventory.setStatus(StatusType.FAILED);
			for (PushInventoryItem item : pushInventory.getPushInventoryItems()) {
				if (!item.getStatus().equals(StatusType.SUCCESS))
					item.setStatus(StatusType.FAILED);
			}
		} finally {
			save(pushInventory);
		}
	}

	@Override
	public List<PushInventory> fetchPushedInventory() {
		return pushInventoryRepository.findByStatus(StatusType.SUCCESS);
	}

	@Override
	public void deletePushedInventory(PushInventory pushInventory) {
		pushInventoryRepository.delete(pushInventory);
	}

	private void retryOutboundPushInventory(List<PushInventoryItem> failedItemList, PushInventory pushInventory) {
		OutboundPushRequestRoot requestRoot = new OutboundPushRequestRoot();
		List<OutboundPushRequestItems> requestItems = new ArrayList<OutboundPushRequestItems>();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(OutboundPushRequestItems.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			for (PushInventoryItem item : failedItemList) {
				XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
				xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
				xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
				XMLStreamReader xmlStreamReader = xmlInputFactory
		                .createXMLStreamReader(new StringReader(item.getRequestBody()));
				requestItems.add((OutboundPushRequestItems) jaxbUnmarshaller.unmarshal(xmlStreamReader));
			}
			requestRoot.setItems(requestItems);
			requestRoot.setFacilityIdCode(pushInventory.getFacilityIdCode() != null ? pushInventory.getFacilityIdCode() : "");
			requestRoot.setBarcode(pushInventory.getBarcode() != null ? pushInventory.getBarcode() : "");
			
			// removing existing FAILED OutboundPushRequestItems from OutboundPushRequestRoot
			ListIterator<OutboundPushRequestItems> iter = requestRoot.getItems().listIterator();
			OutboundPushRequestItems item = null;
			List<PushInventory> inventories = null;
			while (iter.hasNext()) {
				item = iter.next();
				inventories = pushInventoryRepository.findByInbDeclarationAndSkuBarcodeAndStatusAndLessThanId(item.getInbDeclarationNumber(), item.getSkuBarcode(), StatusType.FAILED,
						pushInventory.getId());
				if (inventories != null && !inventories.isEmpty()) {
					logger.info("REMOVED :: removed PushRequestItems : {}", item);
					iter.remove();
				}
			}

			// if there is no OutboundPushRequestItems exist in OutboundPushRequestRoot then do not call PUSH API
			if (!requestRoot.getItems().isEmpty()) {
				OutboundPushResponseRoot responseRoot = wmsApiService.outboundPushData(requestRoot);
				if (responseRoot != null && responseRoot.getItems() != null) {
					for (PushInventoryItem failedItem : failedItemList) {
						OutboundPushResponseItems responseItem = responseRoot.getItems().stream().filter(p -> p.getBarCode() != null && p.getBarCode().equalsIgnoreCase(failedItem.getSkuBarCode())
								&& p.getOutboundReferenceNumber() != null && p.getOutboundReferenceNumber().equalsIgnoreCase(failedItem.getReferenceNumber())).findFirst().orElse(null);
						if (responseItem != null) {
							if (responseItem.getStatus().equalsIgnoreCase("SUCCESS"))
								failedItem.setStatus(StatusType.SUCCESS);
							else
								failedItem.setStatus(StatusType.FAILED);
							failedItem.setResponseMessage(Utils.objectToXml(responseItem));
							failedItem.setLastPushedDate(LocalDateTime.now());
							failedItem.setRetryCount(failedItem.getRetryCount() + 1);
							pushInventoryItemRepository.save(failedItem);
						}
					}
				}
				updatePushInventoryStatus(pushInventory.getId());
			}
			
		} catch (Exception e) {
			logger.error("ERROR :: Error occurred in Retry Outbound : {}", e);
			pushInventory.setStatus(StatusType.FAILED);
			for (PushInventoryItem item : pushInventory.getPushInventoryItems()) {
				if (!item.getStatus().equals(StatusType.SUCCESS))
					item.setStatus(StatusType.FAILED);
			}
		} finally {
			save(pushInventory);
		}
	}

	private void retryReturnLocalGoodsPushInventory(List<PushInventoryItem> failedItemList, PushInventory pushInventory) {
		ReturnLocalGoodsPushRequestRoot requestRoot = new ReturnLocalGoodsPushRequestRoot();
		List<ReturnLocalGoodsPushRequestItems> requestItems = new ArrayList<ReturnLocalGoodsPushRequestItems>();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ReturnLocalGoodsPushRequestItems.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			for (PushInventoryItem item : failedItemList) {
				XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
				xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
				xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
				XMLStreamReader xmlStreamReader = xmlInputFactory
		                .createXMLStreamReader(new StringReader(item.getRequestBody()));
				requestItems.add((ReturnLocalGoodsPushRequestItems) jaxbUnmarshaller.unmarshal(xmlStreamReader));
			}
			requestRoot.setItems(requestItems);
			requestRoot.setFacilityIdCode(pushInventory.getFacilityIdCode() != null ? pushInventory.getFacilityIdCode() : "");
			requestRoot.setBarcode(pushInventory.getBarcode() != null ? pushInventory.getBarcode() : "");
			
			// removing existing FAILED ReturnLocalGoodsPushRequestItems from ReturnLocalGoodsPushRequestRoot
			ListIterator<ReturnLocalGoodsPushRequestItems> iter = requestRoot.getItems().listIterator();
			ReturnLocalGoodsPushRequestItems item = null;
			List<PushInventory> inventories = null;
			while (iter.hasNext()) {
				item = iter.next();
				inventories = pushInventoryRepository.findByInbDeclarationAndSkuBarcodeAndStatusAndLessThanId(item.getInbDeclarationNumber(), item.getSkuBarcode(), StatusType.FAILED,
						pushInventory.getId());
				if (inventories != null && !inventories.isEmpty()) {
					logger.info("REMOVED :: removed PushRequestItems : {}", item);
					iter.remove();
				}
			}

			// if there is no ReturnLocalGoodsPushRequestItems exist in ReturnLocalGoodsPushRequestRoot then do not call PUSH API
			if (!requestRoot.getItems().isEmpty()) {
				ReturnLocalGoodsPushResponseRoot responseRoot = wmsApiService.returnLocalGoodsPushData(requestRoot);
				if (responseRoot != null && responseRoot.getItems() != null) {
					for (PushInventoryItem failedItem : failedItemList) {
						ReturnLocalGoodsPushResponseItems responseItem = responseRoot.getItems().stream().filter(p -> p.getBarCode() != null && p.getBarCode().equalsIgnoreCase(failedItem.getSkuBarCode())
								&& p.getReturnLocalGoodReferenceNumber() != null && p.getReturnLocalGoodReferenceNumber().equalsIgnoreCase(failedItem.getReferenceNumber())).findFirst().orElse(null);
						if (responseItem != null) {
							if (responseItem.getStatus().equalsIgnoreCase("SUCCESS"))
								failedItem.setStatus(StatusType.SUCCESS);
							else
								failedItem.setStatus(StatusType.FAILED);
							failedItem.setResponseMessage(Utils.objectToXml(responseItem));
							failedItem.setLastPushedDate(LocalDateTime.now());
							failedItem.setRetryCount(failedItem.getRetryCount() + 1);
							pushInventoryItemRepository.save(failedItem);
						}
					}
				}
				updatePushInventoryStatus(pushInventory.getId());
			}
			
		} catch (Exception e) {
			logger.error("ERROR :: Error occurred in Retry ReturnLocalGoods : {}", e);
			pushInventory.setStatus(StatusType.FAILED);
			for (PushInventoryItem item : pushInventory.getPushInventoryItems()) {
				if (!item.getStatus().equals(StatusType.SUCCESS))
					item.setStatus(StatusType.FAILED);
			}
		} finally {
			save(pushInventory);
		}
	}

	private void retryReturnGoodsPushInventory(List<PushInventoryItem> failedItemList, PushInventory pushInventory) {
		ReturnGoodsPushRequestRoot requestRoot = new ReturnGoodsPushRequestRoot();
		List<ReturnGoodsPushRequestItems> requestItems = new ArrayList<ReturnGoodsPushRequestItems>();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ReturnGoodsPushRequestItems.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			for (PushInventoryItem item : failedItemList) {
				XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
				xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
				xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
				XMLStreamReader xmlStreamReader = xmlInputFactory
		                .createXMLStreamReader(new StringReader(item.getRequestBody()));
				requestItems.add((ReturnGoodsPushRequestItems) jaxbUnmarshaller.unmarshal(xmlStreamReader));
			}
			requestRoot.setItems(requestItems);
			requestRoot.setFacilityIdCode(pushInventory.getFacilityIdCode() != null ? pushInventory.getFacilityIdCode() : "");
			requestRoot.setBarcode(pushInventory.getBarcode() != null ? pushInventory.getBarcode() : "");
			
			// removing existing FAILED ReturnGoodsPushRequestItems from ReturnGoodsPushRequestRoot
			ListIterator<ReturnGoodsPushRequestItems> iter = requestRoot.getItems().listIterator();
			ReturnGoodsPushRequestItems item = null;
			List<PushInventory> inventories = null;
			while (iter.hasNext()) {
				item = iter.next();
				inventories = pushInventoryRepository.findByInbDeclarationAndSkuBarcodeAndStatusAndLessThanId(item.getInbDeclarationNumber(), item.getSkuBarcode(), StatusType.FAILED,
						pushInventory.getId());
				if (inventories != null && !inventories.isEmpty()) {
					logger.info("REMOVED :: removed PushRequestItems : {}", item);
					iter.remove();
				}
			}

			// if there is no ReturnGoodsPushRequestItems exist in ReturnGoodsPushRequestRoot then do not call PUSH API
			if (!requestRoot.getItems().isEmpty()) {
				ReturnGoodsPushResponseRoot responseRoot = wmsApiService.returnGoodsPushData(requestRoot);
				if (responseRoot != null && responseRoot.getItems() != null) {
					for (PushInventoryItem failedItem : failedItemList) {
						ReturnGoodsPushResponseItems responseItem = responseRoot.getItems().stream().filter(p -> p.getBarCode() != null && p.getBarCode().equalsIgnoreCase(failedItem.getSkuBarCode())
								&& p.getReturnGoodReferenceNumber() != null && p.getReturnGoodReferenceNumber().equalsIgnoreCase(failedItem.getReferenceNumber())).findFirst().orElse(null);
						if (responseItem != null) {
							if (responseItem.getStatus().equalsIgnoreCase("SUCCESS"))
								failedItem.setStatus(StatusType.SUCCESS);
							else
								failedItem.setStatus(StatusType.FAILED);
							failedItem.setResponseMessage(Utils.objectToXml(responseItem));
							failedItem.setLastPushedDate(LocalDateTime.now());
							failedItem.setRetryCount(failedItem.getRetryCount() + 1);
							pushInventoryItemRepository.save(failedItem);
						}
					}
				}
				updatePushInventoryStatus(pushInventory.getId());
			}
			
		} catch (Exception e) {
			logger.error("ERROR :: Error occurred in Retry ReturnGoods : {}", e);
			pushInventory.setStatus(StatusType.FAILED);
			for (PushInventoryItem item : pushInventory.getPushInventoryItems()) {
				if (!item.getStatus().equals(StatusType.SUCCESS))
					item.setStatus(StatusType.FAILED);
			}
		} finally {
			save(pushInventory);
		}
	}

	private void retryInboundPushInventory(List<PushInventoryItem> failedItemList, PushInventory pushInventory) {
		InboundPushRequestRoot requestRoot = new InboundPushRequestRoot();
		List<InboundPushRequestItems> requestItems = new ArrayList<>();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(InboundPushRequestItems.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			
			for (PushInventoryItem item : failedItemList) {
				XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
				xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
				xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
				XMLStreamReader xmlStreamReader = xmlInputFactory
		                .createXMLStreamReader(new StringReader(item.getRequestBody()));
				requestItems.add((InboundPushRequestItems) jaxbUnmarshaller.unmarshal(xmlStreamReader));
			}
			requestRoot.setItems(requestItems);
			requestRoot.setFacilityIdCode(pushInventory.getFacilityIdCode() != null ? pushInventory.getFacilityIdCode() : "");
			requestRoot.setBarcode(pushInventory.getBarcode() != null ? pushInventory.getBarcode() : "");
			
			if (!pushInventory.getOperationType().equals(OperationType.CREATE)) {
				ListIterator<InboundPushRequestItems> iter = requestRoot.getItems().listIterator();
				InboundPushRequestItems item = null;
				List<PushInventory> inventories = null;
				while (iter.hasNext()) {
					item = iter.next();
					inventories = pushInventoryRepository.findByInbDeclarationAndSkuBarcodeAndStatusAndLessThanId(item.getDeclarationNumber(), item.getSkuBarcode(), StatusType.FAILED,
							pushInventory.getId());
					if (inventories != null && !inventories.isEmpty()) {
						logger.info("REMOVED :: removed PushRequestItems : {}", item);
						iter.remove();
					}
				}
			}

			// if there is no InboundPushRequestItems exist in InboundPushRequestRoot then do not call PUSH API
			if (!requestRoot.getItems().isEmpty()) {
				InboundPushResponseRoot responseRoot = wmsApiService.inboundPushData(requestRoot);
				if (responseRoot != null && responseRoot.getItems() != null) {
					for (PushInventoryItem failedItem : failedItemList) {
						InboundPushResponseItems item = responseRoot.getItems().stream().filter(p -> p.getBarCode() != null && p.getBarCode().equalsIgnoreCase(failedItem.getSkuBarCode())
								&& p.getInboundReferenceNumber() != null && p.getInboundReferenceNumber().equalsIgnoreCase(failedItem.getReferenceNumber())).findFirst().orElse(null);
						if (item != null) {
							if (item.getStatus().equalsIgnoreCase("SUCCESS"))
								failedItem.setStatus(StatusType.SUCCESS);
							else
								failedItem.setStatus(StatusType.FAILED);
							failedItem.setResponseMessage(Utils.objectToXml(item));
							failedItem.setLastPushedDate(LocalDateTime.now());
							failedItem.setRetryCount(failedItem.getRetryCount() + 1);
							pushInventoryItemRepository.save(failedItem);
						}
					}
				}
				updatePushInventoryStatus(pushInventory.getId());
			}
			
		} catch (Exception e) {
			logger.error("ERROR :: Error occurred in Retry Inbound : {}", e);
			pushInventory.setStatus(StatusType.FAILED);
			for (PushInventoryItem item : pushInventory.getPushInventoryItems()) {
				if (!item.getStatus().equals(StatusType.SUCCESS))
					item.setStatus(StatusType.FAILED);
			}
		} finally {
			save(pushInventory);
		}
	}
	
	@Override
	public void updatePushInventoryStatus(Long id) {
		PushInventory inventory = pushInventoryRepository.getOne(id);
		Boolean isFailed = false, isPending = false;
		
		if (inventory != null && inventory.getPushInventoryItems() != null && !inventory.getPushInventoryItems().isEmpty()) {
			for (PushInventoryItem item : inventory.getPushInventoryItems()) {
				if (item.getStatus().equals(StatusType.FAILED)) {
					isFailed = true;
					break;
				}
				if (item.getStatus().equals(StatusType.PENDING))
					isPending = true;
			}
		}

		inventory.setStatus(isFailed ? StatusType.FAILED : (isPending ? StatusType.PENDING : StatusType.SUCCESS));
		pushInventoryRepository.save(inventory);
	}
	
	@Override
	public ReturnLocalGoodsPushRequestRoot createOrUpdateReturnLocalGoodsPushRequest(Inbound inbound, List<InbCargoInformation> oldCargoList, List<Long> deletedCargosIds, List<Long> deletedBarcodeDetails) {
		//logger.info("ENTRY :: createOrUpdateReturnLocalGoodsPushRequest with request : {}, {}, {}", oldCargoList, deletedCargosIds, deletedBarcodeDetails);
		List<InbCargoInformation> newCargoList = inbound.getInbCargosInformation();

		ReturnLocalGoodsPushRequestRoot root = new ReturnLocalGoodsPushRequestRoot();
		List<ReturnLocalGoodsPushRequestItems> items = new ArrayList<ReturnLocalGoodsPushRequestItems>();

		if (deletedCargosIds != null && !deletedCargosIds.isEmpty()) {
			List<InbCargoInformation> deletedCargoList = oldCargoList.stream().filter(p -> deletedCargosIds.contains(p.getId())).collect(Collectors.toList());

			if (deletedCargoList != null && !deletedCargoList.isEmpty()) {

				for (InbCargoInformation deletedCargo : deletedCargoList) {

					// these cargo is deleted from DB, so delete it from WMS
					if (deletedCargo.getInbBarcodeDetails() != null && deletedCargo.getInbBarcodeDetails().isEmpty()) {
						for (InbBarCodeDetails deletedBarcode : deletedCargo.getInbBarcodeDetails()) {
							items.add(createReturnLocalGoodsPushRequestItem(inbound, deletedCargo, deletedBarcode, -deletedBarcode.getPiecesQuantity()));
						}
					} else {
						items.add(createReturnLocalGoodsPushRequestItem(inbound, deletedCargo, null, -deletedCargo.getPiecesQuantity()));
					}
				}
			}
		}

		InbCargoInformation oldCargo = null;
		List<InbBarCodeDetails> oldBarcodeList = null;
		List<InbBarCodeDetails> newBarcodeList = null;
		Double difference = 0.0; 

		for (InbCargoInformation newCargo : newCargoList) {

			if (oldCargoList != null && !oldCargoList.isEmpty())
				oldCargo = oldCargoList.stream().filter(p -> p.getId() == newCargo.getId()).findFirst().orElse(null);

			newBarcodeList = newCargo.getInbBarcodeDetails();
			if (oldCargo != null) {
				// this cargo is exist in DB and updating its values and barcodes, so update it accordingly in WMS as follow

				oldBarcodeList = oldCargo.getInbBarcodeDetails();

				if (newBarcodeList != null && !newBarcodeList.isEmpty() && oldBarcodeList != null && !oldBarcodeList.isEmpty()) {

					for (InbBarCodeDetails newBarcode : newBarcodeList) {
						InbBarCodeDetails oldBarcode = oldBarcodeList.stream().filter(p -> p.getInbBarcodeDetails() == newBarcode.getInbBarcodeDetails()).findFirst().orElse(null);
						if (oldBarcode != null) {

							// this barcode is exist in DB and updating its values, so update its values in WMS
							difference = newBarcode.getPiecesQuantity() - oldBarcode.getPiecesQuantity();
							if (difference != 0.0)
								items.add(createReturnLocalGoodsPushRequestItem(inbound, newBarcode.getInbCargoInformationId(), newBarcode,
										newBarcode.getPiecesQuantity() - oldBarcode.getPiecesQuantity()));
						} else {

							// this barcode is new entry in DB, so add new in WMS
							items.add(createReturnLocalGoodsPushRequestItem(inbound, newBarcode.getInbCargoInformationId(), newBarcode, newBarcode.getPiecesQuantity()));
						}
					}

					for (InbBarCodeDetails oldBarcode : oldBarcodeList) {
						if (deletedBarcodeDetails.contains(oldBarcode.getInbBarcodeDetails())) {

							// this barcode is deleted from this cargo information, so remove it from WMS
							items.add(createReturnLocalGoodsPushRequestItem(inbound, oldBarcode.getInbCargoInformationId(), oldBarcode, -oldBarcode.getPiecesQuantity()));
						}
					}

				} else if ((oldBarcodeList != null && !oldBarcodeList.isEmpty()) && (newBarcodeList == null || newBarcodeList.isEmpty())) {

					// this cargo had barcode details before, but now all barcodes are removed and only cargo it self has quantity
					// so remove all these old barcodes from WMS
					// and add only cargo in WMS

					for (InbBarCodeDetails oldBarcode : oldBarcodeList) {
						if (deletedBarcodeDetails.contains(oldBarcode.getInbBarcodeDetails())) {
							// "oldBarcodeList" ids should be available in deletedBarcodeDetails, thats why
							// old object had barcodes and new object dont have barcodes.
							items.add(createReturnLocalGoodsPushRequestItem(inbound, oldBarcode.getInbCargoInformationId(), oldBarcode, -oldBarcode.getPiecesQuantity()));
						}
					}
					items.add(createReturnLocalGoodsPushRequestItem(inbound, newCargo, null, newCargo.getPiecesQuantity()));

				} else if ((oldBarcodeList == null || oldBarcodeList.isEmpty()) && (newBarcodeList != null && !newBarcodeList.isEmpty())) {

					// this cargo didnt have barcode details before, but now barcode details added to this cargo.
					// so first remove only cargo from WMS which was added earlier and then add new barcodes in WMS

					items.add(createReturnLocalGoodsPushRequestItem(inbound, oldCargo, null, -oldCargo.getPiecesQuantity()));

					for (InbBarCodeDetails newBarcode : newBarcodeList) {
						items.add(createReturnLocalGoodsPushRequestItem(inbound, newBarcode.getInbCargoInformationId(), newBarcode, newBarcode.getPiecesQuantity()));
					}

				} else if ((oldBarcodeList == null || oldBarcodeList.isEmpty()) && (newBarcodeList == null || newBarcodeList.isEmpty())) {

					// this cargo didnt have barcode details before and also it dont have barcode details now
					// it is just updating its quantity values that cargo it self has, so update in WMS also
					difference = newCargo.getPiecesQuantity() - oldCargo.getPiecesQuantity();
					if (difference != 0.0)
						items.add(createReturnLocalGoodsPushRequestItem(inbound, newCargo, null, newCargo.getPiecesQuantity() - oldCargo.getPiecesQuantity()));

				}
			} else {
				// this cargo is complete new entry in DB, so add its values in WMS

				if (newBarcodeList != null && !newBarcodeList.isEmpty()) {

					// this is new cargo and it has barcodes, all its values are newly added in DB, so add those barcodes to WMS
					for (InbBarCodeDetails newBarcode : newBarcodeList) {
						items.add(createReturnLocalGoodsPushRequestItem(inbound, newBarcode.getInbCargoInformationId(), newBarcode, newBarcode.getPiecesQuantity()));
					}

				} else if (newBarcodeList == null || newBarcodeList.isEmpty()) {
					// this is new cargo but it doesnt have barcodes, only hscode value is added in DB, so add only cargo to WMS
					items.add(createReturnLocalGoodsPushRequestItem(inbound, newCargo, null, newCargo.getPiecesQuantity()));
				}
			}

		}
		root.setItems(items);
		root.setBarcode(inbound.getInbDeclarationInformation() != null ? inbound.getInbDeclarationInformation().getBusinessCode() : "");
		root.setFacilityIdCode("");

//		logger.info("INFO :: createOrUpdateReturnLocalGoodsPushRequest with ReturnLocalGoodsPushRequestRoot: {}", Utils.objectToXml(root));
		return root;
	}
	
	@Override
	public ReturnLocalGoodsPushRequestRoot cancelReturnLocalGoodsPushRequest(Inbound inbound, List<InbCargoInformation> cancelledCargoList) {

		ReturnLocalGoodsPushRequestRoot root = new ReturnLocalGoodsPushRequestRoot();
		List<ReturnLocalGoodsPushRequestItems> items = new ArrayList<ReturnLocalGoodsPushRequestItems>();

		if (cancelledCargoList != null && !cancelledCargoList.isEmpty()) {

			for (InbCargoInformation cancelledCargo : cancelledCargoList) {

				if (cancelledCargo.getInbBarcodeDetails() != null && !cancelledCargo.getInbBarcodeDetails().isEmpty()) {
					for (InbBarCodeDetails cancelledBarcode : cancelledCargo.getInbBarcodeDetails()) {
						items.add(createReturnLocalGoodsPushRequestItem(inbound, cancelledCargo, cancelledBarcode, -cancelledBarcode.getPiecesQuantity()));
					}
				} else {
					items.add(createReturnLocalGoodsPushRequestItem(inbound, cancelledCargo, null, -cancelledCargo.getPiecesQuantity()));
				}
			}
		}

		root.setItems(items);
		root.setBarcode(inbound.getInbDeclarationInformation() != null ? inbound.getInbDeclarationInformation().getBusinessCode() : "");
		root.setFacilityIdCode("");

//		logger.info("INFO :: cancelReturnLocalGoodsPushRequest with ReturnLocalGoodsPushRequestRoot: {}", Utils.objectToXml(root));
		return root;
	}
	
	@Override
	public PushInventory createReturnLocalGoodsPushInventory(ReturnLocalGoodsPushRequestRoot requestRoot, Inbound inbound, OperationType operationType) {
		PushInventory pushInventory = new PushInventory();
		pushInventory.setInventoryType(InventoryType.RETURNLOCALGOODS);
		pushInventory.setCreatedDate(LocalDateTime.now());
		pushInventory.setLastPushedDate(LocalDateTime.now());
		pushInventory.setCreatedBy(inbound.getCreatedBy());
		pushInventory.setInventoryId(inbound.getInboundId());
		pushInventory.setBarcode(requestRoot.getBarcode());
		pushInventory.setFacilityIdCode(requestRoot.getFacilityIdCode());
		pushInventory.setOperationType(operationType);
		pushInventory.setInbDeclarationNumber(inbound.getInbDeclarationInformation().getDeclarationNumber());
		pushInventory.setRetryCount(1);
		
		for (ReturnLocalGoodsPushRequestItems item : requestRoot.getItems()) {
			PushInventoryItem pushInventoryItem = new PushInventoryItem();
			pushInventoryItem.setPushInventory(pushInventory);
			pushInventoryItem.setRequestBody(Utils.objectToXml(item));
			pushInventoryItem.setReferenceNumber(item.getReturnLocalGoodsReferenceNumber());
			pushInventoryItem.setSkuBarCode(item.getSkuBarcode());
			pushInventoryItem.setLastPushedDate(LocalDateTime.now());
			pushInventoryItem.setStatus(StatusType.PENDING);
			pushInventoryItem.setRetryCount(1);
			pushInventory.getPushInventoryItems().add(pushInventoryItem);
		}
		pushInventory.setStatus(StatusType.PENDING);
		return pushInventoryRepository.save(pushInventory);
	}
	
	@Override
	public UpdatePushInventoryStatusResponse updateReturnLocalGoodsPushInventoryStatus(PushInventory pushInventory, ReturnLocalGoodsPushResponseRoot responseRoot) {
		UpdatePushInventoryStatusResponse response = new UpdatePushInventoryStatusResponse(); 
		int successCount = 0, failedCount = 0;
		List<String> list = new ArrayList<String>();
		if (responseRoot != null) {

			List<ReturnLocalGoodsPushResponseItems> responseItems = responseRoot.getItems();
			Boolean isFailed = false, isPending = false;
			for (PushInventoryItem item : pushInventory.getPushInventoryItems()) {

				ReturnLocalGoodsPushResponseItems responseItem = responseItems.stream()
						.filter(p -> (p.getBarCode() != null && p.getBarCode().equals(item.getSkuBarCode()) && p.getReturnLocalGoodReferenceNumber().equals(item.getReferenceNumber()))).findFirst()
						.orElse(null);

				if (responseItem != null) {
					if (responseItem.getStatus() == null || responseItem.getStatus().trim().isEmpty() || responseItem.getStatus().equalsIgnoreCase("ERROR")) {
						list.add(responseItem.getBarCode() + " : " + responseItem.getMessage());
						item.setStatus(StatusType.FAILED);
						isFailed = true;
						failedCount++;
					} else if (responseItem.getStatus() != null && responseItem.getStatus().equalsIgnoreCase("SUCCESS")) {
						item.setStatus(StatusType.SUCCESS);
						list.add(responseItem.getBarCode() + " : Pushed successfully to Oracle");
						successCount++;
					}
					item.setResponseMessage(Utils.objectToXml(responseItem));
				}
				
				if (item.getStatus().equals(StatusType.PENDING))
					isPending = true;
			}

			pushInventory.setStatus(isFailed ? StatusType.FAILED : (isPending ? StatusType.PENDING : StatusType.SUCCESS));
			pushInventoryRepository.save(pushInventory);

		} else {
			list.add(MasterDataAppConstants.SOMETHING_WENT_WRONG);
		}
		response.setFailedCount(failedCount);
		response.setSuccessCount(successCount);
		response.setMessages(list);
		return response;
	}
	
	@Override
	public ReturnGoodsPushRequestRoot createOrUpdateReturnGoodsPushRequest(Inbound inbound, List<InbCargoInformation> oldCargoList, List<Long> deletedCargosIds, List<Long> deletedBarcodeDetails) {
		//logger.info("ENTRY :: createOrUpdateReturnGoodsPushRequest with request : {}, {}, {}", oldCargoList, deletedCargosIds, deletedBarcodeDetails);
		List<InbCargoInformation> newCargoList = inbound.getInbCargosInformation();

		ReturnGoodsPushRequestRoot root = new ReturnGoodsPushRequestRoot();
		List<ReturnGoodsPushRequestItems> items = new ArrayList<ReturnGoodsPushRequestItems>();

		if (deletedCargosIds != null && !deletedCargosIds.isEmpty()) {
			List<InbCargoInformation> deletedCargoList = oldCargoList.stream().filter(p -> deletedCargosIds.contains(p.getId())).collect(Collectors.toList());

			if (deletedCargoList != null && !deletedCargoList.isEmpty()) {

				for (InbCargoInformation deletedCargo : deletedCargoList) {

					// these cargo is deleted from DB, so delete it from WMS
					if (deletedCargo.getInbBarcodeDetails() != null && deletedCargo.getInbBarcodeDetails().isEmpty()) {
						for (InbBarCodeDetails deletedBarcode : deletedCargo.getInbBarcodeDetails()) {
							items.add(createReturnGoodsPushRequestItem(inbound, deletedCargo, deletedBarcode, -deletedBarcode.getPiecesQuantity()));
						}
					} else {
						items.add(createReturnGoodsPushRequestItem(inbound, deletedCargo, null, -deletedCargo.getPiecesQuantity()));
					}
				}
			}
		}

		InbCargoInformation oldCargo = null;
		List<InbBarCodeDetails> oldBarcodeList = null;
		List<InbBarCodeDetails> newBarcodeList = null;
		Double difference = 0.0; 
		
		for (InbCargoInformation newCargo : newCargoList) {
			if (oldCargoList != null && !oldCargoList.isEmpty()) {
				oldCargo = oldCargoList.stream().filter(p -> p.getId() == newCargo.getId()).findFirst().orElse(null);
			}
			newBarcodeList = newCargo.getInbBarcodeDetails();
			if (oldCargo != null) {
				// this cargo is exist in DB and updating its values and barcodes, so update it accordingly in WMS as follow

				oldBarcodeList = oldCargo.getInbBarcodeDetails();

				if (newBarcodeList != null && !newBarcodeList.isEmpty() && oldBarcodeList != null && !oldBarcodeList.isEmpty()) {

					for (InbBarCodeDetails newBarcode : newBarcodeList) {
						InbBarCodeDetails oldBarcode = oldBarcodeList.stream().filter(p -> p.getInbBarcodeDetails() == newBarcode.getInbBarcodeDetails()).findFirst().orElse(null);
						if (oldBarcode != null) {

							// this barcode is exist in DB and updating its values, so update its values in WMS
							difference = newBarcode.getPiecesQuantity() - oldBarcode.getPiecesQuantity();
							if (difference != 0.0)
								items.add(createReturnGoodsPushRequestItem(inbound, newBarcode.getInbCargoInformationId(), newBarcode, newBarcode.getPiecesQuantity() - oldBarcode.getPiecesQuantity()));
						} else {

							// this barcode is new entry in DB, so add new in WMS
							items.add(createReturnGoodsPushRequestItem(inbound, newBarcode.getInbCargoInformationId(), newBarcode, newBarcode.getPiecesQuantity()));
						}
					}

					for (InbBarCodeDetails oldBarcode : oldBarcodeList) {
						if (deletedBarcodeDetails.contains(oldBarcode.getInbBarcodeDetails())) {

							// this barcode is deleted from this cargo information, so remove it from WMS
							items.add(createReturnGoodsPushRequestItem(inbound, oldBarcode.getInbCargoInformationId(), oldBarcode, -oldBarcode.getPiecesQuantity()));
						}
					}

				} else if ((oldBarcodeList != null && !oldBarcodeList.isEmpty()) && (newBarcodeList == null || newBarcodeList.isEmpty())) {

					// this cargo had barcode details before, but now all barcodes are removed and only cargo it self has quantity
					// so remove all these old barcodes from WMS
					// and add only cargo in WMS

					for (InbBarCodeDetails oldBarcode : oldBarcodeList) {
						if (deletedBarcodeDetails.contains(oldBarcode.getInbBarcodeDetails())) {
							// "oldBarcodeList" ids should be available in deletedBarcodeDetails, thats why
							// old object had barcodes and new object dont have barcodes.
							items.add(createReturnGoodsPushRequestItem(inbound, oldBarcode.getInbCargoInformationId(), oldBarcode, -oldBarcode.getPiecesQuantity()));
						}
					}
					items.add(createReturnGoodsPushRequestItem(inbound, newCargo, null, newCargo.getPiecesQuantity()));

				} else if ((oldBarcodeList == null || oldBarcodeList.isEmpty()) && (newBarcodeList != null && !newBarcodeList.isEmpty())) {

					// this cargo didnt have barcode details before, but now barcode details added to this cargo.
					// so first remove only cargo from WMS which was added earlier and then add new barcodes in WMS

					items.add(createReturnGoodsPushRequestItem(inbound, oldCargo, null, -oldCargo.getPiecesQuantity()));

					for (InbBarCodeDetails newBarcode : newBarcodeList) {
						items.add(createReturnGoodsPushRequestItem(inbound, newBarcode.getInbCargoInformationId(), newBarcode, newBarcode.getPiecesQuantity()));
					}

				} else if ((oldBarcodeList == null || oldBarcodeList.isEmpty()) && (newBarcodeList == null || newBarcodeList.isEmpty())) {

					// this cargo didnt have barcode details before and also it dont have barcode details now
					// it is just updating its quantity values that cargo it self has, so update in WMS also
					difference = newCargo.getPiecesQuantity() - oldCargo.getPiecesQuantity();
					if (difference != 0.0)
						items.add(createReturnGoodsPushRequestItem(inbound, newCargo, null, newCargo.getPiecesQuantity() - oldCargo.getPiecesQuantity()));

				}
			} else {
				// this cargo is complete new entry in DB, so add its values in WMS

				if (newBarcodeList != null && !newBarcodeList.isEmpty()) {

					// this is new cargo and it has barcodes, all its values are newly added in DB, so add those barcodes to WMS
					for (InbBarCodeDetails newBarcode : newBarcodeList) {
						items.add(createReturnGoodsPushRequestItem(inbound, newBarcode.getInbCargoInformationId(), newBarcode, newBarcode.getPiecesQuantity()));
					}

				} else if (newBarcodeList == null || newBarcodeList.isEmpty()) {
					// this is new cargo but it doesnt have barcodes, only hscode value is added in DB, so add only cargo to WMS
					items.add(createReturnGoodsPushRequestItem(inbound, newCargo, null, newCargo.getPiecesQuantity()));
				}
			}

		}
		root.setItems(items);
		root.setBarcode(inbound.getInbDeclarationInformation() != null ? inbound.getInbDeclarationInformation().getBusinessCode() : "");
		root.setFacilityIdCode("");

//		logger.info("INFO :: createOrUpdateReturnGoodsPushRequest with ReturnGoodsPushRequestRoot: {}", Utils.objectToXml(root));
		return root;
	}
	
	@Override
	public ReturnGoodsPushRequestRoot cancelReturnGoodsPushRequest(Inbound inbound, List<InbCargoInformation> cancelledCargoList) {

		ReturnGoodsPushRequestRoot root = new ReturnGoodsPushRequestRoot();
		List<ReturnGoodsPushRequestItems> items = new ArrayList<ReturnGoodsPushRequestItems>();

		if (cancelledCargoList != null && !cancelledCargoList.isEmpty()) {

			for (InbCargoInformation cancelledCargo : cancelledCargoList) {

				if (cancelledCargo.getInbBarcodeDetails() != null && !cancelledCargo.getInbBarcodeDetails().isEmpty()) {
					for (InbBarCodeDetails cancelledBarcode : cancelledCargo.getInbBarcodeDetails()) {
						items.add(createReturnGoodsPushRequestItem(inbound, cancelledCargo, cancelledBarcode, -cancelledBarcode.getPiecesQuantity()));
					}
				} else {
					items.add(createReturnGoodsPushRequestItem(inbound, cancelledCargo, null, -cancelledCargo.getPiecesQuantity()));
				}
			}
		}

		root.setItems(items);
		root.setBarcode(inbound.getInbDeclarationInformation() != null ? inbound.getInbDeclarationInformation().getBusinessCode() : "");
		root.setFacilityIdCode("");

//		logger.info("INFO :: cancelReturnGoodsPushRequest with ReturnGoodsPushRequestRoot: {}", Utils.objectToXml(root));
		return root;
	}
	
	@Override
	public PushInventory createReturnGoodsPushInventory(ReturnGoodsPushRequestRoot requestRoot, Inbound inbound, OperationType operationType) {
		PushInventory pushInventory = new PushInventory();
		pushInventory.setInventoryType(InventoryType.RETURNGOODS);
		pushInventory.setCreatedDate(LocalDateTime.now());
		pushInventory.setLastPushedDate(LocalDateTime.now());
		pushInventory.setCreatedBy(inbound.getCreatedBy());
		pushInventory.setInventoryId(inbound.getInboundId());
		pushInventory.setBarcode(requestRoot.getBarcode());
		pushInventory.setFacilityIdCode(requestRoot.getFacilityIdCode());
		pushInventory.setOperationType(operationType);
		pushInventory.setInbDeclarationNumber(inbound.getInbDeclarationInformation().getDeclarationNumber());
		pushInventory.setRetryCount(1);

		for (ReturnGoodsPushRequestItems item : requestRoot.getItems()) {
			PushInventoryItem pushInventoryItem = new PushInventoryItem();
			pushInventoryItem.setPushInventory(pushInventory);
			pushInventoryItem.setRequestBody(Utils.objectToXml(item));
			pushInventoryItem.setReferenceNumber(item.getReturngoodsReferenceNumber());
			pushInventoryItem.setSkuBarCode(item.getSkuBarcode());
			pushInventoryItem.setLastPushedDate(LocalDateTime.now());
			pushInventoryItem.setStatus(StatusType.PENDING);
			pushInventoryItem.setRetryCount(1);
			pushInventory.getPushInventoryItems().add(pushInventoryItem);
		}
		pushInventory.setStatus(StatusType.PENDING);
		return pushInventoryRepository.save(pushInventory);
	}

	@Override
	public UpdatePushInventoryStatusResponse updateReturnGoodsPushInventoryStatus(PushInventory pushInventory, ReturnGoodsPushResponseRoot responseRoot) {
		UpdatePushInventoryStatusResponse response = new UpdatePushInventoryStatusResponse(); 
		int successCount = 0, failedCount = 0;
		List<String> list = new ArrayList<String>();
		if (responseRoot != null) {

			List<ReturnGoodsPushResponseItems> responseItems = responseRoot.getItems();
			Boolean isFailed = false, isPending = false;
			
			for (PushInventoryItem item : pushInventory.getPushInventoryItems()) {

				ReturnGoodsPushResponseItems responseItem = responseItems.stream()
						.filter(p -> (p.getBarCode() != null && p.getBarCode().equals(item.getSkuBarCode()) && p.getReturnGoodReferenceNumber().equals(item.getReferenceNumber()))).findFirst()
						.orElse(null);

				if (responseItem != null) {
					if (responseItem.getStatus() == null || responseItem.getStatus().trim().isEmpty() || responseItem.getStatus().equalsIgnoreCase("ERROR")) {
						list.add(responseItem.getBarCode() + " : " + responseItem.getMessage());
						item.setStatus(StatusType.FAILED);
						isFailed = true;
						failedCount++;
					} else if (responseItem.getStatus() != null && responseItem.getStatus().equalsIgnoreCase("SUCCESS")) {
						item.setStatus(StatusType.SUCCESS);
						list.add(responseItem.getBarCode() + " : Pushed successfully to Oracle");
						successCount++;
					}
					item.setResponseMessage(Utils.objectToXml(responseItem));
				}

				if (item.getStatus().equals(StatusType.PENDING))
					isPending = true;
			}

			pushInventory.setStatus(isFailed ? StatusType.FAILED : (isPending ? StatusType.PENDING : StatusType.SUCCESS));
			pushInventoryRepository.save(pushInventory);

		} else {
			list.add(MasterDataAppConstants.SOMETHING_WENT_WRONG);
		}
		response.setFailedCount(failedCount);
		response.setSuccessCount(successCount);
		response.setMessages(list);
		return response;
	}
	
	@Override
	public InboundPushRequestRoot createOrUpdateInboundPushRequest(Inbound inbound, List<InbCargoInformation> oldCargoList, List<Long> deletedCargosIds, List<Long> deletedBarcodeDetails) {
		//logger.info("ENTRY :: createOrUpdateInboundPushRequest with request: {}, {}, {}", oldCargoList, deletedCargosIds, deletedBarcodeDetails);
		InboundPushRequestRoot root = new InboundPushRequestRoot();
		List<InboundPushRequestItems> items = new ArrayList<InboundPushRequestItems>();

		if (deletedCargosIds != null && !deletedCargosIds.isEmpty()) {
			List<InbCargoInformation> deletedCargoList = oldCargoList.stream().filter(p -> deletedCargosIds.contains(p.getId())).collect(Collectors.toList());

			if (deletedCargoList != null && !deletedCargoList.isEmpty()) {

				for (InbCargoInformation deletedCargo : deletedCargoList) {

					// these cargo is deleted from DB, so delete it from WMS
					if (deletedCargo.getInbBarcodeDetails() != null && !deletedCargo.getInbBarcodeDetails().isEmpty()) {
						for (InbBarCodeDetails deletedBarcode : deletedCargo.getInbBarcodeDetails()) {
							items.add(createInboundPushRequestItem(inbound, deletedCargo, deletedBarcode, -deletedBarcode.getPiecesQuantity()));
						}
					} else {
						items.add(createInboundPushRequestItem(inbound, deletedCargo, null, -deletedCargo.getPiecesQuantity()));
					}
				}
			}
		}

		List<InbCargoInformation> newCargoList = inbound.getInbCargosInformation();
		InbCargoInformation oldCargo = null;
		List<InbBarCodeDetails> oldBarcodeList = null;
		List<InbBarCodeDetails> newBarcodeList = null;
		Double difference = 0.0; 

		for (InbCargoInformation newCargo : newCargoList) {
			if (oldCargoList != null && !oldCargoList.isEmpty()) {
				oldCargo = oldCargoList.stream().filter(p -> p.getId() == newCargo.getId()).findFirst().orElse(null);
			}
			newBarcodeList = newCargo.getInbBarcodeDetails();
			if (oldCargo != null) {
				// this cargo is exist in DB and updating its values and barcodes, so update it accordingly in WMS as follow

				oldBarcodeList = oldCargo.getInbBarcodeDetails();

				if (newBarcodeList != null && !newBarcodeList.isEmpty() && oldBarcodeList != null && !oldBarcodeList.isEmpty()) {

					for (InbBarCodeDetails newBarcode : newBarcodeList) {
						InbBarCodeDetails oldBarcode = oldBarcodeList.stream().filter(p -> p.getInbBarcodeDetails() == newBarcode.getInbBarcodeDetails()).findFirst().orElse(null);
						if (oldBarcode != null) {

							// this barcode is exist in DB and updating its values, so update its values in WMS
							difference = newBarcode.getPiecesQuantity() - oldBarcode.getPiecesQuantity();
							if (difference != 0.0)
								items.add(createInboundPushRequestItem(inbound, newBarcode.getInbCargoInformationId(), newBarcode, newBarcode.getPiecesQuantity() - oldBarcode.getPiecesQuantity()));
						} else {

							// this barcode is new entry in DB, so add new in WMS
							items.add(createInboundPushRequestItem(inbound, newBarcode.getInbCargoInformationId(), newBarcode, newBarcode.getPiecesQuantity()));
						}
					}

					for (InbBarCodeDetails oldBarcode : oldBarcodeList) {
						if (deletedBarcodeDetails.contains(oldBarcode.getInbBarcodeDetails())) {

							// this barcode is deleted from this cargo information, so remove it from WMS
							items.add(createInboundPushRequestItem(inbound, oldBarcode.getInbCargoInformationId(), oldBarcode, -oldBarcode.getPiecesQuantity()));
						}
					}

				} else if ((oldBarcodeList != null && !oldBarcodeList.isEmpty()) && (newBarcodeList == null || newBarcodeList.isEmpty())) {

					// this cargo had barcode details before, but now all barcodes are removed and only cargo it self has quantity
					// so remove all these old barcodes from WMS
					// and add only cargo in WMS

					for (InbBarCodeDetails oldBarcode : oldBarcodeList) {
						if (deletedBarcodeDetails.contains(oldBarcode.getInbBarcodeDetails())) {
							// "oldBarcodeList" ids should be available in deletedBarcodeDetails, thats why
							// old object had barcodes and new object dont have barcodes.
							items.add(createInboundPushRequestItem(inbound, oldBarcode.getInbCargoInformationId(), oldBarcode, -oldBarcode.getPiecesQuantity()));
						}
					}
					items.add(createInboundPushRequestItem(inbound, newCargo, null, newCargo.getPiecesQuantity()));

				} else if ((oldBarcodeList == null || oldBarcodeList.isEmpty()) && (newBarcodeList != null && !newBarcodeList.isEmpty())) {

					// this cargo didnt have barcode details before, but now barcode details added to this cargo.
					// so first remove only cargo from WMS which was added earlier and then add new barcodes in WMS

					items.add(createInboundPushRequestItem(inbound, oldCargo, null, -oldCargo.getPiecesQuantity()));

					for (InbBarCodeDetails newBarcode : newBarcodeList) {
						items.add(createInboundPushRequestItem(inbound, newBarcode.getInbCargoInformationId(), newBarcode, newBarcode.getPiecesQuantity()));
					}

				} else if ((oldBarcodeList == null || oldBarcodeList.isEmpty()) && (newBarcodeList == null || newBarcodeList.isEmpty())) {

					// this cargo didnt have barcode details before and also it dont have barcode details now
					// it is just updating its quantity values that cargo it self has, so update in WMS also
					difference = newCargo.getPiecesQuantity() - oldCargo.getPiecesQuantity();
					if (difference != 0.0)
						items.add(createInboundPushRequestItem(inbound, newCargo, null, newCargo.getPiecesQuantity() - oldCargo.getPiecesQuantity()));

				}
			} else {
				// this cargo is complete new entry in DB, so add its values in WMS

				if (newBarcodeList != null && !newBarcodeList.isEmpty()) {

					// this is new cargo and it has barcodes, all its values are newly added in DB, so add those barcodes to WMS
					for (InbBarCodeDetails newBarcode : newBarcodeList) {
						items.add(createInboundPushRequestItem(inbound, newBarcode.getInbCargoInformationId(), newBarcode, newBarcode.getPiecesQuantity()));
					}

				} else if (newBarcodeList == null || newBarcodeList.isEmpty()) {
					// this is new cargo but it doesnt have barcodes, only hscode value is added in DB, so add only cargo to WMS
					items.add(createInboundPushRequestItem(inbound, newCargo, null, newCargo.getPiecesQuantity()));
				}
			}

		}
		root.setItems(items);
		root.setBarcode(inbound.getInbDeclarationInformation() != null ? inbound.getInbDeclarationInformation().getBusinessCode() : "");
		root.setFacilityIdCode("");

//		logger.info("INFO :: createOrUpdateInboundPushRequest with InboundPushRequestRoot: {}", Utils.objectToXml(root));
		return root;
	}
	
	@Override
	public InboundPushRequestRoot cancelInboundPushRequest(Inbound inbound, List<InbCargoInformation> cancelledCargoList) {

		InboundPushRequestRoot root = new InboundPushRequestRoot();
		List<InboundPushRequestItems> items = new ArrayList<InboundPushRequestItems>();

		if (cancelledCargoList != null && !cancelledCargoList.isEmpty()) {

			for (InbCargoInformation cancelledCargo : cancelledCargoList) {

				if (cancelledCargo.getInbBarcodeDetails() != null && !cancelledCargo.getInbBarcodeDetails().isEmpty()) {
					for (InbBarCodeDetails cancelledBarcode : cancelledCargo.getInbBarcodeDetails()) {
						items.add(createInboundPushRequestItem(inbound, cancelledCargo, cancelledBarcode, -cancelledBarcode.getPiecesQuantity()));
					}
				} else {
					items.add(createInboundPushRequestItem(inbound, cancelledCargo, null, -cancelledCargo.getPiecesQuantity()));
				}
			}
		}
		root.setItems(items);
		root.setBarcode(inbound.getInbDeclarationInformation() != null ? inbound.getInbDeclarationInformation().getBusinessCode() : "");
		root.setFacilityIdCode("");

//		logger.info("INFO :: cancelInboundPushRequest with InboundPushRequestRoot: {}", Utils.objectToXml(root));
		return root;
	}

	@Override
	public PushInventory createInboundPushInventory(InboundPushRequestRoot requestRoot, Inbound inbound, OperationType operationType) {
		PushInventory pushInventory = new PushInventory();
		pushInventory.setInventoryType(InventoryType.INBOUND);
		pushInventory.setCreatedDate(LocalDateTime.now());
		pushInventory.setLastPushedDate(LocalDateTime.now());
		pushInventory.setCreatedBy(inbound.getCreatedBy());
		pushInventory.setInventoryId(inbound.getInboundId());
		pushInventory.setBarcode(requestRoot.getBarcode());
		pushInventory.setFacilityIdCode(requestRoot.getFacilityIdCode());
		pushInventory.setOperationType(operationType);
		pushInventory.setInbDeclarationNumber(inbound.getInbDeclarationInformation().getDeclarationNumber());
		pushInventory.setRetryCount(1);

		for (InboundPushRequestItems item : requestRoot.getItems()) {
			PushInventoryItem pushInventoryItem = new PushInventoryItem();
			pushInventoryItem.setPushInventory(pushInventory);
			pushInventoryItem.setRequestBody(Utils.objectToXml(item));
			pushInventoryItem.setReferenceNumber(item.getInboundReferenceNumber());
			pushInventoryItem.setSkuBarCode(item.getSkuBarcode());
			pushInventoryItem.setLastPushedDate(LocalDateTime.now());
			pushInventoryItem.setStatus(StatusType.PENDING);
			pushInventoryItem.setRetryCount(1);
			pushInventory.getPushInventoryItems().add(pushInventoryItem);
		}
		pushInventory.setStatus(StatusType.PENDING);
		return pushInventoryRepository.save(pushInventory);
	}

	@Override
	public UpdatePushInventoryStatusResponse updateInboundPushInventoryStatus(PushInventory pushInventory, InboundPushResponseRoot responseRoot) {
		UpdatePushInventoryStatusResponse response = new UpdatePushInventoryStatusResponse(); 
		int successCount = 0, failedCount = 0;
		List<String> list = new ArrayList<String>();
		if (responseRoot != null) {

			List<InboundPushResponseItems> responseItems = responseRoot.getItems();
			Boolean isFailed = false, isPending = false;
			for (PushInventoryItem item : pushInventory.getPushInventoryItems()) {

				InboundPushResponseItems responseItem = responseItems.stream()
						.filter(p -> (p.getBarCode() != null && p.getBarCode().equals(item.getSkuBarCode()) && p.getInboundReferenceNumber().equals(item.getReferenceNumber()))).findFirst()
						.orElse(null);

				if (responseItem != null) {
					if (responseItem.getStatus() == null || responseItem.getStatus().trim().isEmpty() || responseItem.getStatus().equalsIgnoreCase("ERROR")) {
						list.add(responseItem.getBarCode() + " : " + responseItem.getMessage());
						item.setStatus(StatusType.FAILED);
						isFailed = true;
						failedCount++;
					} else if (responseItem.getStatus() != null && responseItem.getStatus().equalsIgnoreCase("SUCCESS")) {
						item.setStatus(StatusType.SUCCESS);
						list.add(responseItem.getBarCode() + " : Pushed successfully to Oracle");
						successCount++;
					}
					item.setResponseMessage(Utils.objectToXml(responseItem));
				}
				
				if (item.getStatus().equals(StatusType.PENDING))
					isPending = true;
			}

			pushInventory.setStatus(isFailed ? StatusType.FAILED : (isPending ? StatusType.PENDING : StatusType.SUCCESS));
			pushInventoryRepository.save(pushInventory);

		} else {
			list.add(MasterDataAppConstants.SOMETHING_WENT_WRONG);
		}
		
		response.setFailedCount(failedCount);
		response.setSuccessCount(successCount);
		response.setMessages(list);
		return response;
	}
	
	@Override
	public OutboundPushRequestRoot createOrUpdateOutboundPushRequest(Outbound outbound, List<OutbCargoInformation> oldCargoList, List<Long> deletedCargosIds, List<Long> deletedBarcodeDetails) {
		//logger.info("ENTRY :: createOrUpdateOutboundPushRequest with request : {}, {}, {}", oldCargoList, deletedCargosIds, deletedBarcodeDetails);
		List<OutbCargoInformation> newCargoList = outbound.getOutbCargosInformation();

		OutboundPushRequestRoot root = new OutboundPushRequestRoot();
		List<OutboundPushRequestItems> items = new ArrayList<OutboundPushRequestItems>();

		if (deletedCargosIds != null && !deletedCargosIds.isEmpty()) {
			List<OutbCargoInformation> deletedCargoList = oldCargoList.stream().filter(p -> deletedCargosIds.contains(p.getId())).collect(Collectors.toList());

			if (deletedCargoList != null && !deletedCargoList.isEmpty()) {

				for (OutbCargoInformation deletedCargo : deletedCargoList) {

					// these cargo is deleted from DB, so delete it from WMS
					if (deletedCargo.getOutbBarcodeDetails() != null && deletedCargo.getOutbBarcodeDetails().isEmpty()) {
						for (OutbBarCodeDetails deletedBarcode : deletedCargo.getOutbBarcodeDetails()) {
							items.add(createOutboundPushRequestItem(outbound, deletedCargo, deletedBarcode, -deletedBarcode.getPiecesQuantity()));
						}
					} else {
						items.add(createOutboundPushRequestItem(outbound, deletedCargo, null, -deletedCargo.getPiecesQuantity()));
					}
				}
			}
		}

		OutbCargoInformation oldCargo = null;
		List<OutbBarCodeDetails> oldBarcodeList = null;
		List<OutbBarCodeDetails> newBarcodeList = null;
		Double difference = 0.0; 

		for (OutbCargoInformation newCargo : newCargoList) {

			if (oldCargoList != null && !oldCargoList.isEmpty())
				oldCargo = oldCargoList.stream().filter(p -> p.getId() == newCargo.getId()).findFirst().orElse(null);

			newBarcodeList = newCargo.getOutbBarcodeDetails();
			if (oldCargo != null) {
				// this cargo is exist in DB and updating its values and barcodes, so update it accordingly in WMS as follow

				oldBarcodeList = oldCargo.getOutbBarcodeDetails();

				if (newBarcodeList != null && !newBarcodeList.isEmpty() && oldBarcodeList != null && !oldBarcodeList.isEmpty()) {

					for (OutbBarCodeDetails newBarcode : newBarcodeList) {
						OutbBarCodeDetails oldBarcode = oldBarcodeList.stream().filter(p -> p.getOutbBarcodeDetails() == newBarcode.getOutbBarcodeDetails()).findFirst().orElse(null);
						if (oldBarcode != null) {

							// this barcode is exist in DB and updating its values, so update its values in WMS
							difference = newBarcode.getPiecesQuantity() - oldBarcode.getPiecesQuantity();
							if (difference != 0.0)
								items.add(createOutboundPushRequestItem(outbound, newBarcode.getOutbCargoInformationId(), newBarcode, newBarcode.getPiecesQuantity() - oldBarcode.getPiecesQuantity()));
						} else {

							// this barcode is new entry in DB, so add new in WMS
							items.add(createOutboundPushRequestItem(outbound, newBarcode.getOutbCargoInformationId(), newBarcode, newBarcode.getPiecesQuantity()));
						}
					}

					for (OutbBarCodeDetails oldBarcode : oldBarcodeList) {
						if (deletedBarcodeDetails.contains(oldBarcode.getOutbBarcodeDetails())) {

							// this barcode is deleted from this cargo information, so remove it from WMS
							items.add(createOutboundPushRequestItem(outbound, oldBarcode.getOutbCargoInformationId(), oldBarcode, -oldBarcode.getPiecesQuantity()));
						}
					}

				} else if ((oldBarcodeList != null && !oldBarcodeList.isEmpty()) && (newBarcodeList == null || newBarcodeList.isEmpty())) {

					// this cargo had barcode details before, but now all barcodes are removed and only cargo it self has quantity
					// so remove all these old barcodes from WMS
					// and add only cargo in WMS

					for (OutbBarCodeDetails oldBarcode : oldBarcodeList) {
						if (deletedBarcodeDetails.contains(oldBarcode.getOutbBarcodeDetails())) {
							// "oldBarcodeList" ids should be available in deletedBarcodeDetails, thats why
							// old object had barcodes and new object dont have barcodes.
							items.add(createOutboundPushRequestItem(outbound, oldBarcode.getOutbCargoInformationId(), oldBarcode, -oldBarcode.getPiecesQuantity()));
						}
					}
					items.add(createOutboundPushRequestItem(outbound, newCargo, null, newCargo.getPiecesQuantity()));

				} else if ((oldBarcodeList == null || oldBarcodeList.isEmpty()) && (newBarcodeList != null && !newBarcodeList.isEmpty())) {

					// this cargo didnt have barcode details before, but now barcode details added to this cargo.
					// so first remove only cargo from WMS which was added earlier and then add new barcodes in WMS

					items.add(createOutboundPushRequestItem(outbound, oldCargo, null, -oldCargo.getPiecesQuantity()));

					for (OutbBarCodeDetails newBarcode : newBarcodeList) {
						items.add(createOutboundPushRequestItem(outbound, newBarcode.getOutbCargoInformationId(), newBarcode, newBarcode.getPiecesQuantity()));
					}

				} else if ((oldBarcodeList == null || oldBarcodeList.isEmpty()) && (newBarcodeList == null || newBarcodeList.isEmpty())) {

					// this cargo didnt have barcode details before and also it dont have barcode details now
					// it is just updating its quantity values that cargo it self has, so update in WMS also
					difference = newCargo.getPiecesQuantity() - oldCargo.getPiecesQuantity();
					if (difference != 0.0)
						items.add(createOutboundPushRequestItem(outbound, newCargo, null, newCargo.getPiecesQuantity() - oldCargo.getPiecesQuantity()));

				}
			} else {
				// this cargo is complete new entry in DB, so add its values in WMS

				if (newBarcodeList != null && !newBarcodeList.isEmpty()) {

					// this is new cargo and it has barcodes, all its values are newly added in DB, so add those barcodes to WMS
					for (OutbBarCodeDetails newBarcode : newBarcodeList) {
						items.add(createOutboundPushRequestItem(outbound, newBarcode.getOutbCargoInformationId(), newBarcode, newBarcode.getPiecesQuantity()));
					}

				} else if (newBarcodeList == null || newBarcodeList.isEmpty()) {
					// this is new cargo but it doesnt have barcodes, only hscode value is added in DB, so add only cargo to WMS
					items.add(createOutboundPushRequestItem(outbound, newCargo, null, newCargo.getPiecesQuantity()));
				}
			}
		}

		root.setItems(items);
		root.setBarcode(outbound.getOutbDeclarationInformation() != null ? outbound.getOutbDeclarationInformation().getBusinessCode() : "");
		root.setFacilityIdCode("");

//		logger.info("INFO :: createOrUpdateOutboundPushRequest with OutboundPushRequestRoot: {}", Utils.objectToXml(root));
		return root;
	}
	
	@Override
	public OutboundPushRequestRoot cancelOutboundPushRequest(Outbound outbound, List<OutbCargoInformation> deletedCargoList) {

		OutboundPushRequestRoot root = new OutboundPushRequestRoot();
		List<OutboundPushRequestItems> items = new ArrayList<OutboundPushRequestItems>();

		if (deletedCargoList != null && !deletedCargoList.isEmpty()) {

			for (OutbCargoInformation deletedCargo : deletedCargoList) {

				// these cargo is deleted from DB, so delete it from WMS
				if (deletedCargo.getOutbBarcodeDetails() != null && !deletedCargo.getOutbBarcodeDetails().isEmpty()) {
					for (OutbBarCodeDetails deletedBarcode : deletedCargo.getOutbBarcodeDetails()) {
						items.add(createOutboundPushRequestItem(outbound, deletedCargo, deletedBarcode, -deletedBarcode.getPiecesQuantity()));
					}
				} else {
					items.add(createOutboundPushRequestItem(outbound, deletedCargo, null, -deletedCargo.getPiecesQuantity()));
				}
			}
		}

		root.setItems(items);
		root.setBarcode(outbound.getOutbDeclarationInformation() != null ? outbound.getOutbDeclarationInformation().getBusinessCode() : "");
		root.setFacilityIdCode("");

//		logger.info("INFO :: cancelOutboundPushRequest with OutboundPushRequestRoot: {}", Utils.objectToXml(root));
		return root;
	}

	@Override
	public PushInventory createOutboundPushInventory(OutboundPushRequestRoot requestRoot, Outbound outbound, OperationType operationType) {
		PushInventory pushInventory = new PushInventory();
		pushInventory.setInventoryType(InventoryType.OUTBOUND);
		pushInventory.setCreatedDate(LocalDateTime.now());
		pushInventory.setLastPushedDate(LocalDateTime.now());
		pushInventory.setCreatedBy(outbound.getCreatedBy());
		pushInventory.setInventoryId(outbound.getOutboundId());
		pushInventory.setBarcode(requestRoot.getBarcode());
		pushInventory.setFacilityIdCode(requestRoot.getFacilityIdCode());
		pushInventory.setOperationType(operationType);
		pushInventory.setInbDeclarationNumber(outbound.getOutbDeclarationInformation().getInbDeclarationNumber());
		pushInventory.setRetryCount(1);

		for (OutboundPushRequestItems item : requestRoot.getItems()) {
			PushInventoryItem pushInventoryItem = new PushInventoryItem();
			pushInventoryItem.setPushInventory(pushInventory);
			pushInventoryItem.setRequestBody(Utils.objectToXml(item));
			pushInventoryItem.setReferenceNumber(item.getOutbReferenceNumber());
			pushInventoryItem.setSkuBarCode(item.getSkuBarcode());
			pushInventoryItem.setLastPushedDate(LocalDateTime.now());
			pushInventoryItem.setStatus(StatusType.PENDING);
			pushInventoryItem.setRetryCount(1);
			pushInventory.getPushInventoryItems().add(pushInventoryItem);
		}
		pushInventory.setStatus(StatusType.PENDING);
		return pushInventoryRepository.save(pushInventory);
	}

	@Override
	public UpdatePushInventoryStatusResponse updateOutboundPushInventoryStatus(PushInventory pushInventory, OutboundPushResponseRoot responseRoot) {
		UpdatePushInventoryStatusResponse response = new UpdatePushInventoryStatusResponse(); 
		int successCount = 0, failedCount = 0;
		List<String> list = new ArrayList<String>();
		if (responseRoot != null) {

			List<OutboundPushResponseItems> responseItems = responseRoot.getItems();
			Boolean isFailed = false, isPending = false;
			for (PushInventoryItem item : pushInventory.getPushInventoryItems()) {

				OutboundPushResponseItems responseItem = responseItems.stream()
						.filter(p -> (p.getBarCode() != null && p.getBarCode().equals(item.getSkuBarCode()) && p.getOutboundReferenceNumber().equals(item.getReferenceNumber()))).findFirst()
						.orElse(null);

				if (responseItem != null) {
					if (responseItem.getStatus() == null || responseItem.getStatus().trim().isEmpty() || responseItem.getStatus().equalsIgnoreCase("ERROR")) {
						list.add(responseItem.getBarCode() + " : " + responseItem.getMessage());
						item.setStatus(StatusType.FAILED);
						isFailed = true;
						failedCount++;
					} else if (responseItem.getStatus() != null && responseItem.getStatus().equalsIgnoreCase("SUCCESS")) {
						item.setStatus(StatusType.SUCCESS);
						list.add(responseItem.getBarCode() + " : Pushed successfully to Oracle");
						successCount++;
					}
					item.setResponseMessage(Utils.objectToXml(responseItem));
				}
				
				if (item.getStatus().equals(StatusType.PENDING))
					isPending = true;
			}

			pushInventory.setStatus(isFailed ? StatusType.FAILED : (isPending ? StatusType.PENDING : StatusType.SUCCESS));
			pushInventoryRepository.save(pushInventory);

		} else {
			list.add(MasterDataAppConstants.SOMETHING_WENT_WRONG);
		}
		response.setFailedCount(failedCount);
		response.setSuccessCount(successCount);
		response.setMessages(list);
		return response;
	}
	
	@Override
	public PushInventory createItemDetailsPushInventory(BarcodePushRequestRoot requestRoot, BarCode barCode, OperationType operationType) {
		PushInventory pushInventory = new PushInventory();
		pushInventory.setInventoryType(InventoryType.BARCODE);
		pushInventory.setCreatedDate(LocalDateTime.now());
		pushInventory.setLastPushedDate(LocalDateTime.now());
		pushInventory.setCreatedBy(barCode.getCreatedBy());
		pushInventory.setInventoryId(barCode.getBarCodeId());
		pushInventory.setBarcode("");
		pushInventory.setFacilityIdCode("");
		pushInventory.setOperationType(operationType);
		pushInventory.setInbDeclarationNumber("");
		pushInventory.setRetryCount(1);

		for (BarcodePushRequestItems item : requestRoot.getItems()) {
			PushInventoryItem pushInventoryItem = new PushInventoryItem();
			pushInventoryItem.setPushInventory(pushInventory);
			pushInventoryItem.setRequestBody(Utils.objectToXml(item));
			pushInventoryItem.setReferenceNumber("");
			if (barCode.getHsCodeId() != null)
				pushInventoryItem.setSkuBarCode(barCode.getHsCodeId().getHsCode() + "/" + barCode.getBarCode());
			else
				pushInventoryItem.setSkuBarCode(barCode.getBarCode());
			pushInventoryItem.setLastPushedDate(LocalDateTime.now());
			pushInventoryItem.setStatus(StatusType.PENDING);
			pushInventoryItem.setRetryCount(1);
			pushInventory.getPushInventoryItems().add(pushInventoryItem);
		}
		pushInventory.setStatus(StatusType.PENDING);
		return pushInventoryRepository.save(pushInventory);
	}
	
	@Override
	public List<String> updateBarCodePushInventoryStatus(PushInventory pushInventory, BarcodePushResponseRoot responseRoot) {
		List<String> list = new ArrayList<String>();
		if (responseRoot != null) {
			PushInventoryItem item = null;
			Boolean isFailed = false, isPending = false;
			if (pushInventory.getPushInventoryItems() != null && !pushInventory.getPushInventoryItems().isEmpty()) {
				item = pushInventory.getPushInventoryItems().get(0);
				if (responseRoot != null) {
					if (responseRoot.getStatus().equalsIgnoreCase("SUCCESS")) {
						item.setStatus(StatusType.SUCCESS);
					} else if (responseRoot.getStatus().equalsIgnoreCase("ERROR")) {
						list.add(responseRoot.getMessage());
						item.setStatus(StatusType.FAILED);
						isFailed = true;
					}
					item.setResponseMessage(Utils.objectToXml(responseRoot));
				}
				if (item.getStatus().equals(StatusType.PENDING))
					isPending = true;
			}

			pushInventory.setStatus(isFailed ? StatusType.FAILED : (isPending ? StatusType.PENDING : StatusType.SUCCESS));
			pushInventoryRepository.save(pushInventory);

		} else {
			list.add(MasterDataAppConstants.SOMETHING_WENT_WRONG);
		}
		return list;
	}

}
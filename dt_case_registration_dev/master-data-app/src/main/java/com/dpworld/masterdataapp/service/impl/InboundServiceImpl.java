package com.dpworld.masterdataapp.service.impl;

import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.INBOUND_CAN_NOT_BE_CANCELLED_AFTER_30DAYS;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.asm.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dpworld.common.constants.DateFormates;
import com.dpworld.common.utils.CustomException;
import com.dpworld.common.utils.DateUtils;
import com.dpworld.masterdataapp.constants.MasterDataAppConstants;
import com.dpworld.masterdataapp.constants.ReferenceNumberGenerationType;
import com.dpworld.masterdataapp.constants.SpecificationOperation;
import com.dpworld.masterdataapp.model.request.InbBarCodeDetailRequest;
import com.dpworld.masterdataapp.model.request.InbCargoInformationRequest;
import com.dpworld.masterdataapp.model.request.InbDeclarationInformationRequest;
import com.dpworld.masterdataapp.model.request.InbInvoiceDetailsRequest;
import com.dpworld.masterdataapp.model.request.InbShipmentArrivalInformationRequest;
import com.dpworld.masterdataapp.model.request.InboundRequest;
import com.dpworld.masterdataapp.model.request.SearchInventoryFilterRequest;
import com.dpworld.masterdataapp.model.response.CountryDetailsResponse;
import com.dpworld.masterdataapp.model.response.CurrencyDetailsResponse;
import com.dpworld.masterdataapp.model.response.DeclarationTypeResponse;
import com.dpworld.masterdataapp.model.response.GoodsConditionResponse;
import com.dpworld.masterdataapp.model.response.InboundOnloadResponse;
import com.dpworld.masterdataapp.model.response.MarketTypeResponse;
import com.dpworld.masterdataapp.pushInbound.InboundPushRequestItems;
import com.dpworld.masterdataapp.pushInbound.InboundPushRequestRoot;
import com.dpworld.masterdataapp.pushInbound.InboundPushResponseRoot;
import com.dpworld.masterdataapp.pushReturnGoods.ReturnGoodsPushRequestItems;
import com.dpworld.masterdataapp.pushReturnGoods.ReturnGoodsPushRequestRoot;
import com.dpworld.masterdataapp.pushReturnGoods.ReturnGoodsPushResponseRoot;
import com.dpworld.masterdataapp.pushReturnLocalGoods.ReturnLocalGoodsPushRequestItems;
import com.dpworld.masterdataapp.pushReturnLocalGoods.ReturnLocalGoodsPushRequestRoot;
import com.dpworld.masterdataapp.pushReturnLocalGoods.ReturnLocalGoodsPushResponseRoot;
import com.dpworld.masterdataapp.service.BarCodeService;
import com.dpworld.masterdataapp.service.CountryDetailsService;
import com.dpworld.masterdataapp.service.CurrencyDetailsService;
import com.dpworld.masterdataapp.service.DeclarationTypeService;
import com.dpworld.masterdataapp.service.GoodsConditionService;
import com.dpworld.masterdataapp.service.HsCodeService;
import com.dpworld.masterdataapp.service.InbCargoInformationService;
import com.dpworld.masterdataapp.service.InbUploadDocumentsService;
import com.dpworld.masterdataapp.service.InboundService;
import com.dpworld.masterdataapp.service.MarketTypeService;
import com.dpworld.masterdataapp.service.PushInventoryService;
import com.dpworld.masterdataapp.service.WmsApiService;
import com.dpworld.masterdataapp.utility.TmWmsUtils;
import com.dpworld.persistence.entity.BarCode;
import com.dpworld.persistence.entity.CountryDetails;
import com.dpworld.persistence.entity.HsCode;
import com.dpworld.persistence.entity.InbBarCodeDetails;
import com.dpworld.persistence.entity.InbCargoInformation;
import com.dpworld.persistence.entity.InbDeclarationInformation;
import com.dpworld.persistence.entity.InbShipmentArrivalInformation;
import com.dpworld.persistence.entity.InbUploadDocuments;
import com.dpworld.persistence.entity.Inbound;
import com.dpworld.persistence.entity.MarketType;
import com.dpworld.persistence.entity.PushInventory;
import com.dpworld.persistence.entity.PushInventoryItem;
import com.dpworld.persistence.entity.ReturnType;
import com.dpworld.persistence.entity.StatusType;
import com.dpworld.persistence.enums.OperationType;
import com.dpworld.persistence.repository.InbBarcodeDetailsRepository;
import com.dpworld.persistence.repository.InbDeclarationInfoRepository;
import com.dpworld.persistence.repository.InbShipmentArrivalInfoRepository;
import com.dpworld.persistence.repository.InboundRepository;
import com.dpworld.persistence.repository.PushInventoryRepository;
import com.dpworld.persistence.repository.UploadDocumentTypeRepository;
import com.dpworld.persistence.vo.PushApiResponse;
import com.dpworld.persistence.vo.SearchInventory;
import com.dpworld.persistence.vo.UpdatePushInventoryStatusResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.models.Tag;

@Service
public class InboundServiceImpl implements InboundService{

	private static final Logger logger = LoggerFactory.getLogger(InboundServiceImpl.class);
	
	@Value("${wms.fileupload.maxsize}")
	private long maxsize;
	
	@Value("${inventory.restrict.update}")
	private Integer inventoryRestrictUpdate;

	@Autowired
	private InboundRepository inboundRepository;

	@Autowired
	private WmsApiService wmsApiService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MarketTypeService marketTypeService;

	@Autowired
	private CountryDetailsService countryDetailsService;

	@Autowired
	private DeclarationTypeService declarationTypeService;

	@Autowired
	private GoodsConditionService goodsConditionService;

	@Autowired
	private HsCodeService hsCodeService;

	@Autowired
	private UploadDocumentTypeRepository uploadDocumentTypeRepository;

	@Autowired
	private InbUploadDocumentsService inbUploadDocumentsService;

	@Autowired
	private CurrencyDetailsService currencyDetailsService;

	@Autowired
	private BarCodeService barCodeService;

	@Autowired
	private DateUtils dateUtils;

	@Autowired
	private InbShipmentArrivalInfoRepository inbShipmentArrivalInfoRepository;

	@Autowired
	private InbDeclarationInfoRepository inbDeclarationInfoRepository;
	
	@Autowired
	private TmWmsUtils tmWmsUtils;
	
	@Autowired
	private InbBarcodeDetailsRepository inbBarcodeDetailsRepository;
	
	@Autowired
	private InbCargoInformationService inbCargoInformationService;
	
	@Autowired
	private PushInventoryService pushInventoryService;
	
	@Autowired
	private PushInventoryRepository pushInventoryRepository;
	
	private Long maxsizeInMb = maxsize / 1000000;

	@Override
	public StringBuilder validateAddInboundRequest(InboundRequest inboundRequest, Boolean isUpdate, List<MultipartFile> customDeclaration, List<MultipartFile> certificateOfOrigin,
			List<MultipartFile> commercialInvoice, List<MultipartFile> packingList) {
		StringBuilder message = new StringBuilder();
		InbShipmentArrivalInformationRequest arrivalInfo = inboundRequest.getInbShipmentArrivalInformation();
		InbDeclarationInformationRequest declarationInfo = inboundRequest.getInbDeclarationInformation();

		if (inboundRequest.getInboundId() == 0) {
			if (arrivalInfo.getId() != 0 && inbShipmentArrivalInfoRepository.findByIdAndWithInbound(arrivalInfo.getId()) != null)
				message.append("Inbound Shipment Arrival Information already linked with another inbound.\n");
			if (declarationInfo.getId() != 0 && inbDeclarationInfoRepository.findByIdAndWithInbound(arrivalInfo.getId()) != null)
				message.append("Inbound Declaration Information already linked with another inbound.\n");
		} else {
			Inbound inbound = getInboundById(inboundRequest.getInboundId());
			if (inbound == null) {
				message.append("Inbound Details not found \n");
			} else {
				if (arrivalInfo.getId() != 0) {
					InbShipmentArrivalInformation arriaval = inbShipmentArrivalInfoRepository.findById(arrivalInfo.getId());
					if (arriaval == null)
						message.append("Inbound Shipment Arrival Information details not found.\n");
					else if (arriaval.getInbound() != null && arriaval.getInbound().getInboundId() != inbound.getInboundId())
						message.append("Inbound Shipment Arrival Information already linked with another inbound.\n");
				}

				if (declarationInfo.getId() != 0) {
					InbDeclarationInformation declaration = inbDeclarationInfoRepository.findById(declarationInfo.getId());
					if (declaration == null)
						message.append("Declaration Information details not found.\n");
					else if (declaration.getInbound() != null && declaration.getInbound().getInboundId() != inbound.getInboundId())
						message.append("Inbound Declaration Information already linked with another inbound.\n");
				}
			}
		}

		if (arrivalInfo.getTransportMode() == null || arrivalInfo.getTransportMode() == 0)
			message.append("Please select transport mode.\n");
		
		if (arrivalInfo.getTransportMode() > 3  || arrivalInfo.getTransportMode() < 1)
			message.append("Please Enter only 1,2 or 3 in transport mode.\n");
		
		if (arrivalInfo.getArrivedFromCountryId() == null || arrivalInfo.getArrivedFromCountryId() == 0)
			message.append("Please select country arrived from.\n");
		else {
			CountryDetails country = countryDetailsService.getByCountryId(arrivalInfo.getArrivedFromCountryId());
			if (country == null)
				message.append("Arrived from country details not found.\n");
			else if (country.getCountryName().equalsIgnoreCase("UNITED ARAB EMIRATES") || country.getCountryName().equalsIgnoreCase("UAE")) {
				
				if (arrivalInfo.getMarketTypeId() == null || arrivalInfo.getMarketTypeId() == 0)
					message.append("Please select market type.\n");
				else {
					MarketType marketType = marketTypeService.getByMarketTypeId(arrivalInfo.getMarketTypeId());
					if (marketType == null)
						message.append("Market type details not found.\n");
					else if (marketType.getMarketType().equalsIgnoreCase("Local Market")) {
						if (declarationInfo.getLocalGoodsPassNumber() == null || declarationInfo.getLocalGoodsPassNumber().trim().isEmpty())
							message.append("Please enter local good pass number.\n");
						else if (declarationInfo.getLocalGoodsPassNumber().length() > 15)
							message.append("Local goods pass number must not be greater than 15 character.\n");
					}
				}
			}
		}
		
		if (arrivalInfo.getArrivalDate() == null)
			message.append("Please select arrival date.\n");
		else if (arrivalInfo.getArrivalDate().isAfter(LocalDate.now()))
			message.append("Arrival date can not be future date.\n");

		if (arrivalInfo.getVoyageNumber() != null && arrivalInfo.getVoyageNumber().length() > 20)
			message.append("Voyage number must not be greater than 20 character.\n");

		if (arrivalInfo.getCarrier() != null && arrivalInfo.getCarrier().length() > 20)
			message.append("Carrier must not be greater than 20 character.\n");

		String returnType = inboundRequest.getReturnType();
		if (returnType.equalsIgnoreCase(ReturnType.NONE.toString())) {
			if (declarationInfo.getDeclarationNumber() == null || declarationInfo.getDeclarationNumber().trim().isEmpty())
				message.append("Please enter declaration number.\n");

			if (declarationInfo.getDeclarationTypeId() == null || declarationInfo.getDeclarationTypeId() == 0)
				message.append("Please select declaration type.\n");
			else if (declarationTypeService.getByDeclarationTypeId(declarationInfo.getDeclarationTypeId()) == null)
				message.append("Declaration type info not found.\n");
		} else {

			if (!(declarationInfo.getDeclarationNumber() == null || declarationInfo.getDeclarationNumber().trim().isEmpty())
					|| !(declarationInfo.getDeclarationTypeId() == null || declarationInfo.getDeclarationTypeId() == 0)) {

				if (declarationInfo.getDeclarationNumber() == null || declarationInfo.getDeclarationNumber().trim().isEmpty())
					message.append("Please enter declaration number.\n");
				else if (declarationInfo.getDeclarationNumber().length() > 13)
					message.append("Declaration number must not be greater than 13 character.\n");

				if (declarationInfo.getDeclarationTypeId() == null || declarationInfo.getDeclarationTypeId() == 0) {
					message.append("Please select declaration type.\n");
				} else if (declarationTypeService.getByDeclarationTypeId(declarationInfo.getDeclarationTypeId()) == null)
					message.append("Declaration type info not found.\n");
			}
		}
		
		if (declarationInfo.getConsigneeName() == null || declarationInfo.getConsigneeName().trim().isEmpty())
			message.append("Please select consignee name.\n");
		
		if (declarationInfo.getBusinessCode() == null || declarationInfo.getBusinessCode().trim().isEmpty())
			message.append("Please select business code.\n");

		List<InbCargoInformationRequest> cargoInfos = inboundRequest.getInbCargosInformation();
		if (cargoInfos == null || cargoInfos.isEmpty()) {
			message.append("Please enter at least one cargo information.\n");
		} else {
			InbCargoInformationRequest cargoInfo;
			InbInvoiceDetailsRequest invoiceInfo;
			InbBarCodeDetailRequest barCodeDetailInfo;
			HsCode hscode = null;
			BarCode barCode = null;
			for (int i = 0; i < cargoInfos.size(); i++) {
				cargoInfo = cargoInfos.get(i);
				if (cargoInfo.getHsCodeId() == null || cargoInfo.getHsCodeId() == 0)
					message.append("Cargo Info : Please select valid HsCode.\n");
				else {
					hscode = hsCodeService.getByHsCodeId(cargoInfo.getHsCodeId());
					if (hscode == null)
						message.append("Cargo Info : HsCode details not found.\n");
					else {
						if (cargoInfo.getPiecesQuantity() == null || cargoInfo.getPiecesQuantity() <= 0)
							message.append("Cargo Info : " + hscode.getHsCode() + " : Please enter pieces quantity.\n");

						if (cargoInfo.getPiecesNetWeight() == null || cargoInfo.getPiecesNetWeight() <= 0)
							message.append("Cargo Info : " + hscode.getHsCode() + " : Please enter pieces total weight.\n");

						if (cargoInfo.getManufacturerCountryId() == null || cargoInfo.getManufacturerCountryId() == 0)
							message.append("Cargo Info : " + hscode.getHsCode() + " : Please select country of manufacture.\n");
						else if (countryDetailsService.getByCountryId(cargoInfo.getManufacturerCountryId()) == null)
							message.append("Cargo Info : " + hscode.getHsCode() + " : Country of manufacture details not found.\n");

						if (cargoInfo.getPackageDescription() != null && cargoInfo.getPackageDescription().length() > 30)
							message.append("Cargo Info : " + hscode.getHsCode() + " : Package description must not be greater than 30 character.\n");

						if (cargoInfo.getGoodsConditionId() == null || cargoInfo.getGoodsConditionId() == 0)
							message.append("Cargo Info : " + hscode.getHsCode() + " : Please select goods condition.\n");
						else if (goodsConditionService.getByGoodsConditionId(cargoInfo.getGoodsConditionId()) == null)
							message.append("Cargo Info : " + hscode.getHsCode() + " : Goods condition details not found.\n");

						invoiceInfo = cargoInfo.getInbInvoiceDetail();
						if (invoiceInfo == null) {
							message.append("Cargo Info : " + hscode.getHsCode() + " : Invoice Information not found.\n");
						} else {

							if (invoiceInfo.getInvoiceNumber() == null || invoiceInfo.getInvoiceNumber().trim().isEmpty())
								message.append("Cargo Info : " + hscode.getHsCode() + " : Please enter invoice number.\n");
							else if (invoiceInfo.getInvoiceNumber().length() > 10)
								message.append("Invoice number must not be greater than 10 character.\n");

							if (invoiceInfo.getInvoiceDate() == null)
								message.append("Cargo Info : " + hscode.getHsCode() + " : Please select invoice date.\n");
							else if (invoiceInfo.getInvoiceDate().isAfter(LocalDate.now()))
								message.append("Cargo Info : " + hscode.getHsCode() + " : Invoice date can not be future date.\n");

							if (invoiceInfo.getCurrencyId() == null || invoiceInfo.getCurrencyId() == 0)
								message.append("Cargo Info : " + hscode.getHsCode() + " : Please select invoice currency.\n");
							else if (currencyDetailsService.getByCurrencyId(invoiceInfo.getCurrencyId()) == null)
								message.append("Cargo Info : " + hscode.getHsCode() + " : Invoice currency details not found.\n");

							if (invoiceInfo.getCifValue() == null || invoiceInfo.getCifValue() == 0)
								message.append("Cargo Info : " + hscode.getHsCode() + " : Please select invoice currency.\n");
						}

						List<InbBarCodeDetailRequest> barCodeDetailInfos = cargoInfo.getInbBarcodeDetails();
						if (barCodeDetailInfos != null && !barCodeDetailInfos.isEmpty()) {
							for (int j = 0; j < barCodeDetailInfos.size(); j++) {
								barCodeDetailInfo = barCodeDetailInfos.get(j);
								if (barCodeDetailInfo.getBarCodeId() == 0) {
									message.append("Cargo Info : " + hscode.getHsCode() + " : Please select barcode.\n");
								} else {
									barCode = barCodeService.getByBarCodeId(barCodeDetailInfo.getBarCodeId());
									if (barCode == null)
										message.append("Cargo Info : " + hscode.getHsCode() + " : BarCode : Barcode details not found.\n");
									else if (barCode.getHsCodeId() == null)
										message.append("Cargo Info : " + hscode.getHsCode() + " : BarCode : " + barCode.getBarCode() + " : HsCode not found in barcode.\n");
									else if (!barCode.getHsCodeId().getHsCodeId().equals(cargoInfo.getHsCodeId()))
										message.append("Cargo Info : " + hscode.getHsCode() + " : BarCode : " + barCode.getBarCode() + " : Barcode not matched with HsCode.\n");
									if (barCodeDetailInfo.getPiecesQuantity() == null || barCodeDetailInfo.getPiecesQuantity() <= 0)
										message.append("Cargo Info : " + hscode.getHsCode() + " : BarCode : " + barCode.getBarCode() + " : Please enter pieces quantity.\n");
									if (barCodeDetailInfo.getPiecesNetWeight() == null || barCodeDetailInfo.getPiecesNetWeight() <= 0)
										message.append("Cargo Info : " + hscode.getHsCode() + " : BarCode : " + barCode.getBarCode() + " : Please enter pieces total weight.\n");
								}
							}
						}
					}
				}
			}
		}
		
		// Return type validation
		String searchReq = inboundRequest.getReturnType();
		if (searchReq == null || searchReq.isEmpty()) {
			message.append("Please enter at least one inventory type (NONE/RETURNGOODS/RETURNLOCALGOODS).\n");
		} else if ((!searchReq.equalsIgnoreCase(ReturnType.NONE.toString())) && (!searchReq.equalsIgnoreCase(ReturnType.RETURNGOODS.toString()))
				&& (!searchReq.equalsIgnoreCase(ReturnType.RETURNLOCALGOODS.toString()))) {
			message.append("Please select inventory type (NONE/RETURNGOODS/RETURNLOCALGOODS).\n");
		}
		
		if (isUpdate) {
			int count = 0;
//			if (inboundRequest.getDeletedCustomDeclarationDocuments() != null && !inboundRequest.getDeletedCustomDeclarationDocuments().isEmpty()) {
//				if (customDeclaration == null || customDeclaration.isEmpty()) {
//					count = outbUploadDocumentsService.countByOutboundIdUploadDocumentTypeIsCancelAndIdNotIn(inboundRequest.getInboundId(), MasterDataAppConstants.CUSTOM_DECLARATION,
//							inboundRequest.getDeletedCustomDeclarationDocuments());
//					if (count == 0)
//						message.append("Please upload custom declaration.\n");
//				}
//			}
			if (inboundRequest.getDeletedPackingListDocuments() != null && !inboundRequest.getDeletedPackingListDocuments().isEmpty()) {
				if (packingList == null || packingList.isEmpty()) {
					count = inbUploadDocumentsService.countByInboundIdUploadDocumentTypeIsCancelAndIdNotIn(inboundRequest.getInboundId(), MasterDataAppConstants.PACKING_LIST,
							inboundRequest.getDeletedPackingListDocuments());
					if (count == 0)
						message.append("Please upload paking list.\n");
				}
			}
			if (inboundRequest.getDeletedCertificateOfOriginDocuments() != null && !inboundRequest.getDeletedCertificateOfOriginDocuments().isEmpty()) {
				if (certificateOfOrigin == null || certificateOfOrigin.isEmpty()) {
					count = inbUploadDocumentsService.countByInboundIdUploadDocumentTypeIsCancelAndIdNotIn(inboundRequest.getInboundId(), MasterDataAppConstants.CERTIFICATE_OF_ORIGIN,
							inboundRequest.getDeletedCertificateOfOriginDocuments());
					if (count == 0)
						message.append("Please upload certificate of origin.\n");
				}
			}
			if (inboundRequest.getDeletedCommercialInvoiceDocuments() != null && !inboundRequest.getDeletedCommercialInvoiceDocuments().isEmpty()) {
				if (commercialInvoice == null || commercialInvoice.isEmpty()) {
					count = inbUploadDocumentsService.countByInboundIdUploadDocumentTypeIsCancelAndIdNotIn(inboundRequest.getInboundId(), MasterDataAppConstants.COMMERCIAL_INVOICE,
							inboundRequest.getDeletedCommercialInvoiceDocuments());
					if (count == 0)
						message.append("Please upload commercial invoice.\n");
				}
			}
		} else {
//			if (customDeclaration == null || customDeclaration.isEmpty())
//				message.append("Please upload custom declaration.\n");
			if (packingList == null || packingList.isEmpty())
				message.append("Please upload packing list.\n");
			if (commercialInvoice == null || commercialInvoice.isEmpty())
				message.append("Please upload commercial invoice.\n");
			if (certificateOfOrigin == null || certificateOfOrigin.isEmpty())
				message.append("Please upload sales invoice.\n");
		}
		
		if (customDeclaration != null && !customDeclaration.isEmpty()) {
			for (MultipartFile file : customDeclaration)
				validateFileTypeAndSize(file, "Custom Declaration", message);
		}

		if (packingList != null && !packingList.isEmpty()) {
			for (MultipartFile file : packingList)
				validateFileTypeAndSize(file, "Packing List", message);
		}

		if (certificateOfOrigin != null && !certificateOfOrigin.isEmpty()) {
			for (MultipartFile file : certificateOfOrigin)
				validateFileTypeAndSize(file, "Certificate Of Origin", message);
		}
		
		if (commercialInvoice != null && !commercialInvoice.isEmpty()) {
			for (MultipartFile file : commercialInvoice)
				validateFileTypeAndSize(file, "Commercial Invoice", message);
		}

		return message;
	}
	
	private void validateFileTypeAndSize(MultipartFile file, String type, StringBuilder message) {
		if (!file.isEmpty()) {
			if (!tmWmsUtils.validateFileType(file))
				message.append(type + " invalid type of file.\n");
			else if (file.getSize() > maxsize)
				message.append(type + " file size should be less than or equal to " + maxsizeInMb + "MB : " + file.getOriginalFilename() + "\n");
		}
	}

	@Override
	@Transactional
	public Inbound addOrUpdateInbound(InboundRequest inboundRequest, boolean isUpdate, List<MultipartFile> customDeclaration, List<MultipartFile> certificateOfOrigin,
			List<MultipartFile> commercialInvoice, List<MultipartFile> packingList) throws DataIntegrityViolationException, CustomException, Exception {
		//logger.info("ENTRY:: addOrUpdateInbound :: Add or Update inbound details.");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Inbound inbound = new Inbound();
		Inbound inboundUpdate = null;
		List<InbCargoInformation> oldCargoInformation = null;
		List<Long> deletedCargos = inboundRequest.getDeletedInbCargoInformations();
		List<Long> deletedBarcodeDetails = inboundRequest.getDeletedInbBarCodeDetails();
		if (!isUpdate) {
			inbound = objectMapper.convertValue(inboundRequest, Inbound.class);

			inbound.setCreatedOn(new Date());
			inbound.setCreatedBy(auth.getName());
			inbound.setIsCancel(false);
			
			if (inboundRequest.getReturnType().equalsIgnoreCase(ReturnType.NONE.toString()))
				inbound.setReferenceNumber(wmsApiService.getReferenceCode(ReferenceNumberGenerationType.INBOUND));
			else if (inboundRequest.getReturnType().equalsIgnoreCase(ReturnType.RETURNGOODS.toString()))
				inbound.setReferenceNumber(wmsApiService.getReferenceCode(ReferenceNumberGenerationType.RETURNGOODS));
			else if (inboundRequest.getReturnType().equalsIgnoreCase(ReturnType.RETURNLOCALGOODS.toString()))
				inbound.setReferenceNumber(wmsApiService.getReferenceCode(ReferenceNumberGenerationType.RETURNLOCALGOODS));
			
			inbound.setReturnInboundRefId(0);
			inbound.setCompanyCode("Company Code"); // Pending
		} else {
			inboundUpdate = getInboundById(inboundRequest.getInboundId());
			oldCargoInformation = objectMapper.convertValue(inboundUpdate.getInbCargosInformation(), new TypeReference<List<InbCargoInformation>>() {
			});
			if (dateUtils.numberOfDaysBetween(inboundUpdate.getCreatedOn(), new Date()) > inventoryRestrictUpdate)
				throw new CustomException(MasterDataAppConstants.INBOUND_CAN_NOT_BE_UPDATED_AFTER_30DAYS);
			inbound = objectMapper.convertValue(inboundRequest, Inbound.class);
			inbound.setCreatedOn(inboundUpdate.getCreatedOn());
			inbound.setCreatedBy(inboundUpdate.getCreatedBy());
			inbound.setIsCancel(inboundUpdate.getIsCancel());
			inbound.setReferenceNumber(inboundUpdate.getReferenceNumber());
			inbound.setCompanyCode(inboundUpdate.getCompanyCode());
			inbound.setReturnInboundRefId(inboundUpdate.getReturnInboundRefId());
			inbound.setUpdatedBy(auth.getName());
			inbound.setUpdatedOn(new Date());
		}

		// Convert object
		if (inboundRequest.getInbShipmentArrivalInformation() != null) {
			if (inboundRequest.getInbShipmentArrivalInformation().getMarketTypeId() != null)
				inbound.getInbShipmentArrivalInformation().setMarketTypeId(marketTypeService.getByMarketTypeId(inboundRequest.getInbShipmentArrivalInformation().getMarketTypeId()));
			if (inboundRequest.getInbShipmentArrivalInformation().getArrivedFromCountryId() != null)
				inbound.getInbShipmentArrivalInformation().setArrivedFromCountryId(countryDetailsService.getByCountryId(inboundRequest.getInbShipmentArrivalInformation().getArrivedFromCountryId()));
		}

		if (inboundRequest.getInbDeclarationInformation() != null) {
			if (inboundRequest.getInbDeclarationInformation().getDeclarationTypeId() != null)
				inbound.getInbDeclarationInformation().setDeclarationTypeId(declarationTypeService.getByDeclarationTypeId(inboundRequest.getInbDeclarationInformation().getDeclarationTypeId()));
		}
		
		if (isUpdate && inboundUpdate != null) {

			if(deletedCargos != null && !deletedCargos.isEmpty())
				inbCargoInformationService.deleteAllByIds(deletedCargos);

			if(deletedBarcodeDetails != null && !deletedBarcodeDetails.isEmpty())
				inbBarcodeDetailsRepository.deleteByInbBarcodeDetailsIn(deletedBarcodeDetails);
			
		}
		
		InbCargoInformation cargoInformation = null;
		InbCargoInformationRequest cargoInformationRequest = null;
		if (inboundRequest.getInbCargosInformation() != null) {
			for (int i = 0; i < inbound.getInbCargosInformation().size(); i++) {
				cargoInformation = inbound.getInbCargosInformation().get(i);
				cargoInformation.setInbound(inbound);
				cargoInformationRequest = inboundRequest.getInbCargosInformation().get(i);
				if (cargoInformationRequest.getGoodsConditionId() != null)
					cargoInformation.setGoodsConditionId(goodsConditionService.getByGoodsConditionId(cargoInformationRequest.getGoodsConditionId()));
				
				if (cargoInformationRequest.getHsCodeId() != 0) {
					HsCode hscode = hsCodeService.getByHsCodeId(cargoInformationRequest.getHsCodeId());
					cargoInformation.setHsCodeId(hscode);
					cargoInformation.setDutyValue(hsCodeService.getDutyValue(hscode));
				}
				
				if (cargoInformationRequest.getManufacturerCountryId() != null)
					cargoInformation.setManufacturerCountryId(countryDetailsService.getByCountryId(cargoInformationRequest.getManufacturerCountryId()));
				
				if (cargoInformationRequest.getInbInvoiceDetail() != null && cargoInformationRequest.getInbInvoiceDetail().getCurrencyId() != null)
					cargoInformation.getInbInvoiceDetail().setCurrencyId(currencyDetailsService.getByCurrencyId(cargoInformationRequest.getInbInvoiceDetail().getCurrencyId()));
				cargoInformation.getInbInvoiceDetail().setInbCargoInformationId(cargoInformation);

				InbBarCodeDetails barcodeDetails = null;
				InbBarCodeDetailRequest barCodeDetailRequest = null;

				if (cargoInformationRequest.getInbBarcodeDetails() != null) {
					for (int j = 0; j < cargoInformation.getInbBarcodeDetails().size(); j++) {
						barcodeDetails = cargoInformation.getInbBarcodeDetails().get(j);
						barcodeDetails.setInbCargoInformationId(cargoInformation);
						barCodeDetailRequest = cargoInformationRequest.getInbBarcodeDetails().get(j);
						if (barCodeDetailRequest.getBarCodeId() != 0)
							barcodeDetails.setBarCodeId(barCodeService.getByBarCodeId(barCodeDetailRequest.getBarCodeId()));
					}
				}
			}
		}

		inbound.getInbDeclarationInformation().setInbound(inbound);
		inbound.getInbShipmentArrivalInformation().setInbound(inbound);
		try {
			//logger.info("EXIT:: addOrUpdateInbound :: Add or Update inbound details.");
			Inbound save = inboundRepository.save(inbound);
			List<InbUploadDocuments> documents = new ArrayList<InbUploadDocuments>();
			documents.addAll(inBoundUploadDocuments(customDeclaration, save, MasterDataAppConstants.CUSTOM_DECLARATION));
			documents.addAll(inBoundUploadDocuments(certificateOfOrigin, save, MasterDataAppConstants.CERTIFICATE_OF_ORIGIN));
			documents.addAll(inBoundUploadDocuments(commercialInvoice, save, MasterDataAppConstants.COMMERCIAL_INVOICE));
			documents.addAll(inBoundUploadDocuments(packingList, save, MasterDataAppConstants.PACKING_LIST));
			save.setInbUploadDocuments(documents);
			
			if(inboundRequest.getDeletedCustomDeclarationDocuments() != null && !inboundRequest.getDeletedCustomDeclarationDocuments().isEmpty())
				inbUploadDocumentsService.cancelByIdIn(inboundRequest.getDeletedCustomDeclarationDocuments());
			if(inboundRequest.getDeletedPackingListDocuments() != null && !inboundRequest.getDeletedPackingListDocuments().isEmpty())
				inbUploadDocumentsService.cancelByIdIn(inboundRequest.getDeletedPackingListDocuments());
			if(inboundRequest.getDeletedCertificateOfOriginDocuments() != null && !inboundRequest.getDeletedCertificateOfOriginDocuments().isEmpty())
				inbUploadDocumentsService.cancelByIdIn(inboundRequest.getDeletedCertificateOfOriginDocuments());
			if(inboundRequest.getDeletedCommercialInvoiceDocuments() != null && !inboundRequest.getDeletedCommercialInvoiceDocuments().isEmpty())
				inbUploadDocumentsService.cancelByIdIn(inboundRequest.getDeletedCommercialInvoiceDocuments());
			
			PushApiResponse pushApiResponse = null;
			if (save.getReturnType().equals(ReturnType.NONE)) {
				pushApiResponse = inboundPushData(save, oldCargoInformation, deletedCargos, deletedBarcodeDetails, isUpdate ? OperationType.UPDATE : OperationType.CREATE);
			} else if (save.getReturnType().equals(ReturnType.RETURNGOODS)) {
				pushApiResponse = returnGoodsPushData(save, oldCargoInformation, deletedCargos, deletedBarcodeDetails, isUpdate ? OperationType.UPDATE : OperationType.CREATE);
			} else if (save.getReturnType().equals(ReturnType.RETURNLOCALGOODS)) {
				pushApiResponse = returnLocalGoodsPushData(save, oldCargoInformation, deletedCargos, deletedBarcodeDetails, isUpdate ? OperationType.UPDATE : OperationType.CREATE);
			}
			save.setPushApiResponse(pushApiResponse);
			return save;
		} catch (DataIntegrityViolationException e) {
			logger.error("ERROR:: addOrUpdateInbound :: Data integrity exception occured while saving Inboud.");
			throw new DataIntegrityViolationException("Error: already exist:--" + e.getMessage());
		}
	}

	@Override
	public PushApiResponse inboundPushData(Inbound inbound, List<InbCargoInformation> oldCargoInformation, List<Long> deletedCargos, List<Long> deleteBarcodeDetails, OperationType operationType) {
		PushApiResponse pushApiResponse = new PushApiResponse();
		InboundPushRequestRoot requestRoot = pushInventoryService.createOrUpdateInboundPushRequest(inbound, oldCargoInformation, deletedCargos, deleteBarcodeDetails);
		List<String> messages = null;
		int itemsCount = 0, removedItemsCount = 0, successCount = 0, failedCount = 0;
		if (requestRoot != null && requestRoot.getItems() != null && !requestRoot.getItems().isEmpty()) {
			itemsCount = requestRoot.getItems().size();
			messages = new ArrayList<>();
			PushInventory pushInventory = pushInventoryService.createInboundPushInventory(requestRoot, inbound, operationType);
			try {
				
				// removing InboundPushRequestItems from being pushed if depending parent item is FAILED or PENDING
				if (!operationType.equals(OperationType.CREATE)) {
					ListIterator<InboundPushRequestItems> iter = requestRoot.getItems().listIterator();
					InboundPushRequestItems item = null;
					List<PushInventory> inventories = null;
					while (iter.hasNext()) {
						item = iter.next();
						inventories = pushInventoryRepository.findByInbDeclarationAndSkuBarcodeAndStatus(item.getDeclarationNumber(), item.getSkuBarcode(), StatusType.FAILED);
						if (inventories != null && !inventories.isEmpty()) {
							messages.add(item.getSkuBarcode() + " : not pushed to Oracle");
							iter.remove();
							removedItemsCount++;
						}
					}
				}

				// if there is no InboundPushRequestItems exist in InboundPushRequestRoot then do not call PUSH API
				if (!requestRoot.getItems().isEmpty()) {
					InboundPushResponseRoot responseRoot = wmsApiService.inboundPushData(requestRoot);
					UpdatePushInventoryStatusResponse sResponse = pushInventoryService.updateInboundPushInventoryStatus(pushInventory, responseRoot);
					successCount = sResponse.getSuccessCount();
					failedCount = sResponse.getFailedCount();
					messages.addAll(sResponse.getMessages());
				}
			} catch (CustomException e) {
				logger.error("ERROR : Error while inbound push data : {}", e.getMessage());
				pushInventory.setStatus(StatusType.FAILED);
				for (PushInventoryItem item : pushInventory.getPushInventoryItems()) {
					if (!item.getStatus().equals(StatusType.SUCCESS)) {
						item.setStatus(StatusType.FAILED);
						messages.add(item.getSkuBarCode() + " : not pushed to Oracle");
						failedCount++;
					}
				}
				pushInventoryService.save(pushInventory);
				pushApiResponse.setMessages(Arrays.asList(MasterDataAppConstants.SOMETHING_WENT_WRONG));
			}
		}
		pushApiResponse.setMessages(messages);
		pushApiResponse.setCode(tmWmsUtils.getPushApiResponseCode(itemsCount, removedItemsCount, successCount, failedCount));
		return pushApiResponse;
	}
	
	@Override
	public PushApiResponse returnGoodsPushData(Inbound inbound, List<InbCargoInformation> oldCargoInformation, List<Long> deletedCargos, List<Long> deletedBarcodeDetails, OperationType operationType) {
		PushApiResponse pushApiResponse = new PushApiResponse();
		ReturnGoodsPushRequestRoot requestRoot = pushInventoryService.createOrUpdateReturnGoodsPushRequest(inbound, oldCargoInformation, deletedCargos, deletedBarcodeDetails);
		List<String> messages = null;
		int itemsCount = 0, removedItemsCount = 0, successCount = 0, failedCount = 0;
		if (requestRoot != null && requestRoot.getItems() != null && !requestRoot.getItems().isEmpty()) {
			PushInventory pushInventory = pushInventoryService.createReturnGoodsPushInventory(requestRoot, inbound, operationType);
			itemsCount = requestRoot.getItems().size();
			messages = new ArrayList<>();
			try {
				
				// removing ReturnGoodsPushRequestItems from being pushed if depending parent item is FAILED or PENDING
				ListIterator<ReturnGoodsPushRequestItems> iter = requestRoot.getItems().listIterator();
				ReturnGoodsPushRequestItems item = null;
				List<PushInventory> inventories = null;
				while (iter.hasNext()) {
					item = iter.next();
					inventories = pushInventoryRepository.findByInbDeclarationAndSkuBarcodeAndStatus(item.getInbDeclarationNumber(), item.getSkuBarcode(), StatusType.FAILED);
					if (inventories != null && !inventories.isEmpty()) {
						messages.add(item.getSkuBarcode() + " : not pushed to Oracle");
						iter.remove();
						removedItemsCount++;
					}
				}

				// if there is no ReturnGoodsPushRequestItems exist in ReturnGoodsPushRequestRoot then do not call PUSH API
				if (!requestRoot.getItems().isEmpty()) {
					ReturnGoodsPushResponseRoot responseRoot = wmsApiService.returnGoodsPushData(requestRoot);
					UpdatePushInventoryStatusResponse sResponse = pushInventoryService.updateReturnGoodsPushInventoryStatus(pushInventory, responseRoot);
					successCount = sResponse.getSuccessCount();
					failedCount = sResponse.getFailedCount();
					messages.addAll(sResponse.getMessages());
				}
				
			} catch (CustomException e) {
				logger.error("ERROR : Error while inbound push data : {}", e.getMessage());
				pushInventory.setStatus(StatusType.FAILED);
				messages.add(MasterDataAppConstants.SOMETHING_WENT_WRONG);
				for (PushInventoryItem item : pushInventory.getPushInventoryItems()) {
					if (!item.getStatus().equals(StatusType.SUCCESS)) {
						item.setStatus(StatusType.FAILED);
						messages.add(item.getSkuBarCode() + " : not pushed to Oracle");
						failedCount++;
					}
				}
				pushInventoryService.save(pushInventory);
			}
		}
		pushApiResponse.setMessages(messages);
		pushApiResponse.setCode(tmWmsUtils.getPushApiResponseCode(itemsCount, removedItemsCount, successCount, failedCount));
		return pushApiResponse;
	}

	@Override
	public PushApiResponse returnLocalGoodsPushData(Inbound inbound, List<InbCargoInformation> oldCargoInformation, List<Long> deletedCargos, List<Long> deleteBarcodeDetails, OperationType operationType) {
		PushApiResponse pushApiResponse = new PushApiResponse();
		ReturnLocalGoodsPushRequestRoot requestRoot = pushInventoryService.createOrUpdateReturnLocalGoodsPushRequest(inbound, oldCargoInformation, deletedCargos, deleteBarcodeDetails);
		List<String> messages = null;
		int itemsCount = 0, removedItemsCount = 0, successCount = 0, failedCount = 0;
		if (requestRoot != null && requestRoot.getItems() != null && !requestRoot.getItems().isEmpty()) {
			itemsCount = requestRoot.getItems().size();
			messages = new ArrayList<>();
			PushInventory pushInventory = pushInventoryService.createReturnLocalGoodsPushInventory(requestRoot, inbound, operationType);
			try {
				
				// removing ReturnLocalGoodsPushRequestItems from being pushed if depending parent item is FAILED or PENDING
				ListIterator<ReturnLocalGoodsPushRequestItems> iter = requestRoot.getItems().listIterator();
				ReturnLocalGoodsPushRequestItems item = null;
				List<PushInventory> inventories = null;
				while (iter.hasNext()) {
					item = iter.next();
					inventories = pushInventoryRepository.findByInbDeclarationAndSkuBarcodeAndStatus(item.getInbDeclarationNumber(), item.getSkuBarcode(), StatusType.FAILED);
					if (inventories != null && !inventories.isEmpty()) {
						messages.add(item.getSkuBarcode() + " : not pushed to Oracle");
						iter.remove();
						removedItemsCount++;
					}
				}

				// if there is no ReturnLocalGoodsPushRequestItems exist in ReturnLocalGoodsPushRequestRoot then do not call PUSH API
				if (!requestRoot.getItems().isEmpty()) {
					ReturnLocalGoodsPushResponseRoot responseRoot = wmsApiService.returnLocalGoodsPushData(requestRoot);
					UpdatePushInventoryStatusResponse sResponse = pushInventoryService.updateReturnLocalGoodsPushInventoryStatus(pushInventory, responseRoot);
					successCount = sResponse.getSuccessCount();
					failedCount = sResponse.getFailedCount();
					messages.addAll(sResponse.getMessages());
				}
				
			} catch (CustomException e) {
				logger.error("ERROR : Error while inbound push data : {}", e.getMessage());
				pushInventory.setStatus(StatusType.FAILED);
				messages.add(MasterDataAppConstants.SOMETHING_WENT_WRONG);
				for (PushInventoryItem item : pushInventory.getPushInventoryItems()) {
					if (!item.getStatus().equals(StatusType.SUCCESS)) {
						item.setStatus(StatusType.FAILED);
						messages.add(item.getSkuBarCode() + " : not pushed to Oracle");
						failedCount++;
					}
				}
				pushInventoryService.save(pushInventory);
			}
		}
		pushApiResponse.setMessages(messages);
		pushApiResponse.setCode(tmWmsUtils.getPushApiResponseCode(itemsCount, removedItemsCount, successCount, failedCount));
		return pushApiResponse;
	}
	
	@Override
	public Inbound getInboundById(long inboundId) {
		return inboundRepository.findByInboundId(inboundId);
	}

	@Override
	public List<Inbound> getInboundByIds(List<Long> inboundIds) {
		return inboundRepository.findByInboundIdIn(inboundIds);
	}

	@Override
	public InboundOnloadResponse getOnLoadData(String serviceId) throws CustomException, Exception {

		InboundOnloadResponse inboundOnloadResponse = new InboundOnloadResponse();

		List<MarketTypeResponse> marketTypeResponse = objectMapper.convertValue(marketTypeService.getAll(), new TypeReference<List<MarketTypeResponse>>() {
		});
		inboundOnloadResponse.setMarketType(marketTypeResponse);

		List<CountryDetailsResponse> countryDetailResponse = objectMapper.convertValue(countryDetailsService.getAll(), new TypeReference<List<CountryDetailsResponse>>() {
		});
		inboundOnloadResponse.setCountryDetails(countryDetailResponse);

		List<GoodsConditionResponse> goodsConditionResponse = objectMapper.convertValue(goodsConditionService.getAll(), new TypeReference<List<GoodsConditionResponse>>() {
		});
		inboundOnloadResponse.setGoodCondition(goodsConditionResponse);

		List<CurrencyDetailsResponse> currencyDetailsResponse = objectMapper.convertValue(currencyDetailsService.getAll(), new TypeReference<List<CurrencyDetailsResponse>>() {
		});
		inboundOnloadResponse.setCurrencyDetails(currencyDetailsResponse);

		List<DeclarationTypeResponse> declarationTypeResponse = objectMapper.convertValue(declarationTypeService.getAll(), new TypeReference<List<DeclarationTypeResponse>>() {
		});
		inboundOnloadResponse.setDeclarationType(declarationTypeResponse);

		inboundOnloadResponse.setAgentDetailsList(tmWmsUtils.getImporterAgentsByLoggedInUser(serviceId));

		return inboundOnloadResponse;
	}

	@Override
	public List<SearchInventory> filterSearchInventory(SearchInventoryFilterRequest vo, List<String> agentCodes, Boolean isCancel) {
		List<Inbound> data = null;
		if ((vo.getDeclarationNumber() == null || vo.getDeclarationNumber().trim().isEmpty()) && (vo.getFromDate() == null || vo.getFromDate().trim().isEmpty())
				&& (vo.getToDate() == null || vo.getToDate().trim().isEmpty()) && (vo.getHsCode() == null || vo.getHsCode().trim().isEmpty())
				&& (vo.getTypeOfInventory() == null || vo.getTypeOfInventory().trim().isEmpty())) {
			data = inboundRepository.findAllByBusinessCodeAndIsCancel(agentCodes, isCancel);
		} else {
			Date fromDate = (vo.getFromDate() != null && !vo.getFromDate().trim().isEmpty()) ? dateUtils.stringToDate(vo.getFromDate() + " 00:00:00", DateFormates.DDMMYYYYHHMMSS_HYPHEN) : null;
			Date toDate = (vo.getToDate() != null && !vo.getToDate().trim().isEmpty()) ? dateUtils.stringToDate(vo.getToDate() + " 23:59:59", DateFormates.DDMMYYYYHHMMSS_HYPHEN) : null;
			InboundSpecification specDeclarationNumber = new InboundSpecification("declarationNumber", SpecificationOperation.EQUAL, vo.getDeclarationNumber());
			InboundSpecification specFromDate = new InboundSpecification("fromDate", SpecificationOperation.GREATER_THAN_OR_EQUAL, fromDate);
			InboundSpecification specToDate = new InboundSpecification("toDate", SpecificationOperation.LESS_THAN_OR_EQUAL, toDate);
			InboundSpecification specHsCode = new InboundSpecification("hsCode", SpecificationOperation.EQUAL, vo.getHsCode());
			InboundSpecification specBusinessCode = new InboundSpecification("businessCode", SpecificationOperation.IN, agentCodes);
			InboundSpecification specIsCancel = new InboundSpecification("isCancel", SpecificationOperation.EQUAL, isCancel);
			
			InboundSpecification specReturnType = null;
			if (vo.getTypeOfInventory() != null && !vo.getTypeOfInventory().trim().isEmpty()) {
				if (vo.getTypeOfInventory().equalsIgnoreCase("inbound"))
					specReturnType = new InboundSpecification("returnType", SpecificationOperation.EQUAL, ReturnType.NONE);
				else if (vo.getTypeOfInventory().equalsIgnoreCase("returnGoods"))
					specReturnType = new InboundSpecification("returnType", SpecificationOperation.EQUAL, ReturnType.RETURNGOODS);
				else if (vo.getTypeOfInventory().equalsIgnoreCase("returnLocalGoods"))
					specReturnType = new InboundSpecification("returnType", SpecificationOperation.EQUAL, ReturnType.RETURNLOCALGOODS);
			}
			Specification<Inbound> spec = Specification.where(specDeclarationNumber).and(specFromDate).and(specToDate).and(specHsCode).and(specBusinessCode).and(specIsCancel).and(specReturnType);
			data = inboundRepository.findAll(spec);
		}
		List<SearchInventory> list = new ArrayList<SearchInventory>();
		if (data != null && !data.isEmpty()) {
			list.addAll(data.stream().map(s -> new SearchInventory(s.getInboundId(), s.getReferenceNumber(), s.getCreatedOn(), "Inbound", s.getReturnType())).collect(Collectors.toList()));
		}
		return list;
	}

	private class InboundSpecification implements Specification<Inbound> {
		private static final long serialVersionUID = 1L;
		private String key;
		private String operation;
		private Object value;

		InboundSpecification(String key, String operation, Object value) {
			this.key = key;
			this.operation = operation;
			this.value = value;
		}

		@Override
		public Predicate toPredicate(Root<Inbound> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
//			System.out.println(key + " - " + operation + " - " + value);
			if (value != null && !value.toString().trim().isEmpty() && !value.toString().equalsIgnoreCase("null")) {
				if (key.equalsIgnoreCase("declarationNumber") && operation.equalsIgnoreCase(SpecificationOperation.EQUAL)) {
					return builder.equal(root.<InbDeclarationInformation>get("inbDeclarationInformation").<String>get("declarationNumber"), value.toString());
				} else if (key.equalsIgnoreCase("fromDate") && operation.equalsIgnoreCase(SpecificationOperation.GREATER_THAN_OR_EQUAL)) {
					return builder.greaterThanOrEqualTo(root.<Date>get("createdOn"), (Date) value);
				} else if (key.equalsIgnoreCase("toDate") && operation.equalsIgnoreCase(SpecificationOperation.LESS_THAN_OR_EQUAL)) {
					return builder.lessThanOrEqualTo(root.<Date>get("createdOn"), (Date) value);
				} else if (key.equalsIgnoreCase("hsCode") && operation.equalsIgnoreCase(SpecificationOperation.EQUAL)) {
					return builder.equal(root.join("inbCargosInformation").join("hsCodeId").<String>get("hsCode"), value.toString());
				} else if (key.equalsIgnoreCase("returnType") && operation.equalsIgnoreCase(SpecificationOperation.EQUAL)) {
					return builder.equal(root.<ReturnType>get("returnType"), (ReturnType) value);
				} else if (key.equalsIgnoreCase("isCancel") && operation.equalsIgnoreCase(SpecificationOperation.EQUAL)) {
					return builder.equal(root.<Boolean>get("isCancel"), (Boolean) value);
				} else if (key.equalsIgnoreCase("businessCode") && operation.equalsIgnoreCase(SpecificationOperation.IN)) {
					final Join<Tag, Label> join = root.join("inbDeclarationInformation");
					return join.<String>get("businessCode").in((List<String>) value);
				}
			}
			return null;
		}

	}
	
	@Override
	@Transactional
	public List<Inbound> cancelInbound(List<Inbound> inboundList) throws CustomException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<Inbound> responseList = new ArrayList<Inbound>();
		for (Inbound inbound : inboundList) {
			if (dateUtils.numberOfDaysBetween(inbound.getCreatedOn(), new Date()) > inventoryRestrictUpdate)
				throw new CustomException(INBOUND_CAN_NOT_BE_CANCELLED_AFTER_30DAYS);
			inbound.setUpdatedOn(new Date());
			inbound.setUpdatedBy(auth.getName());
			inbound.setIsCancel(true);
			responseList.add(inbound);
		}
		inboundRepository.saveAll(responseList);

		for (Inbound inbound : responseList) {
			if (inbound.getReturnType().equals(ReturnType.NONE)) {
				InboundPushRequestRoot requestRoot = pushInventoryService.cancelInboundPushRequest(inbound, inbound.getInbCargosInformation());
				if (requestRoot.getItems() != null && !requestRoot.getItems().isEmpty()) {
					PushInventory pushInventory = pushInventoryService.createInboundPushInventory(requestRoot, inbound, OperationType.CANCEL);
					try {

						// removing InboundPushRequestItems from being pushed if depending parent item is FAILED or PENDING
						ListIterator<InboundPushRequestItems> iter = requestRoot.getItems().listIterator();
						InboundPushRequestItems item = null;
						List<PushInventory> inventories = null;
						while (iter.hasNext()) {
							item = iter.next();
							inventories = pushInventoryRepository.findByInbDeclarationAndSkuBarcodeAndStatus(item.getDeclarationNumber(), item.getSkuBarcode(), StatusType.FAILED);
							if (inventories != null && !inventories.isEmpty())
								iter.remove();
						}

						// if there is no InboundPushRequestItems exist in InboundPushRequestRoot then do not call PUSH API
						if (!requestRoot.getItems().isEmpty()) {
							InboundPushResponseRoot responseRoot = wmsApiService.inboundPushData(requestRoot);
							pushInventoryService.updateInboundPushInventoryStatus(pushInventory, responseRoot);
						}

					} catch (CustomException e) {
						logger.error("ERROR :: error occurred while cancelling INBOUND : {}", e.getMessage());
						pushInventory.setStatus(StatusType.FAILED);
						for (PushInventoryItem item : pushInventory.getPushInventoryItems())
							item.setStatus(StatusType.FAILED);
						pushInventoryService.save(pushInventory);
					}
				}
			} else if (inbound.getReturnType().equals(ReturnType.RETURNGOODS)) {
				ReturnGoodsPushRequestRoot requestRoot = pushInventoryService.cancelReturnGoodsPushRequest(inbound, inbound.getInbCargosInformation());
				if (requestRoot.getItems() != null && !requestRoot.getItems().isEmpty()) {
					PushInventory pushInventory = pushInventoryService.createReturnGoodsPushInventory(requestRoot, inbound, OperationType.CANCEL);
					try {
						
						// removing existing FAILED ReturnGoodsPushRequestItems from ReturnGoodsPushRequestRoot
						ListIterator<ReturnGoodsPushRequestItems> iter = requestRoot.getItems().listIterator();
						ReturnGoodsPushRequestItems item = null;
						List<PushInventory> inventories = null;
						while (iter.hasNext()) {
							item = iter.next();
							inventories = pushInventoryRepository.findByInbDeclarationAndSkuBarcodeAndStatus(item.getInbDeclarationNumber(), item.getSkuBarcode(), StatusType.FAILED);
							if (inventories != null && !inventories.isEmpty())
								iter.remove();
						}

						// if there is no ReturnGoodsPushRequestItems exist in ReturnGoodsPushRequestRoot then do not call PUSH API
						if (!requestRoot.getItems().isEmpty()) {
							ReturnGoodsPushResponseRoot responseRoot = wmsApiService.returnGoodsPushData(requestRoot);
							pushInventoryService.updateReturnGoodsPushInventoryStatus(pushInventory, responseRoot);
						}
						
					} catch (CustomException e) {
						logger.error("ERROR :: error occurred while cancelling RETURNGOODS : {}", e.getMessage());
						pushInventory.setStatus(StatusType.FAILED);
						for (PushInventoryItem item : pushInventory.getPushInventoryItems())
							item.setStatus(StatusType.FAILED);
						pushInventoryService.save(pushInventory);
					}
				}
			} else if (inbound.getReturnType().equals(ReturnType.RETURNLOCALGOODS)) {
				ReturnLocalGoodsPushRequestRoot requestRoot = pushInventoryService.cancelReturnLocalGoodsPushRequest(inbound, inbound.getInbCargosInformation());
				if (requestRoot.getItems() != null && !requestRoot.getItems().isEmpty()) {
					PushInventory pushInventory = pushInventoryService.createReturnLocalGoodsPushInventory(requestRoot, inbound, OperationType.CANCEL);
					try {
						
						// removing existing FAILED ReturnLocalGoodsPushRequestItems from ReturnLocalGoodsPushRequestRoot
						ListIterator<ReturnLocalGoodsPushRequestItems> iter = requestRoot.getItems().listIterator();
						ReturnLocalGoodsPushRequestItems item = null;
						List<PushInventory> inventories = null;
						while (iter.hasNext()) {
							item = iter.next();
							inventories = pushInventoryRepository.findByInbDeclarationAndSkuBarcodeAndStatus(item.getInbDeclarationNumber(), item.getSkuBarcode(), StatusType.FAILED);
							if (inventories != null && !inventories.isEmpty())
								iter.remove();
						}

						// if there is no ReturnLocalGoodsPushRequestItems exist in ReturnLocalGoodsPushRequestRoot then do not call PUSH API
						if (!requestRoot.getItems().isEmpty()) {
							ReturnLocalGoodsPushResponseRoot responseRoot = wmsApiService.returnLocalGoodsPushData(requestRoot);
							pushInventoryService.updateReturnLocalGoodsPushInventoryStatus(pushInventory, responseRoot);
						}
						
					} catch (CustomException e) {
						logger.error("ERROR :: error occurred while cancelling RETURNLOCALGOODS : {}", e.getMessage());
						pushInventory.setStatus(StatusType.FAILED);
						for (PushInventoryItem item : pushInventory.getPushInventoryItems())
							item.setStatus(StatusType.FAILED);
						pushInventoryService.save(pushInventory);
					}
				}
			}
		}
		return responseList;
	}

	@Transactional(propagation = Propagation.MANDATORY)
	private List<InbUploadDocuments> inBoundUploadDocuments(List<MultipartFile> files, Inbound inbound, String type) {
		List<InbUploadDocuments> documents = new ArrayList<InbUploadDocuments>();
		if (files != null && !files.isEmpty()) {
			InbUploadDocuments document = null;
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					document = new InbUploadDocuments();
					document.setInbound(inbound);
					document.setUploadDocumentTypeId(uploadDocumentTypeRepository.findByName(type));
					document.setIsCancel(false);
					inbUploadDocumentsService.save(document);
					inbUploadDocumentsService.save(document, file);
					documents.add(document);
				}
			}
		}
		return documents;
	}

	@Override
	public List<Inbound> findAllByBusinessCodeAndDateRange(String businessCode, Date fromDate, Date toDate) {
		return inboundRepository.findAllByBusinessCodeAndDateRange(businessCode, fromDate, toDate);
	}

	@Override
	public void save(Inbound inbound) {
		inboundRepository.save(inbound);		
	}
	
	@Override
	public Inbound updateDateByReferenceNumber(Inbound inbound, String createdOn) throws ParseException {

		Date newDate = dateUtils.stringToDate(createdOn, DateFormates.DDMMYYYY_HYPHEN);
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(newDate);

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(inbound.getCreatedOn());
		calendar2.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DATE));

		inbound.setCreatedOn(calendar2.getTime());
		return inboundRepository.save(inbound);
	}

	@Override
	public Inbound findByReferenceNumber(String referenceNumber) {
		return inboundRepository.findByReferenceNumber(referenceNumber);
	}
}

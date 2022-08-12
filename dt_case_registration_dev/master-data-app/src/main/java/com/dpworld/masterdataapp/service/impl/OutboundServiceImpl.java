package com.dpworld.masterdataapp.service.impl;

import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.OUTBOUND_CAN_NOT_BE_CANCELLED_AFTER_30DAYS;

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
import com.dpworld.masterdataapp.model.request.OutbBarCodeDetailsRequest;
import com.dpworld.masterdataapp.model.request.OutbCargoInformationRequest;
import com.dpworld.masterdataapp.model.request.OutbDeclarationInformationRequest;
import com.dpworld.masterdataapp.model.request.OutbInvoiceDetailsRequest;
import com.dpworld.masterdataapp.model.request.OutbShipmentDepartureInformationRequest;
import com.dpworld.masterdataapp.model.request.OutboundRequest;
import com.dpworld.masterdataapp.model.request.SearchInventoryFilterRequest;
import com.dpworld.masterdataapp.model.response.CountryDetailsResponse;
import com.dpworld.masterdataapp.model.response.CurrencyDetailsResponse;
import com.dpworld.masterdataapp.model.response.DeclarationTypeResponse;
import com.dpworld.masterdataapp.model.response.GoodsConditionResponse;
import com.dpworld.masterdataapp.model.response.MarketTypeResponse;
import com.dpworld.masterdataapp.model.response.OutboundOnloadResponse;
import com.dpworld.masterdataapp.model.response.ShipmentSoldTypeResponse;
import com.dpworld.masterdataapp.pushOutbound.OutboundPushRequestItems;
import com.dpworld.masterdataapp.pushOutbound.OutboundPushRequestRoot;
import com.dpworld.masterdataapp.pushOutbound.OutboundPushResponseRoot;
import com.dpworld.masterdataapp.service.BarCodeService;
import com.dpworld.masterdataapp.service.CountryDetailsService;
import com.dpworld.masterdataapp.service.CurrencyDetailsService;
import com.dpworld.masterdataapp.service.DeclarationTypeService;
import com.dpworld.masterdataapp.service.GoodsConditionService;
import com.dpworld.masterdataapp.service.HsCodeService;
import com.dpworld.masterdataapp.service.MarketTypeService;
import com.dpworld.masterdataapp.service.OutbCargoInformationService;
import com.dpworld.masterdataapp.service.OutbUploadDocumentsService;
import com.dpworld.masterdataapp.service.OutboundService;
import com.dpworld.masterdataapp.service.PushInventoryService;
import com.dpworld.masterdataapp.service.ShipmentSoldTypeService;
import com.dpworld.masterdataapp.service.WmsApiService;
import com.dpworld.masterdataapp.utility.TmWmsUtils;
import com.dpworld.persistence.entity.BarCode;
import com.dpworld.persistence.entity.HsCode;
import com.dpworld.persistence.entity.InvoiceDetail;
import com.dpworld.persistence.entity.OutbBarCodeDetails;
import com.dpworld.persistence.entity.OutbCargoInformation;
import com.dpworld.persistence.entity.OutbDeclarationInformation;
import com.dpworld.persistence.entity.OutbShipmentDepartureInformation;
import com.dpworld.persistence.entity.OutbUploadDocuments;
import com.dpworld.persistence.entity.Outbound;
import com.dpworld.persistence.entity.PushInventory;
import com.dpworld.persistence.entity.PushInventoryItem;
import com.dpworld.persistence.entity.ShipmentSoldType;
import com.dpworld.persistence.entity.StatusType;
import com.dpworld.persistence.enums.OperationType;
import com.dpworld.persistence.repository.OutbBarcodeDetailsRepository;
import com.dpworld.persistence.repository.OutbDeclarationInfoRepository;
import com.dpworld.persistence.repository.OutbInvoiceDetailRepository;
import com.dpworld.persistence.repository.OutbShipmentDepartureInfoRepository;
import com.dpworld.persistence.repository.OutboundRepository;
import com.dpworld.persistence.repository.PushInventoryRepository;
import com.dpworld.persistence.repository.UploadDocumentTypeRepository;
import com.dpworld.persistence.vo.PushApiResponse;
import com.dpworld.persistence.vo.SearchInventory;
import com.dpworld.persistence.vo.UpdatePushInventoryStatusResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.models.Tag;

@Service
public class OutboundServiceImpl implements OutboundService {

	private static final Logger logger = LoggerFactory.getLogger(OutboundServiceImpl.class);

	@Value("${wms.fileupload.maxsize}")
	private long maxsize;

	@Value("${inventory.restrict.update}")
	private Integer inventoryRestrictUpdate;
	
	@Value("${wms.defaults.currency}")
	private String defaultCurrency;

	@Autowired
	private OutboundRepository outboundRepository;

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
	private CurrencyDetailsService currencyDetailsService;

	@Autowired
	private UploadDocumentTypeRepository uploadDocumentTypeRepository;

	@Autowired
	private OutbUploadDocumentsService outbUploadDocumentsService;

	@Autowired
	private ShipmentSoldTypeService shipmentSoldTypeService;

	@Autowired
	private WmsApiService wmsApiService;

	@Autowired
	private BarCodeService barCodeService;

	@Autowired
	private DateUtils dateUtils;

	@Autowired
	private OutbShipmentDepartureInfoRepository outbShipmentDepartureInfoRepository;

	@Autowired
	private OutbDeclarationInfoRepository outbDeclarationInfoRepository;
	
	@Autowired
	private OutbCargoInformationService outbCargoInformationService;
	
	@Autowired
	private OutbBarcodeDetailsRepository outbBarcodeDetailsRepository;

	@Autowired
	private TmWmsUtils tmWmsUtils;
	
	@Autowired
	private PushInventoryService pushInventoryService;
	
	@Autowired
	private PushInventoryRepository pushInventoryRepository;
	
	@Autowired
	private OutbInvoiceDetailRepository outbInvoiceDetailRepository;

	private Long maxsizeInMb = maxsize / 1000000;

	@Override
	public StringBuilder validateAddOutboundRequest(OutboundRequest outboudRequest, boolean isUpdate, List<MultipartFile> customDeclaration, List<MultipartFile> packingList,
			List<MultipartFile> salesInvoice) {
		StringBuilder message = new StringBuilder();

		if (isUpdate && getOutboundById(outboudRequest.getOutboundId()) == null) {
			message.append("Outbound Details not found \n");
		}

		OutbShipmentDepartureInformationRequest departureInfo = outboudRequest.getOutbShipmentDepartureInformation();
		OutbDeclarationInformationRequest declarationInfo = outboudRequest.getOutbDeclarationInformation();

		if (outboudRequest.getOutboundId() == 0) {
			if (departureInfo.getId() != 0 && outbShipmentDepartureInfoRepository.findByIdAndWithOutbound(departureInfo.getId()) != null)
				message.append("Outbound Departure Information already linked with another outbound.\n");
			if (declarationInfo.getId() != 0 && outbDeclarationInfoRepository.findByIdAndWithOutbound(departureInfo.getId()) != null)
				message.append("Outbound Declaration Information already linked with another outbound.\n");
		} else {
			Outbound outbound = getOutboundById(outboudRequest.getOutboundId());
			if (outbound == null) {
				message.append("Inbound Details not found \n");
			} else {
				if (departureInfo.getId() != 0) {
					OutbShipmentDepartureInformation departure = outbShipmentDepartureInfoRepository.findById(departureInfo.getId());
					if (departure == null)
						message.append("Outbound Departure Information details not found.\n");
					else if (departure.getOutbound() != null && departure.getOutbound().getOutboundId() != outbound.getOutboundId())
						message.append("Outbound Departure Information already linked with another outbound.\n");
				}

				if (declarationInfo.getId() != 0) {
					OutbDeclarationInformation declaration = outbDeclarationInfoRepository.findById(declarationInfo.getId());
					if (declaration == null)
						message.append("Declaration Information details not found.\n");
					else if (declaration.getOutbound() != null && declaration.getOutbound().getOutboundId() != outbound.getOutboundId())
						message.append("Outbound Declaration Information already linked with another outbound.\n");
				}
			}
		}

		if (departureInfo.getShipmentDate() == null)
			message.append("Please select ship date \n");
		else if (departureInfo.getShipmentDate().isAfter(LocalDate.now()))
			message.append("Shipment date can not be future date.\n");

		if (departureInfo.getShipmentSoldTypeId() == null || departureInfo.getShipmentSoldTypeId() == 0)
			message.append("Please select shipment sold type.\n");

		ShipmentSoldType soldtype = shipmentSoldTypeService.getByShipmentSoldTypeId(departureInfo.getShipmentSoldTypeId());
		if (soldtype == null)
			message.append("Shipment sold type not found.\n");
		else if (soldtype.getShipmentSoldType().equalsIgnoreCase(MasterDataAppConstants.WHOLESALE) || soldtype.getShipmentSoldType().equalsIgnoreCase(MasterDataAppConstants.EXPORT)) {
			if (departureInfo.getTransportMode() == null || departureInfo.getTransportMode() == 0)
				message.append("Please select transport mode.\n");
			
			if (departureInfo.getTransportMode() > 3 || departureInfo.getTransportMode() < 1)
				message.append("Please select valid transport mode.\n");
			

			if (departureInfo.getCountryDetail() == 0 || departureInfo.getCountryDetail() == null)
				message.append("Please select Arrived from country.\n");
			else if (countryDetailsService.getByCountryId(departureInfo.getCountryDetail()) == null)
				message.append("Arrived from country details not found.\n");

			if (departureInfo.getVoyageNumber() != null && departureInfo.getVoyageNumber().length() > 20)
				message.append("Voyage number must not be greater than 20 character.\n");

			if (departureInfo.getCarrier() != null && departureInfo.getCarrier().length() > 20)
				message.append("Carrier must not be greater than 20 character.\n");
		}

		if (declarationInfo.getInbDeclarationNumber() == null || declarationInfo.getInbDeclarationNumber().trim().isEmpty())
			message.append("Please enter inbound declaration number \n");
		else if (declarationInfo.getInbDeclarationNumber().length() > 13)
			message.append("Inbound declaration number must not be greater than 13 character.\n");

		if (declarationInfo.getInbDeclarationTypeId() == null || declarationInfo.getInbDeclarationTypeId() == 0)
			message.append("Please select declaration type.\n");

		if (!(declarationInfo.getOutbDeclarationNumber() == null || declarationInfo.getOutbDeclarationNumber().trim().isEmpty())
				|| !(declarationInfo.getOutbDeclarationTypeId() == null || declarationInfo.getOutbDeclarationTypeId() == 0)) {

			if (declarationInfo.getOutbDeclarationNumber() == null || declarationInfo.getOutbDeclarationNumber().trim().isEmpty())
				message.append("Please enter outbound declaration number.\n");
			else if (declarationInfo.getOutbDeclarationNumber().length() > 13)
				message.append("Outbound declaration number must not be greater than 13 character.\n");

			if (declarationInfo.getOutbDeclarationTypeId() == null || declarationInfo.getOutbDeclarationTypeId() == 0) {
				message.append("Please select outbound declaration type.\n");
			} else if (declarationTypeService.getByDeclarationTypeId(declarationInfo.getOutbDeclarationTypeId()) == null)
				message.append("Outbound declaration type info not found.\n");
		}

		if (declarationInfo.getConsigneeName() != null && declarationInfo.getConsigneeName().length() > 30)
			message.append("Buyer name must not be greater than 30 character.\n");

		List<OutbCargoInformationRequest> cargoInfos = outboudRequest.getOutbCargosInformation();
		if (cargoInfos == null || cargoInfos.isEmpty()) {
			message.append("Please enter at least one cargo information.\n");
		} else {
			OutbCargoInformationRequest cargoInfo;
			OutbInvoiceDetailsRequest invoiceInfo;
			OutbBarCodeDetailsRequest barCodeDetailInfo;
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

						invoiceInfo = cargoInfo.getOutbInvoiceDetail();
						if (soldtype != null && !soldtype.getShipmentSoldType().equalsIgnoreCase(MasterDataAppConstants.RETAIL)) {
							if (invoiceInfo == null) {
								message.append("Cargo Info : " + hscode.getHsCode() + " : Invoice Information not found.\n");
							} else {
								if (invoiceInfo.getInvoiceNumber() == null || invoiceInfo.getInvoiceNumber().trim().isEmpty())
									message.append("Cargo Info : " + hscode.getHsCode() + " : Please enter invoice number.\n");
								else if (invoiceInfo.getInvoiceNumber().length() > 10)
									message.append("Cargo Info : " + hscode.getHsCode() + " : Invoice number must not be greater than 10 character.\n");

								if (invoiceInfo.getInvoiceDate() == null)
									message.append("Cargo Info : " + hscode.getHsCode() + " : Please select invoice date.\n");
								else if (invoiceInfo.getInvoiceDate().isAfter(LocalDate.now()))
									message.append("Cargo Info : " + hscode.getHsCode() + " : Invoice date can not be future date.\n");

								if (invoiceInfo.getCurrencyId() == null || invoiceInfo.getCurrencyId() == 0)
									message.append("Cargo Info : " + hscode.getHsCode() + " : Please select invoice currency.\n");
								else if (currencyDetailsService.getByCurrencyId(invoiceInfo.getCurrencyId()) == null)
									message.append("Cargo Info : " + hscode.getHsCode() + " : Invoice currency details not found.\n");

								if (invoiceInfo.getCifValue() == null || invoiceInfo.getCifValue() == 0)
									message.append("Cargo Info : " + hscode.getHsCode() + " : Please enter invoice cif.\n");

								if (invoiceInfo.getDiscountPercent() != null && invoiceInfo.getDiscountPercent() > 100)
									message.append("Cargo Info : " + hscode.getHsCode() + " : Discount percentage must be less than 100%.\n");
							}
						} else if (soldtype != null && soldtype.getShipmentSoldType().equalsIgnoreCase(MasterDataAppConstants.RETAIL) && invoiceInfo != null) {

							if (!(invoiceInfo.getInvoiceNumber() == null || invoiceInfo.getInvoiceNumber().trim().isEmpty()) 
									|| !(invoiceInfo.getCurrencyId() == null || invoiceInfo.getCurrencyId() == 0)
									|| !(invoiceInfo.getCifValue() == null || invoiceInfo.getCifValue() == 0) 
									|| !(invoiceInfo.getDiscountPercent() == null || invoiceInfo.getDiscountPercent() == 0)
									|| invoiceInfo.getInvoiceDate() != null) {

								if (invoiceInfo.getInvoiceNumber() == null || invoiceInfo.getInvoiceNumber().trim().isEmpty())
									message.append("Cargo Info : " + hscode.getHsCode() + " : Please enter invoice number.\n");
								else if (invoiceInfo.getInvoiceNumber().length() > 10)
									message.append("Cargo Info : " + hscode.getHsCode() + " : Invoice number must not be greater than 10 character.\n");

								if (invoiceInfo.getInvoiceDate() == null)
									message.append("Cargo Info : " + hscode.getHsCode() + " : Please select invoice date.\n");
								else if (invoiceInfo.getInvoiceDate().isAfter(LocalDate.now()))
									message.append("Cargo Info : " + hscode.getHsCode() + " : Invoice date can not be future date.\n");

								if (invoiceInfo.getCurrencyId() == null || invoiceInfo.getCurrencyId() == 0)
									message.append("Cargo Info : " + hscode.getHsCode() + " : Please select currency.\n");
								else if (currencyDetailsService.getByCurrencyId(invoiceInfo.getCurrencyId()) == null)
									message.append("Cargo Info : " + hscode.getHsCode() + " : Invoice currency details not found.\n");

								if (invoiceInfo.getCifValue() == null || invoiceInfo.getCifValue() == 0)
									message.append("Cargo Info : " + hscode.getHsCode() + " : Please enter CIF value.\n");

								if (invoiceInfo.getDiscountPercent() != null && invoiceInfo.getDiscountPercent() != 0 && invoiceInfo.getDiscountPercent() > 100)
									message.append("Cargo Info : " + hscode.getHsCode() + " : Discount percentage must be less than 100%.\n");
							}

						}

						List<OutbBarCodeDetailsRequest> barCodeDetailInfos = cargoInfo.getOutbBarcodeDetails();
						if (barCodeDetailInfos != null && !barCodeDetailInfos.isEmpty()) {
							for (int j = 0; j < barCodeDetailInfos.size(); j++) {
								barCodeDetailInfo = barCodeDetailInfos.get(j);
								if (barCodeDetailInfo.getBarCodeId() == 0)
									message.append("Cargo Info : " + hscode.getHsCode() + " : BarCode : Please select bar code.\n");
								else {
									barCode = barCodeService.getByBarCodeId(barCodeDetailInfo.getBarCodeId());
									if (barCode == null)
										message.append("Cargo Info : " + hscode.getHsCode() + " : BarCode : Barcode details not found.\n");
									else if (barCode.getHsCodeId() == null)
										message.append("Cargo Info : " + hscode.getHsCode() + " : BarCode : " + barCode.getBarCode() + " : HsCode not found in bar code.\n");
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
//		System.out.println(packingList.isEmpty());
		if (isUpdate) {
			int count = 0;
			if (outboudRequest.getDeletedCustomDeclarationDocuments() != null && !outboudRequest.getDeletedCustomDeclarationDocuments().isEmpty()) {
				if (customDeclaration == null || customDeclaration.isEmpty()) {
					count = outbUploadDocumentsService.countByOutboundIdUploadDocumentTypeIsCancelAndIdNotIn(outboudRequest.getOutboundId(), MasterDataAppConstants.CUSTOM_DECLARATION,
							outboudRequest.getDeletedCustomDeclarationDocuments());
					if (count == 0)
						message.append("Please upload custom declaration.\n");
				}
			}
			if (outboudRequest.getDeletedPackingListDocuments() != null && !outboudRequest.getDeletedPackingListDocuments().isEmpty()) {
				if (packingList == null || packingList.isEmpty()) {
					count = outbUploadDocumentsService.countByOutboundIdUploadDocumentTypeIsCancelAndIdNotIn(outboudRequest.getOutboundId(), MasterDataAppConstants.PACKING_LIST,
							outboudRequest.getDeletedPackingListDocuments());
					if (count == 0)
						message.append("Please upload paking list.\n");
				}
			}
			if (outboudRequest.getDeletedSalesInvoiceDocuments() != null && !outboudRequest.getDeletedSalesInvoiceDocuments().isEmpty()) {
				if ((salesInvoice == null || salesInvoice.isEmpty()) && soldtype != null && !soldtype.getShipmentSoldType().equalsIgnoreCase(MasterDataAppConstants.RETAIL)) {
					count = outbUploadDocumentsService.countByOutboundIdUploadDocumentTypeIsCancelAndIdNotIn(outboudRequest.getOutboundId(), MasterDataAppConstants.SALES_INVOICE,
							outboudRequest.getDeletedSalesInvoiceDocuments());
					if (count == 0)
						message.append("Please upload sales invoice.\n");
				}
			}
		} else {
			if (customDeclaration == null || customDeclaration.isEmpty())
				message.append("Please upload custom declaration.\n");
			if (packingList == null || packingList.isEmpty())
				message.append("Please upload packing list.\n");
			if ((salesInvoice == null || salesInvoice.isEmpty()) && soldtype != null && !soldtype.getShipmentSoldType().equalsIgnoreCase(MasterDataAppConstants.RETAIL))
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

		if (salesInvoice != null && !salesInvoice.isEmpty()) {
			for (MultipartFile file : salesInvoice)
				validateFileTypeAndSize(file, "Sales Invoice", message);
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
	public Outbound addOrUpdateOutbound(OutboundRequest outboundRequest, boolean isUpdate, List<MultipartFile> customDeclaration, List<MultipartFile> packingList, List<MultipartFile> salesInvoice)
			throws DataIntegrityViolationException, CustomException, Exception {

		//logger.info("ENTRY:: addOrUpdateOutbound :: Add or Update outbound details.");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Outbound outbound = new Outbound();
		Outbound outboundUpdate = null;
		List<OutbCargoInformation> oldCargoInformation = null;
		List<Long> deletedCargos = outboundRequest.getDeletedOutbCargoInformations();
		List<Long> deletedBarcodeDetails = outboundRequest.getDeletedOutbBarCodeDetails();
		if (!isUpdate) {

			try {
				outbound = objectMapper.convertValue(outboundRequest, Outbound.class);
			} catch (Exception e) {
				e.getStackTrace();
			}

			outbound.setCreatedOn(new Date());
			outbound.setCreatedBy(auth.getName());
			outbound.setIsCancel(false);
			outbound.setReferenceNumber(wmsApiService.getReferenceCode(ReferenceNumberGenerationType.OUTBOUND));
			outbound.setCompanyCode("Company Code"); // Pending
		} else {
			outboundUpdate = getOutboundById(outboundRequest.getOutboundId());
			if (dateUtils.numberOfDaysBetween(outboundUpdate.getCreatedOn(), new Date()) > inventoryRestrictUpdate) {
				throw new CustomException(MasterDataAppConstants.OUTBOUND_CAN_NOT_BE_UPDATED_AFTER_30DAYS);
			} else if (outboundUpdate.getInvoiceDetail() != null) {
				throw new CustomException(MasterDataAppConstants.OUTBOUND_CAN_NOT_BE_UPDATED_AFTER_INVOICE_GENERATED);
			}
			
			oldCargoInformation = objectMapper.convertValue(outboundUpdate.getOutbCargosInformation(), new TypeReference<List<OutbCargoInformation>>() {
			});
			
			outbound = objectMapper.convertValue(outboundRequest, Outbound.class);
			outbound.setCreatedOn(outboundUpdate.getCreatedOn());
			outbound.setCreatedBy(outboundUpdate.getCreatedBy());
			outbound.setIsCancel(outboundUpdate.getIsCancel());
			outbound.setReferenceNumber(outboundUpdate.getReferenceNumber());
			outbound.setCompanyCode(outboundUpdate.getCompanyCode());
			outbound.setUpdatedBy(auth.getName());
			outbound.setUpdatedOn(new Date());
		}
		ShipmentSoldType shipmentSoldType = shipmentSoldTypeService.getByShipmentSoldTypeId(outboundRequest.getOutbShipmentDepartureInformation().getShipmentSoldTypeId());
		Boolean isRetail = shipmentSoldType.getShipmentSoldType().equalsIgnoreCase(MasterDataAppConstants.RETAIL);
		
		// Convert object
		if (outboundRequest.getOutbShipmentDepartureInformation() != null) {

			if (outboundRequest.getOutbShipmentDepartureInformation().getCountryDetail() != null)
				outbound.getOutbShipmentDepartureInformation().setCountryDetail(countryDetailsService.getByCountryId(outboundRequest.getOutbShipmentDepartureInformation().getCountryDetail()));

			if (outboundRequest.getOutbShipmentDepartureInformation().getShipmentSoldTypeId() != null)
				outbound.getOutbShipmentDepartureInformation().setShipmentSoldTypeId(shipmentSoldType);
		}

		if (outboundRequest.getOutbDeclarationInformation() != null) {
			if (outboundRequest.getOutbDeclarationInformation().getOutbDeclarationTypeId() != null)
				outbound.getOutbDeclarationInformation()
						.setOutbDeclarationTypeId(declarationTypeService.getByDeclarationTypeId(outboundRequest.getOutbDeclarationInformation().getOutbDeclarationTypeId()));

			if (outboundRequest.getOutbDeclarationInformation().getInbDeclarationTypeId() != null)
				outbound.getOutbDeclarationInformation()
						.setInbDeclarationTypeId(declarationTypeService.getByDeclarationTypeId(outboundRequest.getOutbDeclarationInformation().getInbDeclarationTypeId()));

		}
		
		if (isUpdate && outboundUpdate != null) {

			if (deletedCargos != null && !deletedCargos.isEmpty())
				outbCargoInformationService.deleteAllByIds(deletedCargos);

			if (deletedBarcodeDetails != null && !deletedBarcodeDetails.isEmpty())
				outbBarcodeDetailsRepository.deleteByOutbBarcodeDetailsIn(deletedBarcodeDetails);

		}

		OutbCargoInformation cargoInformation = null;
		OutbCargoInformationRequest cargoInformationRequest = null;
		OutbInvoiceDetailsRequest outbInvoiceDetailsRequest = null;
		if (outboundRequest.getOutbCargosInformation() != null) {
			for (int i = 0; i < outbound.getOutbCargosInformation().size(); i++) {
				cargoInformation = outbound.getOutbCargosInformation().get(i);
				cargoInformation.setOutbound(outbound);
				cargoInformationRequest = outboundRequest.getOutbCargosInformation().get(i);
				outbInvoiceDetailsRequest = cargoInformationRequest.getOutbInvoiceDetail();
				if (cargoInformationRequest.getGoodsConditionId() != null)
					cargoInformation.setGoodsConditionId(goodsConditionService.getByGoodsConditionId(cargoInformationRequest.getGoodsConditionId()));

				if (cargoInformationRequest.getHsCodeId() != 0) {
					HsCode hscode = hsCodeService.getByHsCodeId(cargoInformationRequest.getHsCodeId());
					cargoInformation.setHsCodeId(hscode);
					cargoInformation.setDutyValue(hsCodeService.getDutyValue(hscode));
				}
				
				if (cargoInformationRequest.getManufacturerCountryId() != null)
					cargoInformation.setManufacturerCountryId(countryDetailsService.getByCountryId(cargoInformationRequest.getManufacturerCountryId()));

				if (isRetail && outbInvoiceDetailsRequest == null) {
					if(cargoInformation.getId() != 0) {
						outbInvoiceDetailRepository.deleteAllByCargoInformationIdsIn(Arrays.asList(cargoInformation.getId()));
					}
					cargoInformation.setOutbInvoiceDetail(null);
				} else if (isRetail && outbInvoiceDetailsRequest != null && (outbInvoiceDetailsRequest.getCifValue() == null || outbInvoiceDetailsRequest.getCifValue() == 0d)
						&& (outbInvoiceDetailsRequest.getCurrencyId() == null || outbInvoiceDetailsRequest.getCurrencyId() == 0)
						&& (outbInvoiceDetailsRequest.getDiscountPercent() == null || outbInvoiceDetailsRequest.getDiscountPercent() == 0d)
						&& (outbInvoiceDetailsRequest.getInvoiceNumber() == null || outbInvoiceDetailsRequest.getInvoiceNumber().trim().isEmpty())
						&& outbInvoiceDetailsRequest.getInvoiceDate() == null) {
					if (cargoInformation.getId() != 0) {
						outbInvoiceDetailRepository.deleteAllByCargoInformationIdsIn(Arrays.asList(cargoInformation.getId()));
					}
					cargoInformation.setOutbInvoiceDetail(null);
				} else {
					if (outbInvoiceDetailsRequest != null && outbInvoiceDetailsRequest.getCurrencyId() != null && cargoInformation.getOutbInvoiceDetail() != null) {
						cargoInformation.getOutbInvoiceDetail().setCurrencyId(currencyDetailsService.getByCurrencyId(cargoInformationRequest.getOutbInvoiceDetail().getCurrencyId()));
					}
					if (cargoInformation.getOutbInvoiceDetail() != null) {
						cargoInformation.getOutbInvoiceDetail().setOutbCargoInformationId(cargoInformation);
					}
				}

				OutbBarCodeDetails barcodeDetails = null;
				OutbBarCodeDetailsRequest barCodeDetailRequest = null;

				if (cargoInformationRequest.getOutbBarcodeDetails() != null) {
					for (int j = 0; j < cargoInformation.getOutbBarcodeDetails().size(); j++) {
						barcodeDetails = cargoInformation.getOutbBarcodeDetails().get(j);
						barcodeDetails.setOutbCargoInformationId(cargoInformation);
						barCodeDetailRequest = cargoInformationRequest.getOutbBarcodeDetails().get(j);
						if (barCodeDetailRequest.getBarCodeId() != 0)
							barcodeDetails.setBarCodeId(barCodeService.getByBarCodeId(barCodeDetailRequest.getBarCodeId()));
					}
				}
			}
		}

		outbound.getOutbDeclarationInformation().setOutbound(outbound);
		outbound.getOutbShipmentDepartureInformation().setOutbound(outbound);

		try {
			//logger.info("EXIT:: addOrUpdateOutbound :: Add or Update outbound details.");
			Outbound save = outboundRepository.save(outbound);
			List<OutbUploadDocuments> documents = new ArrayList<OutbUploadDocuments>();
			documents.addAll(outBoundUploadDocuments(customDeclaration, save, MasterDataAppConstants.CUSTOM_DECLARATION));
			documents.addAll(outBoundUploadDocuments(packingList, save, MasterDataAppConstants.PACKING_LIST));
			documents.addAll(outBoundUploadDocuments(salesInvoice, save, MasterDataAppConstants.SALES_INVOICE));
			save.setOutbUploadDocuments(documents);
			
			if(outboundRequest.getDeletedSalesInvoiceDocuments() != null && !outboundRequest.getDeletedSalesInvoiceDocuments().isEmpty())
				outbUploadDocumentsService.cancelByIdIn(outboundRequest.getDeletedSalesInvoiceDocuments());
			if(outboundRequest.getDeletedPackingListDocuments() != null && !outboundRequest.getDeletedPackingListDocuments().isEmpty())
				outbUploadDocumentsService.cancelByIdIn(outboundRequest.getDeletedPackingListDocuments());
			if(outboundRequest.getDeletedCustomDeclarationDocuments() != null && !outboundRequest.getDeletedCustomDeclarationDocuments().isEmpty())
				outbUploadDocumentsService.cancelByIdIn(outboundRequest.getDeletedCustomDeclarationDocuments());
			
			PushApiResponse pushApiResponse = outboundPushData(save, oldCargoInformation, deletedCargos, deletedBarcodeDetails, isUpdate ? OperationType.UPDATE : OperationType.CREATE);
			save.setPushApiResponse(pushApiResponse);
			return save;
		} catch (DataIntegrityViolationException e) {
			logger.error("ERROR:: addOrUpdateOutbound :: Data integrity exception occured while saving Outbound.");
			throw new DataIntegrityViolationException("Error: already exist:--" + e.getMessage());
		}
	}
	
	@Override
	public PushApiResponse outboundPushData(Outbound outbound, List<OutbCargoInformation> oldCargoInformation, List<Long> deletedCargos, List<Long> deleteBarcodeDetails, OperationType operationType) {
		PushApiResponse pushApiResponse = new PushApiResponse();
		OutboundPushRequestRoot requestRoot = pushInventoryService.createOrUpdateOutboundPushRequest(outbound, oldCargoInformation, deletedCargos, deleteBarcodeDetails);
		List<String> messages = null;
		int itemsCount = 0, removedItemsCount = 0, successCount = 0, failedCount = 0;
		if (requestRoot != null && requestRoot.getItems() != null && !requestRoot.getItems().isEmpty()) {
			itemsCount = requestRoot.getItems().size();
			messages = new ArrayList<>();
			PushInventory pushInventory = pushInventoryService.createOutboundPushInventory(requestRoot, outbound, operationType);
			try {
				
				// removing OutboundPushRequestItems from being pushed if depending parent item is FAILED or PENDING
				ListIterator<OutboundPushRequestItems> iter = requestRoot.getItems().listIterator();
				OutboundPushRequestItems item = null;
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

				// if there is no OutboundPushRequestItems exist in OutboundPushRequestRoot then do not call PUSH API
				if (!requestRoot.getItems().isEmpty()) {
					OutboundPushResponseRoot responseRoot = wmsApiService.outboundPushData(requestRoot);
					UpdatePushInventoryStatusResponse sResponse = pushInventoryService.updateOutboundPushInventoryStatus(pushInventory, responseRoot); 
					successCount = sResponse.getSuccessCount();
					failedCount = sResponse.getFailedCount();
					messages.addAll(sResponse.getMessages());
				}
				
			} catch (CustomException e) {
				logger.error("ERROR :: outboundPushData :: Error while outbound push data : {}", e.getMessage());
				pushInventory.setStatus(StatusType.FAILED);
				for (PushInventoryItem item : pushInventory.getPushInventoryItems()) {
					if (!item.getStatus().equals(StatusType.SUCCESS)) {
						item.setStatus(StatusType.FAILED);
						messages.add(item.getSkuBarCode() + " : not pushed to Oracle");
						failedCount++;
					}
				}
				pushInventoryService.save(pushInventory);
				messages.addAll(Arrays.asList(MasterDataAppConstants.SOMETHING_WENT_WRONG));
			}
		}
		pushApiResponse.setMessages(messages);
		pushApiResponse.setCode(tmWmsUtils.getPushApiResponseCode(itemsCount, removedItemsCount, successCount, failedCount));
		return pushApiResponse;
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
	private List<OutbUploadDocuments> outBoundUploadDocuments(List<MultipartFile> files, Outbound outbound, String type) {
		List<OutbUploadDocuments> documents = new ArrayList<OutbUploadDocuments>();
		if (files != null && !files.isEmpty()) {
			OutbUploadDocuments document = null;
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					document = new OutbUploadDocuments();
					document.setOutbound(outbound);
					document.setUploadDocumentTypeId(uploadDocumentTypeRepository.findByName(type));
					document.setIsCancel(false);
					outbUploadDocumentsService.save(document);
					outbUploadDocumentsService.save(document, file);
					documents.add(document);
				}
			}
		}
		return documents;
	}

	@Override
	public Outbound getOutboundById(long outboundId) {
		return outboundRepository.findByOutboundId(outboundId);
	}

	@Override
	public List<Outbound> getOutboundByIds(List<Long> outboundIds) {
		return outboundRepository.findByOutboundIdIn(outboundIds);

	}

	@Override
	public OutboundOnloadResponse getOnLoadData(String serviceId) throws CustomException, Exception {

		OutboundOnloadResponse outboundOnloadResponse = new OutboundOnloadResponse();

		List<MarketTypeResponse> marketTypeResponse = objectMapper.convertValue(marketTypeService.getAll(), new TypeReference<List<MarketTypeResponse>>() {
		});
		outboundOnloadResponse.setMarketType(marketTypeResponse);

		List<CountryDetailsResponse> countryDetailResponse = objectMapper.convertValue(countryDetailsService.getAll(), new TypeReference<List<CountryDetailsResponse>>() {
		});
		outboundOnloadResponse.setCountryDetails(countryDetailResponse);

		List<GoodsConditionResponse> goodsConditionResponse = objectMapper.convertValue(goodsConditionService.getAll(), new TypeReference<List<GoodsConditionResponse>>() {
		});
		outboundOnloadResponse.setGoodCondition(goodsConditionResponse);

		List<CurrencyDetailsResponse> currencyDetailsResponse = objectMapper.convertValue(Arrays.asList(currencyDetailsService.findDefaultCurrency()),
				new TypeReference<List<CurrencyDetailsResponse>>() {
				});
		outboundOnloadResponse.setCurrencyDetails(currencyDetailsResponse);

		List<DeclarationTypeResponse> declarationTypeResponse = objectMapper.convertValue(declarationTypeService.getAll(), new TypeReference<List<DeclarationTypeResponse>>() {
		});
		outboundOnloadResponse.setDeclarationType(declarationTypeResponse);

		List<ShipmentSoldTypeResponse> shipmentSoldTypeResponse = objectMapper.convertValue(shipmentSoldTypeService.getAll(), new TypeReference<List<ShipmentSoldTypeResponse>>() {
		});
		outboundOnloadResponse.setShipmentSoldType(shipmentSoldTypeResponse);

		outboundOnloadResponse.setAgentDetailsList(tmWmsUtils.getImporterAgentsByLoggedInUser(serviceId));

		return outboundOnloadResponse;
	}

	@Override
	public Outbound findById(Long id) {
		return outboundRepository.findByOutboundId(id);
	}

	@Override
	public List<Outbound> findAll(Date fromDate, Date toDate, String businessCode, Boolean isCancel) {
		List<Outbound> data = null;
		OutboundSpecification specFromDate = new OutboundSpecification("fromDate", SpecificationOperation.GREATER_THAN_OR_EQUAL, fromDate);
		OutboundSpecification specToDate = new OutboundSpecification("toDate", SpecificationOperation.LESS_THAN_OR_EQUAL, toDate);
		OutboundSpecification specIsCancel = new OutboundSpecification("isCancel", SpecificationOperation.EQUAL, isCancel);
		OutboundSpecification specBusinessCode = new OutboundSpecification("businessCode", SpecificationOperation.EQUAL, businessCode);

		Specification<Outbound> spec = null;
		if (fromDate != null || toDate != null || isCancel != null || (businessCode != null && !businessCode.isEmpty())) {
			spec = Specification.where(specFromDate).and(specToDate).and(specIsCancel).and(specBusinessCode);
			data = outboundRepository.findAll(spec);
		}
		return data;
	}

	@Override
	public List<SearchInventory> filterSearchInventory(SearchInventoryFilterRequest vo, List<String> agentCodes, Boolean isCancel) {
		List<Outbound> data = null;
		if ((vo.getDeclarationNumber() == null || vo.getDeclarationNumber().trim().isEmpty()) && (vo.getFromDate() == null || vo.getFromDate().trim().isEmpty())
				&& (vo.getToDate() == null || vo.getToDate().trim().isEmpty()) && (vo.getHsCode() == null || vo.getHsCode().trim().isEmpty())
				&& (vo.getTypeOfInventory() == null || vo.getTypeOfInventory().trim().isEmpty())) {
			data = outboundRepository.findAllByBusinessCodeAndIsCancel(agentCodes, isCancel);
		} else {
			Date fromDate = (vo.getFromDate() != null && !vo.getFromDate().trim().isEmpty()) ? dateUtils.stringToDate(vo.getFromDate() + " 00:00:00", DateFormates.DDMMYYYYHHMMSS_HYPHEN) : null;
			Date toDate = (vo.getToDate() != null && !vo.getToDate().trim().isEmpty()) ? dateUtils.stringToDate(vo.getToDate() + " 23:59:59", DateFormates.DDMMYYYYHHMMSS_HYPHEN) : null;

			OutboundSpecification specDeclarationNumber = new OutboundSpecification("declarationNumber", SpecificationOperation.EQUAL, vo.getDeclarationNumber());
			OutboundSpecification specFromDate = new OutboundSpecification("fromDate", SpecificationOperation.GREATER_THAN_OR_EQUAL, fromDate);
			OutboundSpecification specToDate = new OutboundSpecification("toDate", SpecificationOperation.LESS_THAN_OR_EQUAL, toDate);
			OutboundSpecification specHsCode = new OutboundSpecification("hsCode", SpecificationOperation.EQUAL, vo.getHsCode());
			OutboundSpecification specBusinessCode = new OutboundSpecification("businessCode", SpecificationOperation.IN, agentCodes);
			OutboundSpecification specIsCancelCode = new OutboundSpecification("isCancel", SpecificationOperation.EQUAL, isCancel);

			Specification<Outbound> spec = Specification.where(specDeclarationNumber).and(specFromDate).and(specToDate).and(specHsCode).and(specBusinessCode).and(specIsCancelCode);
			data = outboundRepository.findAll(spec);
		}
		List<SearchInventory> list = new ArrayList<SearchInventory>();
		if (data != null && !data.isEmpty()) {
			list.addAll(data.stream().map(s -> new SearchInventory(s.getOutboundId(), s.getReferenceNumber(), s.getCreatedOn(), "Outbound",
					(s.getInvoiceDetail() != null ? s.getInvoiceDetail().getInvoiceDetailId() : null))).collect(Collectors.toList()));
		}
		return list;
	}

	private class OutboundSpecification implements Specification<Outbound> {
		private static final long serialVersionUID = 1L;

		private String key;
		private String operation;
		private Object value;

		OutboundSpecification(String key, String operation, Object value) {
			this.key = key;
			this.operation = operation;
			this.value = value;
		}

		@Override
		public Predicate toPredicate(Root<Outbound> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
//			System.out.println(key + " - " + operation + " - " + value);
			if (value != null && !value.toString().trim().isEmpty()) {
				if (operation.equalsIgnoreCase(SpecificationOperation.EQUAL) && key.equalsIgnoreCase("declarationNumber")) {
					return builder.equal(root.<OutbDeclarationInformation>get("outbDeclarationInformation").<String>get("inbDeclarationNumber"), value.toString());
				} else if (key.equalsIgnoreCase("fromDate") && operation.equalsIgnoreCase(SpecificationOperation.GREATER_THAN_OR_EQUAL)) {
					return builder.greaterThanOrEqualTo(root.<Date>get("createdOn"), (Date) value);
				} else if (key.equalsIgnoreCase("toDate") && operation.equalsIgnoreCase(SpecificationOperation.LESS_THAN_OR_EQUAL)) {
					return builder.lessThanOrEqualTo(root.<Date>get("createdOn"), (Date) value);
				} else if (key.equalsIgnoreCase("hsCode") && operation.equalsIgnoreCase(SpecificationOperation.EQUAL)) {
					return builder.equal(root.join("outbCargosInformation").join("hsCodeId").<String>get("hsCode"), value.toString());
				} else if (key.equalsIgnoreCase("businessCode") && operation.equalsIgnoreCase(SpecificationOperation.EQUAL)) {
					return builder.equal(root.<OutbDeclarationInformation>get("outbDeclarationInformation").<String>get("businessCode"), value.toString());
				} else if (key.equalsIgnoreCase("isCancel") && operation.equalsIgnoreCase(SpecificationOperation.EQUAL)) {
					return builder.equal(root.<Boolean>get("isCancel"), (Boolean) value);
				} else if (key.equalsIgnoreCase("businessCode") && operation.equalsIgnoreCase(SpecificationOperation.IN)) {
					final Join<Tag, Label> join = root.join("outbDeclarationInformation");
					return join.<String>get("businessCode").in((List<String>) value);
				}
			}
			return null;
		}
	}

	@Override
	public List<Outbound> cancelOutbound(List<Outbound> outboundList) throws CustomException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<Outbound> responseList = new ArrayList<Outbound>();

		for (Outbound outbound : outboundList) {
			if (dateUtils.numberOfDaysBetween(outbound.getCreatedOn(), new Date()) > inventoryRestrictUpdate) {
				throw new CustomException(OUTBOUND_CAN_NOT_BE_CANCELLED_AFTER_30DAYS);
			} else if (outbound.getInvoiceDetail() != null) {
				throw new CustomException(MasterDataAppConstants.OUTBOUND_CAN_NOT_BE_CANCELLED_AFTER_INVOICE_GENERATED);
			}
			outbound.setUpdatedOn(new Date());
			outbound.setUpdatedBy(auth.getName());
			outbound.setIsCancel(true);
			responseList.add(outbound);
		}
		outboundRepository.saveAll(responseList);
		
		for(Outbound outbound : outboundList) {
			OutboundPushRequestRoot requestRoot = pushInventoryService.cancelOutboundPushRequest(outbound, outbound.getOutbCargosInformation());
			if (requestRoot.getItems() != null && !requestRoot.getItems().isEmpty()) {
				PushInventory pushInventory = pushInventoryService.createOutboundPushInventory(requestRoot, outbound, OperationType.CANCEL);
				try {
					
					// removing existing FAILED OutboundPushRequestItems from OutboundPushRequestRoot
					ListIterator<OutboundPushRequestItems> iter = requestRoot.getItems().listIterator();
					OutboundPushRequestItems item = null;
					List<PushInventory> inventories = null;
					while (iter.hasNext()) {
						item = iter.next();
						inventories = pushInventoryRepository.findByInbDeclarationAndSkuBarcodeAndStatus(item.getInbDeclarationNumber(), item.getSkuBarcode(), StatusType.FAILED);
						if (inventories != null && !inventories.isEmpty())
							iter.remove();
					}

					// if there is no OutboundPushRequestItems exist in OutboundPushRequestRoot then do not call PUSH API
					if (!requestRoot.getItems().isEmpty()) {
						OutboundPushResponseRoot responseRoot = wmsApiService.outboundPushData(requestRoot);
						pushInventoryService.updateOutboundPushInventoryStatus(pushInventory, responseRoot);
					}
					
				} catch (CustomException e) {
					logger.error("ERROR :: cancelOutbound :: Error while outbound push data : {}", e.getMessage());
					pushInventory.setStatus(StatusType.FAILED);
					for (PushInventoryItem item : pushInventory.getPushInventoryItems())
						item.setStatus(StatusType.FAILED);
					pushInventoryService.save(pushInventory);
				}
			}
		}
		return responseList;
	}

	@Override
	public void updateOutboundsWithInvoice(List<Long> outboundIds, InvoiceDetail invoice) {
		outboundRepository.updateOutboundsWithInvoice(outboundIds, invoice);
	}

	@Override
	public void save(Outbound ob) {
		outboundRepository.save(ob);
	}
	
	@Override
	public Outbound updateDateByReferenceNumber(Outbound outbound, String createdOn) throws ParseException {

		Date newDate = dateUtils.stringToDate(createdOn, DateFormates.DDMMYYYY_HYPHEN);
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(newDate);

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(outbound.getCreatedOn());
		calendar2.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DATE));

		outbound.setCreatedOn(calendar2.getTime());
		return outboundRepository.save(outbound);
	}

	@Override
	public Outbound findByReferenceNumber(String referenceNumber) {
		return outboundRepository.findByReferenceNumber(referenceNumber);
	}
}

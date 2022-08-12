package com.dpworld.masterdataapp.service.impl;

import static com.dpworld.common.constants.DateFormates.DDMMYYYYHHMM_HYPHEN;
import static com.dpworld.common.constants.DateFormates.DDMMYYYYHHMM_SLASH;
import static com.dpworld.common.constants.DateFormates.DDMMYYYY_HYPHEN;
import static com.dpworld.common.constants.DateFormates.DDMMYYYY_SLASH;
import static com.dpworld.common.constants.DateFormates.HHMM;
import static com.dpworld.masterdataapp.constants.InvoiceConstants.COMPANYADDRESSLINE1;
import static com.dpworld.masterdataapp.constants.InvoiceConstants.COMPANYCODE;
import static com.dpworld.masterdataapp.constants.InvoiceConstants.COMPANYNAME;
import static com.dpworld.masterdataapp.constants.InvoiceConstants.CURRENCY;
import static com.dpworld.masterdataapp.constants.InvoiceConstants.GRANDTOTAL;
import static com.dpworld.masterdataapp.constants.InvoiceConstants.INVOICECREATEDON;
import static com.dpworld.masterdataapp.constants.InvoiceConstants.INVOICEFROM;
import static com.dpworld.masterdataapp.constants.InvoiceConstants.INVOICENUMBER;
import static com.dpworld.masterdataapp.constants.InvoiceConstants.INVOICETO;
import static com.dpworld.masterdataapp.constants.InvoiceConstants.PRINTDATE;
import static com.dpworld.masterdataapp.constants.InvoiceConstants.PRINTTIME;
import static com.dpworld.masterdataapp.constants.InvoiceConstants.SUBTOTAL;
import static com.dpworld.masterdataapp.constants.InvoiceConstants.TOTALDISCOUNTAMOUNT;
import static com.dpworld.masterdataapp.constants.InvoiceConstants.TOTALDUTYAMOUNT;
import static com.dpworld.masterdataapp.constants.InvoiceConstants.TOTALQUANTITY;
import static com.dpworld.masterdataapp.constants.InvoiceConstants.TOTALSOLDAMOUNT;
import static com.dpworld.masterdataapp.constants.InvoiceConstants.TOTALWEIGHT;
import static com.dpworld.masterdataapp.constants.InvoiceConstants.TRNNUMBER;
import static com.dpworld.masterdataapp.constants.InvoiceConstants.VATAMOUNT;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.BASE64PREFIXJPEG;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.BASE64PREFIXPDF;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.BUSINESS_CODE_NOT_FOUND;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.JPEG;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.JPG;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.NO_DATA_TO_EXPORT;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.PDF;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dpworld.common.constants.DateFormates;
import com.dpworld.common.utils.CustomException;
import com.dpworld.common.utils.DateUtils;
import com.dpworld.common.utils.Utils;
import com.dpworld.masterdataapp.constants.ReferenceNumberGenerationType;
import com.dpworld.masterdataapp.model.request.GenerateInvoiceRequest;
import com.dpworld.masterdataapp.model.response.FileResponse;
import com.dpworld.masterdataapp.model.response.InvoiceCargoInfo;
import com.dpworld.masterdataapp.service.CurrencyDetailsService;
import com.dpworld.masterdataapp.service.HsCodeService;
import com.dpworld.masterdataapp.service.InvoiceDetailService;
import com.dpworld.masterdataapp.service.OutboundService;
import com.dpworld.masterdataapp.service.ShipmentSoldTypeService;
import com.dpworld.masterdataapp.service.VatService;
import com.dpworld.masterdataapp.service.WmsApiService;
import com.dpworld.masterdataapp.utility.TmWmsUtils;
import com.dpworld.masterdataapp.webServiceResponse.JsonResponseData.Address;
import com.dpworld.masterdataapp.webServiceResponse.JsonResponseData.Agents;
import com.dpworld.persistence.entity.HsCode;
import com.dpworld.persistence.entity.InvoiceDetail;
import com.dpworld.persistence.entity.InvoiceOutbounds;
import com.dpworld.persistence.entity.OutbCargoInformation;
import com.dpworld.persistence.entity.OutbInvoiceDetail;
import com.dpworld.persistence.entity.Outbound;
import com.dpworld.persistence.entity.Vat;
import com.dpworld.persistence.repository.InvoiceDetailRepository;
import com.dpworld.persistence.repository.InvoiceOutboundsRepository;
import com.dpworld.persistence.vo.InvoiceDetailsListVo;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class InvoiceDetailServiceImpl implements InvoiceDetailService {
	
	@Autowired
	private InvoiceDetailRepository invoiceDetailRepository;

	@Autowired
	private HsCodeService hsCodeService;

	@Autowired
	private ShipmentSoldTypeService shipmentSoldTypeService;

	@Autowired
	private InvoiceOutboundsRepository invoiceOutboundsRepository;

	@Autowired
	private DateUtils dateUtils;

	@Autowired
	private WmsApiService wmsApiService;

	@Autowired
	private OutboundService outboundService;

	@Autowired
	private VatService vatService;

	@Autowired
	private TmWmsUtils tmWmsUtils;

	@Autowired
	private CurrencyDetailsService currencyDetailsService;
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void save(InvoiceDetail invoice) {
		invoiceDetailRepository.save(invoice);
	}

	@Override
	public StringBuilder validateGenerateInvoiceRequest(GenerateInvoiceRequest vo) {
		StringBuilder errMessage = new StringBuilder();
		if (vo.getFromDate() == null || vo.getFromDate().trim().isEmpty() || vo.getToDate() == null || vo.getToDate().trim().isEmpty())
			errMessage.append("Please select FromDate and ToDate.\n");
		if (vo.getBusinessCode() == null || vo.getBusinessCode().trim().isEmpty())
			errMessage.append("Please select Business Code.\n");
		if (vo.getFormat() == null || vo.getFormat().trim().isEmpty())
			errMessage.append("Please select format.\n");
		if (vo.getFormat() != null && !vo.getFormat().equalsIgnoreCase(PDF) && !vo.getFormat().equalsIgnoreCase(JPG) && !vo.getFormat().equalsIgnoreCase(JPEG))
			errMessage.append("Please select valid format.\n");
		return errMessage;
	}

	@Override
	public StringBuilder validateViewInvoiceRequest(GenerateInvoiceRequest vo) {
		StringBuilder errMessage = new StringBuilder();
		if (vo.getInvoiceId() == null || vo.getInvoiceId() == 0)
			errMessage.append("Please enter valid Invoice ID.\n");
		if (vo.getFormat() == null || vo.getFormat().trim().isEmpty())
			errMessage.append("Please select format.\n");
		if (vo.getFormat() != null && !vo.getFormat().equalsIgnoreCase(PDF) && !vo.getFormat().equalsIgnoreCase(JPG) && !vo.getFormat().equalsIgnoreCase(JPEG))
			errMessage.append("Please select valid format.\n");
		return errMessage;
	}

	@SuppressWarnings("static-access")
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public FileResponse generateInvoice(GenerateInvoiceRequest vo, String serviceId) throws JRException, CustomException, Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();

		Date fromDate = dateUtils.stringToDate(vo.getFromDate() + " 00:00:00", DateFormates.DDMMYYYYHHMMSS_HYPHEN);
		Date toDate = dateUtils.stringToDate(vo.getToDate() + " 23:59:59", DateFormates.DDMMYYYYHHMMSS_HYPHEN);

		List<Agents> agents = tmWmsUtils.getImporterAgentsByLoggedInUser(serviceId);
		if (agents != null && !agents.isEmpty()) {
			Agents agent = agents.stream().filter(p -> (p.getAgentTypeCode() != null && p.getAgentCode().equalsIgnoreCase(vo.getBusinessCode()))).findFirst().orElse(null);
			if (agent != null) {
				parameters.put(COMPANYNAME, agent.getAgentName() != null ? agent.getAgentName() : " ");
				parameters.put(TRNNUMBER, (agent.getVatNumber() != null && !agent.getVatNumber().equalsIgnoreCase("null")) ? agent.getVatNumber() : "NA");
				parameters.put(COMPANYCODE, agent.getAgentCode() != null ? agent.getAgentCode() : " ");
				if (agent.getAddressList() != null && !agent.getAddressList().isEmpty()) {
					List<Address> addressList = agent.getAddressList().stream().filter(p -> p.getAdressType() != null && p.getAdressType().equalsIgnoreCase("Establishment"))
							.collect(Collectors.toList());
					if (addressList != null && !addressList.isEmpty()) {
						Address address = addressList.get(0);
						parameters.put(COMPANYADDRESSLINE1, (address.getAddress() != null && !address.getAddress().equalsIgnoreCase("null")) ? address.getAddress() : "");
					} else
						parameters.put(COMPANYADDRESSLINE1, "");
				} else
					parameters.put(COMPANYADDRESSLINE1, "");
			} else
				throw new CustomException(BUSINESS_CODE_NOT_FOUND);
		} else
			throw new CustomException(BUSINESS_CODE_NOT_FOUND);

		List<Outbound> outboundList = outboundService.findAll(fromDate, toDate, vo.getBusinessCode(), false);
		List<OutbCargoInformation> cargos = new ArrayList<OutbCargoInformation>();
		if (outboundList != null) {
			for (Outbound ob : outboundList) {
				for (OutbCargoInformation cargo : ob.getOutbCargosInformation()) {
					if (cargo.getOutbInvoiceDetail() != null && cargo.getOutbInvoiceDetail().getCifValue() != null && cargo.getOutbInvoiceDetail().getCifValue() > 0)
						cargos.add(cargo);
				}
			}
		}
		
		if (cargos == null || cargos.isEmpty())
			throw new CustomException(NO_DATA_TO_EXPORT);
		parameters.put(INVOICEFROM, vo.getFromDate().replace("-", "/"));
		parameters.put(INVOICETO, vo.getToDate().replace("-", "/"));
		List<InvoiceCargoInfo> cargoInfoList = new ArrayList<InvoiceCargoInfo>();

		Double totalSoldAmount = 0d, totalDiscountAmount = 0d, totalDutyAmount = 0d, subTotal = 0d, vatAmount = 0d,
				totalWeight = 0d, totalQuantity = 0d, soldAmount = 0d, discountAmount = 0d, dutyAmount = 0d, netAmount = 0d;
		for (OutbCargoInformation cargo : cargos) {
			HsCode hsCode = cargo.getHsCodeId();
			InvoiceCargoInfo cargoInfo = cargoInfoList.stream().filter(c -> c.getInventoryType().equals(cargo.getOutbound().getDisplayShipmentSoldType()) && c.getHsCode().equals(hsCode.getHsCode()))
					.findFirst().orElse(null);
			if (cargoInfo == null) {
				cargoInfo = new InvoiceCargoInfo();
				cargoInfo.setHsCode(hsCode.getHsCode());
				cargoInfo.setInventoryType(cargo.getOutbound().getDisplayShipmentSoldType());
				cargoInfo.setItemDescription(hsCode.getHsCodeDescription());
				cargoInfoList.add(cargoInfo);
			}
			OutbInvoiceDetail invoice = cargo.getOutbInvoiceDetail();
			soldAmount = invoice.getCifValue();
			discountAmount = 0d;
			dutyAmount = 0d;
			netAmount = 0d;
			if (invoice.getDiscountPercent() != null && invoice.getDiscountPercent() != 0)
				discountAmount = Utils.doubleRoundUpto(((invoice.getDiscountPercent() * soldAmount) / 100), 2);
			if (cargo.getDutyValue() != null && cargo.getDutyValue() != 0)
				dutyAmount = Utils.doubleRoundUpto(((cargo.getDutyValue() * soldAmount) / 100), 2);
			netAmount = Utils.doubleRoundUpto(((soldAmount + dutyAmount) - discountAmount), 2);
			cargoInfo.setQuantity(cargoInfo.getQuantity() + cargo.getPiecesQuantity());
			cargoInfo.setSoldAmount(cargoInfo.getSoldAmount() + soldAmount);
			cargoInfo.setDiscountAmount(Utils.doubleRoundUpto(cargoInfo.getDiscountAmount() + discountAmount, 2));
			cargoInfo.setDutyAmount(Utils.doubleRoundUpto(cargoInfo.getDutyAmount() + dutyAmount, 2));
			cargoInfo.setNetAmount(Utils.doubleRoundUpto(cargoInfo.getNetAmount() + netAmount, 2));
			cargoInfo.setWeight(Utils.doubleRoundUpto(cargoInfo.getNetAmount() + cargo.getPiecesNetWeight(), 2));
			totalQuantity = totalQuantity + cargo.getPiecesQuantity();
			totalSoldAmount = totalSoldAmount + soldAmount;
			totalDiscountAmount = totalDiscountAmount + discountAmount;
			totalDutyAmount = totalDutyAmount + dutyAmount;
			subTotal = subTotal + netAmount;
			totalWeight = totalWeight + cargo.getPiecesNetWeight();
		}
//		for (int i = 0; i <= 600; i++) {
//			cargoInfoList.add(new InvoiceCargoInfo("48203010", MasterDataAppConstants.RETAIL, 10.0, 100.0, 10.0, 20.0, 110.0, "test", 100.0));
//		}
		if (cargoInfoList == null || cargoInfoList.isEmpty())
			throw new CustomException(NO_DATA_TO_EXPORT);
		Date today = new Date();
		Vat vat = vatService.getLatest();
		if (vat != null)
			vatAmount = (subTotal * vat.getVat()) / 100;
		parameters.put(PRINTDATE, dateUtils.dateToString(today, DDMMYYYY_HYPHEN));
		parameters.put(PRINTTIME, dateUtils.dateToString(today, HHMM));
		parameters.put(INVOICECREATEDON, dateUtils.dateToString(today, DDMMYYYYHHMM_SLASH));
		parameters.put(TOTALSOLDAMOUNT, Utils.doubleRoundUpto(totalSoldAmount, 2));
		parameters.put(TOTALDISCOUNTAMOUNT, Utils.doubleRoundUpto(totalDiscountAmount, 2));
		parameters.put(TOTALWEIGHT, Utils.doubleRoundUpto(totalWeight, 2));
		parameters.put(TOTALDUTYAMOUNT, Utils.doubleRoundUpto(totalDutyAmount, 2));
		parameters.put(TOTALQUANTITY, Utils.doubleRoundUpto(totalQuantity, 2));
		parameters.put(SUBTOTAL, Utils.doubleRoundUpto(subTotal, 2));
		parameters.put(VATAMOUNT, Utils.doubleRoundUpto(vatAmount, 2));
		parameters.put(GRANDTOTAL, Utils.doubleRoundUpto(subTotal + vatAmount, 2));
		
		InvoiceDetail invoice = createInvoice(parameters, cargoInfoList, outboundList);
		parameters.put(INVOICENUMBER, invoice.getReferenceNumber());
		parameters.put(CURRENCY, invoice.getCurrency());
		
		String prefix = "";
		InputStream instream = this.getClass().getResourceAsStream("/invoice.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(instream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(cargoInfoList));
		int pages = 1;
		if (jasperPrint.getPages() != null && !jasperPrint.getPages().isEmpty()) {
			pages = jasperPrint.getPages().size();
			invoice.setInvoicePages(pages);
			save(invoice);
		}
		
		byte[] bytes = null;
		if (vo.getFormat().equalsIgnoreCase(JPG) || vo.getFormat().equalsIgnoreCase(JPEG)) {
			JasperPrintManager printManager = JasperPrintManager.getInstance(DefaultJasperReportsContext.getInstance());
			BufferedImage finalBufferedImage = null;
			if (pages > 1) {
				List<BufferedImage> bufferedImages = new ArrayList<>();
				for (int i = 0; i < pages; i++)
					bufferedImages.add((BufferedImage) printManager.printPageToImage(jasperPrint, i, 5f));
				finalBufferedImage = Utils.convertPdfByteArrayToJpgByteArray(bufferedImages);
			} else
				finalBufferedImage = (BufferedImage) printManager.printPageToImage(jasperPrint, 0, 1f);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			ImageIO.write(finalBufferedImage, "jpeg", stream); // DO NOT USE JPEG CONSTANT HERE
			bytes = stream.toByteArray();
			prefix = BASE64PREFIXJPEG;
		} else if (vo.getFormat().equalsIgnoreCase(PDF)) {
			bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			prefix = BASE64PREFIXPDF;
		}
		return new FileResponse(prefix + Base64.getEncoder().encodeToString(bytes), invoice.getReferenceNumber());
	}
	
	@SuppressWarnings("static-access")
	@Override
	public FileResponse viewInvoice(InvoiceDetail invoice, String format) throws JRException, CustomException, Exception {
		List<InvoiceCargoInfo> cargoInfoList = new ArrayList<InvoiceCargoInfo>();
		for (InvoiceOutbounds ob : invoice.getInvoiceOutbounds()) {
			InvoiceCargoInfo cargoInfo = new InvoiceCargoInfo();
			cargoInfo.setHsCode(ob.getHsCode().getHsCode());
			cargoInfo.setInventoryType(ob.getShipmentSoldType().getShipmentSoldType());
			cargoInfo.setQuantity(ob.getQuantity());
			cargoInfo.setSoldAmount(ob.getSoldValue());
			cargoInfo.setDiscountAmount(ob.getDiscountValue());
			cargoInfo.setDutyAmount(ob.getDutyValue());
			cargoInfo.setNetAmount(ob.getNetAmount());
			cargoInfo.setItemDescription(ob.getHsCode().getHsCodeDescription());
			cargoInfoList.add(cargoInfo);
		}
		if (cargoInfoList == null || cargoInfoList.isEmpty())
			throw new CustomException(NO_DATA_TO_EXPORT);
		Date today = new Date();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(INVOICEFROM, dateUtils.dateToString(invoice.getFromDate(), DDMMYYYY_SLASH));
		parameters.put(INVOICETO, dateUtils.dateToString(invoice.getToDate(), DDMMYYYY_SLASH));
		parameters.put(COMPANYADDRESSLINE1, invoice.getCompanyAddress() != null ? invoice.getCompanyAddress() : "");
		parameters.put(COMPANYNAME, invoice.getCompanyName() != null ? invoice.getCompanyName() : "");
		parameters.put(TRNNUMBER, invoice.getTrnNumber() != null ? invoice.getTrnNumber() : "");
		parameters.put(INVOICENUMBER, invoice.getReferenceNumber() != null ? invoice.getReferenceNumber() : "");
		parameters.put(PRINTDATE, dateUtils.dateToString(today, DDMMYYYY_HYPHEN));
		parameters.put(PRINTTIME, dateUtils.dateToString(today, HHMM));
		parameters.put(INVOICECREATEDON, dateUtils.dateToString(invoice.getCreatedOn(), DDMMYYYYHHMM_HYPHEN));
		parameters.put(TOTALSOLDAMOUNT, invoice.getTotalSoldAmount());
		parameters.put(TOTALDISCOUNTAMOUNT, invoice.getTotalDiscountAmount());
		parameters.put(TOTALDUTYAMOUNT, invoice.getTotalDutyAmount());
		parameters.put(TOTALQUANTITY, invoice.getTotalQuantity());
		parameters.put(SUBTOTAL, invoice.getSubTotal());
		parameters.put(VATAMOUNT, invoice.getVatValue());
		parameters.put(GRANDTOTAL, invoice.getGrandTotal());
		parameters.put(CURRENCY, invoice.getCurrency());

		byte[] bytes = null;
		String prefix = "";
		InputStream instream = this.getClass().getResourceAsStream("/invoice.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(instream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(cargoInfoList));
		int pages = jasperPrint.getPages().size();
		if (format.equalsIgnoreCase(JPG) || format.equalsIgnoreCase(JPEG)) {
			JasperPrintManager printManager = JasperPrintManager.getInstance(DefaultJasperReportsContext.getInstance());
			BufferedImage finalBufferedImage = null;
			
			if (pages > 1) {
				List<BufferedImage> bufferedImages = new ArrayList<>();
				for (int i = 0; i < pages; i++)
					bufferedImages.add((BufferedImage) printManager.printPageToImage(jasperPrint, i, 5f));
				finalBufferedImage = Utils.convertPdfByteArrayToJpgByteArray(bufferedImages);
			} else
				finalBufferedImage = (BufferedImage) printManager.printPageToImage(jasperPrint, 0, 1f);
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			ImageIO.write(finalBufferedImage, "jpeg", stream); // DO NOT USE JPEG CONSTANT HERE
			bytes = stream.toByteArray();
			prefix = BASE64PREFIXJPEG;
		} else if (format.equalsIgnoreCase(PDF)) {
			bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			prefix = BASE64PREFIXPDF;
		}
		return new FileResponse(prefix + Base64.getEncoder().encodeToString(bytes), invoice.getReferenceNumber());
	}

	@Transactional(propagation = Propagation.MANDATORY)
	@Override
	public InvoiceDetail createInvoice(Map<String, Object> map, List<InvoiceCargoInfo> cargoInfoList, List<Outbound> outboundList) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		InvoiceDetail invoice = new InvoiceDetail();
		boolean flag = true;
		if (outboundList != null && !outboundList.isEmpty()) {
			for (Outbound ob : outboundList) {
				if (flag && ob.getOutbCargosInformation() != null && !ob.getOutbCargosInformation().isEmpty()) {
					for (OutbCargoInformation cargo : ob.getOutbCargosInformation()) {
						if (flag && cargo.getOutbInvoiceDetail() != null) {
							invoice.setCurrency(cargo.getOutbInvoiceDetail().getCurrencyId().getCurrencyName());
							flag = false;
						}
						if (!flag)
							break;
					}
				}
				if (!flag)
					break;
			}
		}
		
		if (invoice.getCurrency() == null)
			invoice.setCurrency(currencyDetailsService.findDefaultCurrency().getCurrencyName());
		
		String referenceNumber = wmsApiService.getReferenceCode(ReferenceNumberGenerationType.INVOICE);
		invoice.setFromDate(dateUtils.stringToDate(map.get(INVOICEFROM) + "", DDMMYYYY_SLASH));
		invoice.setToDate(dateUtils.stringToDate(map.get(INVOICETO) + "", DDMMYYYY_SLASH));
		invoice.setReferenceNumber(referenceNumber);
		invoice.setTransactionNumber(""); // Pending
		invoice.setCompanyCode(map.get(COMPANYCODE) + "");
		invoice.setCompanyName(map.get(COMPANYNAME) + "");
		invoice.setTrnNumber(map.get(TRNNUMBER) + "");
		invoice.setCompanyAddress(map.get(COMPANYADDRESSLINE1) + "");
		invoice.setCreatedBy(auth.getName());
		invoice.setCreatedOn(new Date());
		invoice.setIsCancel(false);

		invoice.setVatValue((Double) map.get(VATAMOUNT));
		invoice.setSubTotal((Double) map.get(SUBTOTAL));
		invoice.setGrandTotal((Double) map.get(GRANDTOTAL));
		invoice.setTotalSoldAmount((Double) map.get(TOTALSOLDAMOUNT));
		invoice.setTotalDiscountAmount((Double) map.get(TOTALDISCOUNTAMOUNT));
		invoice.setTotalDutyAmount((Double) map.get(TOTALDUTYAMOUNT));
		invoice.setTotalWeight((Double) map.get(TOTALWEIGHT));
		invoice.setTotalQuantity((Double) map.get(TOTALQUANTITY));
		invoice.setOutbounds(outboundList);
		invoiceDetailRepository.save(invoice);
		for (InvoiceCargoInfo info : cargoInfoList) {
			InvoiceOutbounds ib = new InvoiceOutbounds();
			ib.setInvoiceDetail(invoice);
			ib.setDiscountValue(info.getDiscountAmount());
			ib.setDutyValue(info.getDutyAmount());
			ib.setHsCode(hsCodeService.findByHsCode(info.getHsCode()));
			ib.setNetAmount(info.getNetAmount());
			ib.setQuantity(info.getQuantity());
			ib.setShipmentSoldType(shipmentSoldTypeService.findByShipmentSoldType(info.getInventoryType()));
			ib.setSoldValue(info.getSoldAmount());
			invoiceOutboundsRepository.save(ib);
		}
		if (outboundList != null && !outboundList.isEmpty()) {
			outboundService.updateOutboundsWithInvoice(outboundList.stream().map(Outbound::getOutboundId).collect(Collectors.toList()), invoice);
		}
		return invoice;
	}

	@Override
	public List<InvoiceDetail> findAll() {
		return invoiceDetailRepository.findAll();
	}

	@Override
	public InvoiceDetail findById(Long id) {
		return invoiceDetailRepository.findByInvoiceDetailId(id);
	}

	@Override
	public List<InvoiceDetail> findByDateRangeOverlapAndBusinessCode(Date fromDate, Date toDate, String businessCode) {
		return invoiceDetailRepository.findAllByDateRangeOverlapAndBusinessCode(fromDate, toDate, businessCode);
	}

	@Override
	public void clearInvoice(Long id) {
		if (id != null && id != 0) {
			InvoiceDetail detail = invoiceDetailRepository.findByInvoiceDetailId(id);
			if (detail != null) {
				for (Outbound ob : detail.getOutbounds()) {
					ob.setInvoiceDetail(null);
					outboundService.save(ob);
				}
				for (InvoiceOutbounds ob : detail.getInvoiceOutbounds())
					invoiceOutboundsRepository.delete(ob);
				invoiceDetailRepository.delete(detail);
			}
		} else {
			List<InvoiceDetail> list = invoiceDetailRepository.findAll();
			for (InvoiceDetail detail : list) {
				for (Outbound ob : detail.getOutbounds()) {
					ob.setInvoiceDetail(null);
					outboundService.save(ob);
				}
				invoiceOutboundsRepository.deleteAll(detail.getInvoiceOutbounds());
			}
			invoiceDetailRepository.deleteAll(list);
		}
	}

	@Override
	public List<InvoiceDetail> findAllByCreatedDateBetweenAndBusinessCodeAndIsCancel(Date fromDate, Date toDate, String businessCode, boolean isCancel) {
		return invoiceDetailRepository.findAllByCreatedDateBetweenAndBusinessCodeAndIsCancel(fromDate, toDate, businessCode, isCancel);
	}

	@Override
	public List<InvoiceDetailsListVo> list() {
		return invoiceDetailRepository.list();
	}

	@Override
	public List<InvoiceDetail> findAllByCompanyCodes(List<String> agentCodes) {
		return invoiceDetailRepository.findAllByCompanyCodes(agentCodes);
	}
	
	@Override
	public InvoiceDetail updateDateByReferenceNumber(InvoiceDetail invoice, String createdOn) throws ParseException {

		Date newDate = dateUtils.stringToDate(createdOn, DateFormates.DDMMYYYY_HYPHEN);
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(newDate);

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(invoice.getCreatedOn());
		calendar2.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DATE));

		invoice.setCreatedOn(calendar2.getTime());
		return invoiceDetailRepository.save(invoice);
	}

	@Override
	public InvoiceDetail findByReferenceNumber(String referenceNumber) {
		return invoiceDetailRepository.findByReferenceNumber(referenceNumber);
	}

}

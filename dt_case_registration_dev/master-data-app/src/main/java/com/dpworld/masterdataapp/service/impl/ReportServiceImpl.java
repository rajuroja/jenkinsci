package com.dpworld.masterdataapp.service.impl;

import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.BASE64PREFIXJPEG;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.BASE64PREFIXPDF;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.INVALID_DATE_FORMAT;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.JPEG;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.JPG;
import static com.dpworld.masterdataapp.constants.MasterDataAppConstants.PDF;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dpworld.common.constants.DateFormates;
import com.dpworld.common.utils.CustomException;
import com.dpworld.common.utils.DateUtils;
import com.dpworld.masterdataapp.constants.MasterDataAppConstants;
import com.dpworld.masterdataapp.constants.ReferenceNumberGenerationType;
import com.dpworld.masterdataapp.model.request.GenerateReportRequest;
import com.dpworld.masterdataapp.model.response.FileResponse;
import com.dpworld.masterdataapp.service.HsCodeService;
import com.dpworld.masterdataapp.service.ReportService;
import com.dpworld.masterdataapp.service.WmsApiService;
import com.dpworld.persistence.entity.GenericReportData;
import com.dpworld.persistence.entity.HsCode;
import com.dpworld.persistence.entity.MismatchReportData;
import com.dpworld.persistence.entity.Report;
import com.dpworld.persistence.entity.ReportType;
import com.dpworld.persistence.repository.GenericReportDataRepository;
import com.dpworld.persistence.repository.MismatchReportDataRepository;
import com.dpworld.persistence.repository.ReportRepository;
import com.dpworld.persistence.vo.GenericReportDataResponse;
import com.dpworld.persistence.vo.MismatchReportDataResponse;
import com.dpworld.persistence.vo.ReportListResponse;

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
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private DateUtils dateUtils;

	@Autowired
	private MismatchReportDataRepository mismatchReportDataRepository;

	@Autowired
	private GenericReportDataRepository genericReportDataRepository;

	@Autowired
	private WmsApiService wmsApiService;

	@Autowired
	private HsCodeService hsCodeService;

	@Override
	public Report findByReportId(Long reportId) {
		return reportRepository.findByReportId(reportId);
	}

	@Override
	public List<Report> findAll() {
		return reportRepository.findAll();
	}

	@Override
	public List<ReportListResponse> list() {
		return reportRepository.list();
	}

	@Override
	public List<ReportListResponse> listByBusinessCodes(List<String> agentCodes) {
		return reportRepository.listByBusinessCodes(agentCodes);
	}

	@Override
	public Report findByDateRangeOverlapAndBusinessCode(Date fromDate, Date toDate, String businessCode, ReportType reportType) {
		List<Report> reports = reportRepository.findByDateRangeOverlapAndBusinessCodeAndReportType(fromDate, toDate, businessCode, reportType);
		if (reports != null && !reports.isEmpty())
			return reports.get(0);
		return null;
	}

	@Override
	public StringBuilder validateGenerateReportRequest(GenerateReportRequest vo) {
		StringBuilder errMessage = new StringBuilder();
		if (vo.getFromDate() == null || vo.getToDate() == null)
			errMessage.append("Please select FromDate and ToDate.\n");
		if (vo.getReportType() == null || vo.getReportType().trim().isEmpty())
			errMessage.append("Please select report type.\n");
		else if (!vo.getReportType().equalsIgnoreCase(ReportType.MISMATCH.getReportType()) && !vo.getReportType().equalsIgnoreCase(ReportType.GENERIC.getReportType()))
			errMessage.append("Please select valid report type.\n");
		if (vo.getFormat() == null || vo.getFormat().trim().isEmpty())
			errMessage.append("Please select format.\n");
		else if (!vo.getFormat().equalsIgnoreCase(PDF) && !vo.getFormat().equalsIgnoreCase(JPG) && !vo.getFormat().equalsIgnoreCase(JPEG)
				&& !vo.getFormat().equalsIgnoreCase(MasterDataAppConstants.EXCEL))
			errMessage.append("Please select valid format.\n");
		return errMessage;
	}

	@Override
	public StringBuilder validateViewReportRequest(GenerateReportRequest vo) {
		StringBuilder errMessage = new StringBuilder();
		if (vo.getReportId() == null || vo.getReportId() == 0)
			errMessage.append("Please select report.\n");
		if (vo.getFormat() == null || vo.getFormat().trim().isEmpty())
			errMessage.append("Please select format.\n");
		else if (!vo.getFormat().equalsIgnoreCase(PDF) && !vo.getFormat().equalsIgnoreCase(JPG) && !vo.getFormat().equalsIgnoreCase(JPEG)
				&& !vo.getFormat().equalsIgnoreCase(MasterDataAppConstants.EXCEL))
			errMessage.append("Please select valid format.\n");
		return errMessage;
	}

	@SuppressWarnings("static-access")
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public FileResponse generateMismatchReport(GenerateReportRequest vo) throws CustomException, IOException, JRException, Exception {
		Date fromDate = dateUtils.stringToDate(vo.getFromDate(), DateFormates.DDMMYYYY_HYPHEN);
		Date toDate = dateUtils.stringToDate(vo.getToDate(), DateFormates.DDMMYYYY_HYPHEN);
		if (fromDate == null || toDate == null)
			throw new CustomException(INVALID_DATE_FORMAT);

//		Report existing = findByDateRangeOverlapAndBusinessCode(fromDate, toDate, vo.getBusinessCode(), ReportType.MISMATCH);
//		if (existing != null) {
//			throw new CustomException(ReportType.MISMATCH.getReportType() + " Report from " + existing.getFromDate() + " to " + existing.getToDate() + " for Business code "
//					+ existing.getBusinessCode() + " already exist with Reference number " + existing.getReferenceNumber());
//		}

		Date fromDate2 = dateUtils.stringToDate(vo.getFromDate() + " 00:00:00", DateFormates.DDMMYYYYHHMMSS_HYPHEN);
		Date toDate2 = dateUtils.stringToDate(vo.getToDate() + " 23:59:59", DateFormates.DDMMYYYYHHMMSS_HYPHEN);
		
		List<MismatchReportDataResponse> list = reportRepository.generateMismatchReport(fromDate2, toDate2, vo.getBusinessCode());
		if (list != null && !list.isEmpty()) {
			Report report = createMismatchReport(list, fromDate, toDate, vo.getBusinessCode(), vo.getConsigneeName());
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("reportFrom", vo.getFromDate());
			parameters.put("reportTo", vo.getToDate());
			parameters.put("consigneeName", vo.getConsigneeName());
			String prefix = "";
			byte[] bytes = null;
			if (vo.getFormat().equalsIgnoreCase(JPG) || vo.getFormat().equalsIgnoreCase(JPEG) || vo.getFormat().equalsIgnoreCase(PDF)) {
				InputStream instream = this.getClass().getResourceAsStream("/mismatch_report.jrxml");
				JasperReport jasperReport = JasperCompileManager.compileReport(instream);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(report.getMismatchReportDatas()));
				if (vo.getFormat().equalsIgnoreCase(JPG) || vo.getFormat().equalsIgnoreCase(JPEG)) {
					JasperPrintManager printManager = JasperPrintManager.getInstance(DefaultJasperReportsContext.getInstance());
					BufferedImage rendered_image = (BufferedImage) printManager.printPageToImage(jasperPrint, 0, 1.6f);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					ImageIO.write(rendered_image, "jpeg", stream); // DO NOT USE JPEG CONSTANT HERE
					bytes = stream.toByteArray();
					prefix = BASE64PREFIXJPEG;
				} else if (vo.getFormat().equalsIgnoreCase(PDF)) {
					bytes = JasperExportManager.exportReportToPdf(jasperPrint);
					prefix = BASE64PREFIXPDF;
				}
			} else if (vo.getFormat().equalsIgnoreCase(MasterDataAppConstants.EXCEL)) {
				bytes = exportMismatchReportToExcel(report);
				prefix = MasterDataAppConstants.BASE64XLSXMIMEPREFIX;
			}
			return new FileResponse(prefix + Base64.getEncoder().encodeToString(bytes), report.getReferenceNumber());
		} else
			throw new CustomException(MasterDataAppConstants.NO_DATA_TO_EXPORT);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	@Override
	public Report createMismatchReport(List<MismatchReportDataResponse> list, Date fromDate, Date toDate, String businessCode, String consigneeName) throws Exception {
		Report report = new Report();
		report.setCreatedOn(new Date());
		report.setCreatedBy(" ");
		report.setFromDate(fromDate);
		report.setIsCancel(false);
		report.setToDate(toDate);
		report.setConsigneeName(consigneeName != null ? consigneeName : "");
		report.setBusinessCode(businessCode);
		report.setReportType(ReportType.MISMATCH);
		String referenceNumber = wmsApiService.getReferenceCode(ReferenceNumberGenerationType.MISMATCHREPORT);
		report.setReferenceNumber(referenceNumber);
		List<MismatchReportData> dataList = new ArrayList<>();
		for (MismatchReportDataResponse response : list) {
			MismatchReportData mismatch = new MismatchReportData();
			mismatch.setDocDate(dateUtils.stringToDate(response.getDocDate(), DateFormates.DDMMYYYY_HYPHEN));
			mismatch.setInbCifValue(response.getInbTotalCifValue() != null ? response.getInbTotalCifValue() : 0);
			mismatch.setInbDeclarationNumber(response.getInbDeclarationNumber() != null ? response.getInbDeclarationNumber() : " ");
			mismatch.setInbDescription(response.getInbItemDescription() != null ? response.getInbItemDescription() : " ");
			mismatch.setInbHsCode(response.getInbHsCode() != null ? response.getInbHsCode() : " ");
			mismatch.setInbBarCode(response.getInbBarCode() != null ? response.getInbBarCode() : " ");
			mismatch.setInbQuantity(response.getInbTotalQuantity() != null ? response.getInbTotalQuantity() : 0);
			mismatch.setInbWeight(response.getInbTotalWeight() != null ? response.getInbTotalWeight() : 0);

			mismatch.setOutbCifValue(response.getOutbTotalCifValue() != null ? response.getOutbTotalCifValue() : 0);
			mismatch.setOutbInbDeclarationNumber(response.getOutbInbDeclarationNumber() != null ? response.getOutbInbDeclarationNumber() : " ");
			mismatch.setOutbDescription(response.getOutbItemDescription() != null ? response.getOutbItemDescription() : " ");
			mismatch.setOutbHsCode(response.getOutbHsCode() != null ? response.getOutbHsCode() : " ");
			mismatch.setOutbBarCode(response.getOutbBarCode() != null ? response.getOutbBarCode() : " ");
			mismatch.setOutbQuantity(response.getOutbTotalQuantity() != null ? response.getOutbTotalQuantity() : 0);
			mismatch.setOutbWeight(response.getOutbTotalWeight() != null ? response.getOutbTotalWeight() : 0);
			mismatch.setReport(report);
			dataList.add(mismatch);
		}
		report.setMismatchReportDatas(dataList);
		return reportRepository.save(report);
	}

	@SuppressWarnings("static-access")
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public FileResponse generateGenericReport(GenerateReportRequest vo) throws CustomException, IOException, JRException, Exception {
		Date fromDate = dateUtils.stringToDate(vo.getFromDate(), DateFormates.DDMMYYYY_HYPHEN);
		Date toDate = dateUtils.stringToDate(vo.getToDate(), DateFormates.DDMMYYYY_HYPHEN);

		if (fromDate == null || toDate == null)
			throw new CustomException(INVALID_DATE_FORMAT);
		
		Report existing = findByDateRangeOverlapAndBusinessCode(fromDate, toDate, vo.getBusinessCode(), ReportType.GENERIC);
		if (existing != null) {
			throw new CustomException(ReportType.GENERIC.getReportType() + " Report from " + dateUtils.dateToString(existing.getFromDate(), DateFormates.DDMMYYYY_HYPHEN) + " to "
					+ dateUtils.dateToString(existing.getToDate(), DateFormates.DDMMYYYY_HYPHEN) + " for Business code " + existing.getBusinessCode() + " already exist with Reference number "
					+ existing.getReferenceNumber());
		}
		
		Date fromDate2 = dateUtils.stringToDate(vo.getFromDate() + " 00:00:00", DateFormates.DDMMYYYYHHMMSS_HYPHEN);
		Date toDate2 = dateUtils.stringToDate(vo.getToDate() + " 23:59:59", DateFormates.DDMMYYYYHHMMSS_HYPHEN);
		
		if (fromDate2 == null || toDate2 == null)
			throw new CustomException(INVALID_DATE_FORMAT);

		List<GenericReportDataResponse> responses = reportRepository.generateGenericReport(fromDate2, toDate2, vo.getBusinessCode());
		if (responses != null && !responses.isEmpty()) {
			Report report = createGenericReport(responses, fromDate, toDate, vo.getBusinessCode(), vo.getConsigneeName());
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("reportFrom", vo.getFromDate());
			parameters.put("reportTo", vo.getToDate());
			parameters.put("consigneeName", vo.getConsigneeName());
			String prefix = "";
			byte[] bytes = null;
			if (vo.getFormat().equalsIgnoreCase(JPG) || vo.getFormat().equalsIgnoreCase(JPEG) || vo.getFormat().equalsIgnoreCase(PDF)) {
				InputStream instream = this.getClass().getResourceAsStream("/generic_report.jrxml");
				JasperReport jasperReport = JasperCompileManager.compileReport(instream);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(report.getGenericReportDatas()));
				if (vo.getFormat().equalsIgnoreCase(JPG) || vo.getFormat().equalsIgnoreCase(JPEG)) {
					JasperPrintManager printManager = JasperPrintManager.getInstance(DefaultJasperReportsContext.getInstance());
					BufferedImage rendered_image = (BufferedImage) printManager.printPageToImage(jasperPrint, 0, 1.6f);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					ImageIO.write(rendered_image, "jpeg", stream); // DO NOT USE JPEG CONSTANT HERE
					bytes = stream.toByteArray();
					prefix = BASE64PREFIXJPEG;
				} else if (vo.getFormat().equalsIgnoreCase(PDF)) {
					bytes = JasperExportManager.exportReportToPdf(jasperPrint);
					prefix = BASE64PREFIXPDF;
				}
			} else if (vo.getFormat().equalsIgnoreCase(MasterDataAppConstants.EXCEL)) {
				bytes = exportGenericReportToExcel(report);
				prefix = MasterDataAppConstants.BASE64XLSXMIMEPREFIX;
			}
			
			return new FileResponse(prefix + Base64.getEncoder().encodeToString(bytes), report.getReferenceNumber());
		} else
			throw new CustomException(MasterDataAppConstants.NO_DATA_TO_EXPORT);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	@Override
	public Report createGenericReport(List<GenericReportDataResponse> responses, Date fromDate, Date toDate, String businessCode, String consigneeName) throws Exception {
		Report report = new Report();
		report.setCreatedOn(new Date());
		report.setCreatedBy(" ");
		report.setFromDate(fromDate);
		report.setIsCancel(false);
		report.setToDate(toDate);
		report.setConsigneeName(consigneeName != null ? consigneeName : "");
		report.setBusinessCode(businessCode);
		report.setReportType(ReportType.GENERIC);
		String referenceNumber = wmsApiService.getReferenceCode(ReferenceNumberGenerationType.GENERICREPORT);
		report.setReferenceNumber(referenceNumber);
		List<GenericReportData> dataList = new ArrayList<GenericReportData>();
		HsCode hscode = null;
		for (GenericReportDataResponse response : responses) {
			hscode = hsCodeService.getByHsCodeId(response.getHsCodeId());
			GenericReportData generic = new GenericReportData();
			generic.setReport(report);
			generic.setInvoiceReferenceNumber(response.getInvoiceReferenceNumber());
			generic.setInvoiceCreatedDate(response.getInvoiceCreatedDate());
			generic.setInvoicePages(response.getInvoicePages());
			generic.setConsigneeName(response.getConsigneeName() != null ? response.getConsigneeName() : " ");
			generic.setCurrency(response.getCurrency() != null ? response.getCurrency() : " ");
			generic.setGoodsCondition(response.getGoodsCondition() != null ? response.getGoodsCondition() : " ");
			generic.setTotalQuantity(response.getTotalQuantity() != null ? response.getTotalQuantity() : 0);
			generic.setTotalWeight(response.getTotalWeight() != null ? response.getTotalWeight() : 0);
			generic.setGrandTotal(response.getGrandTotal() != null ? response.getGrandTotal() : 0);
			generic.setCifAmount(response.getCifValue() != null ? response.getCifValue() : 0);
			generic.setInbDeclarationNumber(response.getInbDeclarationNumber() != null ? response.getInbDeclarationNumber() : " ");
			if (hscode != null) {
				generic.setHsCode(hscode.getHsCode());
				generic.setHsCodeDescription(hscode.getHsCodeDescription());
				generic.setAuthorities(hscode.getAuthoritiyCodes());
			} else {
				generic.setHsCode(" ");
				generic.setHsCodeDescription(" ");
				generic.setAuthorities(" ");
			}
			dataList.add(generic);
		}
		report.setGenericReportDatas(dataList);
		return reportRepository.save(report);
	}

	@SuppressWarnings("static-access")
	@Override
	public FileResponse view(Report report, String format) throws IOException, JRException {
		if (report.getReportType() == ReportType.MISMATCH) {
			List<MismatchReportData> reportData = report.getMismatchReportDatas();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("reportFrom", dateUtils.dateToString(report.getFromDate(), DateFormates.DDMMYYYY_HYPHEN));
			parameters.put("reportTo", dateUtils.dateToString(report.getToDate(), DateFormates.DDMMYYYY_HYPHEN));
			parameters.put("consigneeName", report.getConsigneeName());
			String prefix = "";
			byte[] bytes = null;
			if (format.equalsIgnoreCase(JPG) || format.equalsIgnoreCase(JPEG) || format.equalsIgnoreCase(PDF)) {
				InputStream instream = this.getClass().getResourceAsStream("/mismatch_report.jrxml");
				JasperReport jasperReport = JasperCompileManager.compileReport(instream);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(reportData));
				if (format.equalsIgnoreCase(JPG) || format.equalsIgnoreCase(JPEG)) {
					JasperPrintManager printManager = JasperPrintManager.getInstance(DefaultJasperReportsContext.getInstance());
					BufferedImage rendered_image = (BufferedImage) printManager.printPageToImage(jasperPrint, 0, 1.6f);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					ImageIO.write(rendered_image, "jpeg", stream); // DO NOT USE JPEG CONSTANT HERE
					bytes = stream.toByteArray();
					prefix = BASE64PREFIXJPEG;
				} else if (format.equalsIgnoreCase(PDF)) {
					bytes = JasperExportManager.exportReportToPdf(jasperPrint);
					prefix = BASE64PREFIXPDF;
				}
			} else if (format.equalsIgnoreCase(MasterDataAppConstants.EXCEL)) {
				bytes = exportMismatchReportToExcel(report);
				prefix = MasterDataAppConstants.BASE64XLSXMIMEPREFIX;
			}
			return new FileResponse(prefix + Base64.getEncoder().encodeToString(bytes), report.getReferenceNumber());
		} else if (report.getReportType() == ReportType.GENERIC) {
			List<GenericReportData> reportData = report.getGenericReportDatas();
			String prefix = "";
			byte[] bytes = null;
			if (format.equalsIgnoreCase(JPG) || format.equalsIgnoreCase(JPEG) || format.equalsIgnoreCase(PDF)) {
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("reportFrom", dateUtils.dateToString(report.getFromDate(), DateFormates.DDMMYYYY_HYPHEN));
				parameters.put("reportTo", dateUtils.dateToString(report.getToDate(), DateFormates.DDMMYYYY_HYPHEN));
				parameters.put("consigneeName", report.getConsigneeName());
				InputStream instream = this.getClass().getResourceAsStream("/generic_report.jrxml");
				JasperReport jasperReport = JasperCompileManager.compileReport(instream);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(reportData));
				if (format.equalsIgnoreCase(JPG) || format.equalsIgnoreCase(JPEG)) {
					JasperPrintManager printManager = JasperPrintManager.getInstance(DefaultJasperReportsContext.getInstance());
					BufferedImage rendered_image = (BufferedImage) printManager.printPageToImage(jasperPrint, 0, 1.6f);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					ImageIO.write(rendered_image, "jpeg", stream); // DO NOT USE JPEG CONSTANT HERE
					bytes = stream.toByteArray();
					prefix = BASE64PREFIXJPEG;
				} else if (format.equalsIgnoreCase(PDF)) {
					bytes = JasperExportManager.exportReportToPdf(jasperPrint);
					prefix = BASE64PREFIXPDF;
				}
			} else if (format.equalsIgnoreCase(MasterDataAppConstants.EXCEL)) {
				bytes = exportGenericReportToExcel(report);
				prefix = MasterDataAppConstants.BASE64XLSXMIMEPREFIX;
			}
			
			return new FileResponse(prefix + Base64.getEncoder().encodeToString(bytes), report.getReferenceNumber());
		}
		return null;
	}

	@Override
	public void clearReport(ReportType reportType) {
		List<Report> list = reportRepository.findAllByReportType(reportType);
		if (reportType == ReportType.MISMATCH)
			mismatchReportDataRepository.deleteAll();
		if (reportType == ReportType.GENERIC)
			genericReportDataRepository.deleteAll();
		reportRepository.deleteAll(list);
	}

	private byte[] exportGenericReportToExcel(Report report) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Generic Report");
		int rownum = 0;
		int cellnum = 0;
		Row titleRow2 = sheet.createRow(rownum++);
		titleRow2.createCell(cellnum++).setCellValue("Invoice Detailed Report (Period - " + dateUtils.dateToString(report.getFromDate(), DateFormates.DDMMYYYY_HYPHEN) + " & "
				+ dateUtils.dateToString(report.getToDate(), DateFormates.DDMMYYYY_HYPHEN) + ") " + report.getConsigneeName());
		for (int i = 0; i < 15; i++)
			titleRow2.createCell(cellnum++).setCellValue("");

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 14));

		cellnum = 0;

		Row titleRow = sheet.createRow(rownum++);
		titleRow.createCell(cellnum++).setCellValue("TM Invoice Number");
		titleRow.createCell(cellnum++).setCellValue("TM Invoice Date");
		titleRow.createCell(cellnum++).setCellValue("Count Pages");
		titleRow.createCell(cellnum++).setCellValue("Company Name");
		titleRow.createCell(cellnum++).setCellValue("Currency");
		titleRow.createCell(cellnum++).setCellValue("Grand Total");
		titleRow.createCell(cellnum++).setCellValue("CIF");
		titleRow.createCell(cellnum++).setCellValue("HS Code");
		titleRow.createCell(cellnum++).setCellValue("Product Description");
		titleRow.createCell(cellnum++).setCellValue("Goods Condition");
		titleRow.createCell(cellnum++).setCellValue("Gross Quantity");
		titleRow.createCell(cellnum++).setCellValue("Gross Weight");
		titleRow.createCell(cellnum++).setCellValue("Inb. Declaration Number");
		titleRow.createCell(cellnum++).setCellValue("Permit Issuing Authority");

		for (GenericReportData data : report.getGenericReportDatas()) {
			cellnum = 0;
			Row row = sheet.createRow(rownum++);
			row.createCell(cellnum++).setCellValue(data.getInvoiceReferenceNumber());
			row.createCell(cellnum++).setCellValue(dateUtils.dateToString(data.getInvoiceCreatedDate(), DateFormates.DDMMYYYY_HYPHEN));
			row.createCell(cellnum++).setCellValue(data.getInvoicePages());
			row.createCell(cellnum++).setCellValue(data.getConsigneeName());
			row.createCell(cellnum++).setCellValue(data.getCurrency());
			row.createCell(cellnum++).setCellValue((data.getGrandTotal() != null && data.getGrandTotal() != 0) ? String.format("%.2f", data.getGrandTotal()) : "");
			row.createCell(cellnum++).setCellValue((data.getCifAmount() != null && data.getCifAmount() != 0) ? String.format("%.2f", data.getCifAmount()) : "");
			row.createCell(cellnum++).setCellValue(data.getHsCode());
			row.createCell(cellnum++).setCellValue(data.getHsCodeDescription());
			row.createCell(cellnum++).setCellValue(data.getGoodsCondition());
			row.createCell(cellnum++).setCellValue((data.getTotalQuantity() != null && data.getTotalQuantity() != 0) ? String.format("%.2f", data.getTotalQuantity()) : "");
			row.createCell(cellnum++).setCellValue((data.getTotalWeight() != null && data.getTotalWeight() != 0) ? String.format("%.2f", data.getTotalWeight()) : "");
			row.createCell(cellnum++).setCellValue(data.getInbDeclarationNumber());
			row.createCell(cellnum++).setCellValue(data.getAuthorities());
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		bos.close();
		return bos.toByteArray();
	}

	private byte[] exportMismatchReportToExcel(Report report) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Mismatch Report");
		int rownum = 0;
		int cellnum = 0;
		Row titleRow2 = sheet.createRow(rownum++);
		titleRow2.createCell(cellnum++).setCellValue("Inventory Movement Records (Period - " + dateUtils.dateToString(report.getFromDate(), DateFormates.DDMMYYYY_HYPHEN) + " & "
				+ dateUtils.dateToString(report.getToDate(), DateFormates.DDMMYYYY_HYPHEN) + ") " + report.getConsigneeName());

		for (int i = 0; i < 15; i++)
			titleRow2.createCell(cellnum++).setCellValue("");

		cellnum = 0;
		Row titleRow1 = sheet.createRow(rownum++);
		titleRow1.createCell(cellnum++).setCellValue("Inbound");
		for (int i = 0; i < 7; i++)
			titleRow1.createCell(cellnum++).setCellValue("");
		titleRow1.createCell(cellnum++).setCellValue("Outbound");
		for (int i = 0; i < 6; i++)
			titleRow1.createCell(cellnum++).setCellValue("");

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 14));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 8, 14));

		Row titleRow = sheet.createRow(rownum++);
		cellnum = 0;
		titleRow.createCell(cellnum++).setCellValue("Item Code");
		titleRow.createCell(cellnum++).setCellValue("Description");
		titleRow.createCell(cellnum++).setCellValue("Ref.#(Trans.in/BOE./ FZ.Transfares)");
		titleRow.createCell(cellnum++).setCellValue("Doc Date");
		titleRow.createCell(cellnum++).setCellValue("HSC");
		titleRow.createCell(cellnum++).setCellValue("Quantity");
		titleRow.createCell(cellnum++).setCellValue("G.weight");
		titleRow.createCell(cellnum++).setCellValue("CIF Value");
		titleRow.createCell(cellnum++).setCellValue("Ref.#(Trans.out/Imort Dec./ FZ.Transfares)");
		titleRow.createCell(cellnum++).setCellValue("HSC");
		titleRow.createCell(cellnum++).setCellValue("Item Code");
		titleRow.createCell(cellnum++).setCellValue("Description");
		titleRow.createCell(cellnum++).setCellValue("Quantity");
		titleRow.createCell(cellnum++).setCellValue("G.weight");
		titleRow.createCell(cellnum++).setCellValue("CIF Value");

		for (MismatchReportData data : report.getMismatchReportDatas()) {
			cellnum = 0;
			Row row = sheet.createRow(rownum++);
			row.createCell(cellnum++).setCellValue(data.getInbBarCode());
			row.createCell(cellnum++).setCellValue(data.getInbDescription());
			row.createCell(cellnum++).setCellValue(data.getInbDeclarationNumber());
			row.createCell(cellnum++).setCellValue(dateUtils.dateToString(data.getDocDate(), DateFormates.DDMMYYYY_HYPHEN));
			row.createCell(cellnum++).setCellValue(data.getInbHsCode());
			row.createCell(cellnum++).setCellValue((data.getInbQuantity() != null && data.getInbQuantity() != 0) ? String.format("%.2f", data.getInbQuantity()) : "");
			row.createCell(cellnum++).setCellValue((data.getInbWeight() != null && data.getInbWeight() != 0) ? String.format("%.2f", data.getInbWeight()) : "");
			row.createCell(cellnum++).setCellValue((data.getInbCifValue() != null && data.getInbCifValue() != 0) ? String.format("%.2f", data.getInbCifValue()) : "");
			row.createCell(cellnum++).setCellValue(data.getOutbInbDeclarationNumber());
			row.createCell(cellnum++).setCellValue(data.getOutbHsCode());
			row.createCell(cellnum++).setCellValue(data.getOutbBarCode());
			row.createCell(cellnum++).setCellValue(data.getOutbDescription());
			row.createCell(cellnum++).setCellValue((data.getOutbQuantity() != null && data.getOutbQuantity() != 0) ? String.format("%.2f", data.getOutbQuantity()) : "");
			row.createCell(cellnum++).setCellValue((data.getOutbWeight() != null && data.getOutbWeight() != 0) ? String.format("%.2f", data.getOutbWeight()) : "");
			row.createCell(cellnum++).setCellValue((data.getOutbCifValue() != null && data.getOutbCifValue() != 0) ? String.format("%.2f", data.getOutbCifValue()) : "");
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		bos.close();
		return bos.toByteArray();
	}

}

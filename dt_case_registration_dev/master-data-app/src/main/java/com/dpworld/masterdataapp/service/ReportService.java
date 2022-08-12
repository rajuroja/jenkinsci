package com.dpworld.masterdataapp.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.dpworld.common.utils.CustomException;
import com.dpworld.masterdataapp.model.request.GenerateReportRequest;
import com.dpworld.masterdataapp.model.response.FileResponse;
import com.dpworld.persistence.entity.Report;
import com.dpworld.persistence.entity.ReportType;
import com.dpworld.persistence.vo.GenericReportDataResponse;
import com.dpworld.persistence.vo.MismatchReportDataResponse;
import com.dpworld.persistence.vo.ReportListResponse;

import net.sf.jasperreports.engine.JRException;

public interface ReportService {

	Report findByReportId(Long reportId);

	List<Report> findAll();

	List<ReportListResponse> list();

	List<ReportListResponse> listByBusinessCodes(List<String> agentCodes);

	Report findByDateRangeOverlapAndBusinessCode(Date fromDate, Date toDate, String businessCode, ReportType reportType);

	StringBuilder validateGenerateReportRequest(GenerateReportRequest vo);

	StringBuilder validateViewReportRequest(GenerateReportRequest vo);

	FileResponse generateMismatchReport(GenerateReportRequest vo) throws CustomException, IOException, JRException, Exception;

	FileResponse generateGenericReport(GenerateReportRequest vo) throws CustomException, IOException, JRException, Exception;

	FileResponse view(Report report, String format) throws IOException, JRException;

	void clearReport(ReportType valueOf);

	Report createMismatchReport(List<MismatchReportDataResponse> list, Date fromDate, Date toDate, String businessCode, String consigneeName) throws Exception;

	Report createGenericReport(List<GenericReportDataResponse> responses, Date fromDate, Date toDate, String businessCode, String consigneeName) throws Exception;

}

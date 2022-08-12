package com.dpworld.persistence.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity @OptimisticLocking(type = OptimisticLockType.ALL) @DynamicUpdate
//@Table(name = "REPORT", indexes = @Index(columnList = "BUSINESS_CODE"))
@Table(name = "REPORT")
public class Report extends ECommonFields implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "REPORT_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "seq_report")
	@SequenceGenerator(name = "seq_report", sequenceName = "seq_report")
	private long reportId;

	@Column(name = "FROM_DATE")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date fromDate;

	@Column(name = "TO_DATE")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date toDate;

	@Column(name = "REFERENCE_NUMBER", length = 20)
	private String referenceNumber;

	@JsonBackReference
	@OneToMany(mappedBy = "report", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private List<MismatchReportData> mismatchReportDatas;

	@JsonBackReference
	@OneToMany(mappedBy = "report", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private List<GenericReportData> genericReportDatas;

	@Enumerated(EnumType.STRING)
	@Column(name = "REPORT_TYPE")
	private ReportType reportType;

	//@Column(name= "BUSINESS_CODE")
	private String businessCode;

	//@Column(name= "CONSIGNEE_NAME")
	private String consigneeName;
	
	public long getReportId() {
		return reportId;
	}

	public void setReportId(long reportId) {
		this.reportId = reportId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public List<MismatchReportData> getMismatchReportDatas() {
		return mismatchReportDatas;
	}

	public void setMismatchReportDatas(List<MismatchReportData> mismatchReportDatas) {
		this.mismatchReportDatas = mismatchReportDatas;
	}

	public ReportType getReportType() {
		return reportType;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public List<GenericReportData> getGenericReportDatas() {
		return genericReportDatas;
	}

	public void setGenericReportDatas(List<GenericReportData> genericReportDatas) {
		this.genericReportDatas = genericReportDatas;
	}

	@Override
	public String toString() {
		return "Report [reportId=" + reportId + ", fromDate=" + fromDate + ", toDate=" + toDate + ", referenceNumber=" + referenceNumber + ", mismatchReportDatas=" + mismatchReportDatas
				+ ", genericReportDatas=" + genericReportDatas + ", reportType=" + reportType + ", businessCode=" + businessCode + ", consigneeName=" + consigneeName + "]";
	}

}

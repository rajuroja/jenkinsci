package com.dpworld.persistence.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.Type;

import com.dpworld.persistence.vo.PushApiResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity @OptimisticLocking(type = OptimisticLockType.ALL) @DynamicUpdate
@Table(name = "OUTBOUND", indexes = @Index(columnList = "REFERENCE_NUMBER"))
public class Outbound extends ECommonFields {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "OUTBOUND_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_outbound")
	@SequenceGenerator(name = "seq_outbound", sequenceName = "seq_outbound", allocationSize = 1)
	private long outboundId;

	@Column(name = "REFERENCE_NUMBER", length = 20)
	private String referenceNumber;

	@Column(name = "COMPANY_CODE")
	private String companyCode; // Pending

//	@Column(name = "RETURN_OUTBOUND_REF_ID") //Default
//	private long returnOutboundRefId;

	@Type(type = "true_false")
	@Column(name = "PUSHED_TO_OCI")
	protected Boolean pushedToOci;

	@JsonIgnore
	@JsonManagedReference
	@OneToOne(mappedBy = "outbound", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private OutbShipmentDepartureInformation outbShipmentDepartureInformation;

	@JsonIgnore
	@JsonManagedReference
	@OneToOne(mappedBy = "outbound", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private OutbDeclarationInformation outbDeclarationInformation;

	@JsonIgnore
	@JsonManagedReference
	@OneToMany(mappedBy = "outbound", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<OutbCargoInformation> outbCargosInformation;

	@JsonIgnore
	@JsonManagedReference
	@OneToMany(mappedBy = "outbound", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<OutbUploadDocuments> outbUploadDocuments;

	@ManyToOne
	@JoinColumn(name = "INVOICE_ID")
	private InvoiceDetail invoiceDetail;

	@Transient
	private PushApiResponse pushApiResponse;

	public InvoiceDetail getInvoiceDetail() {
		return invoiceDetail;
	}

	public void setInvoiceDetail(InvoiceDetail invoiceDetail) {
		this.invoiceDetail = invoiceDetail;
	}

	public long getOutboundId() {
		return outboundId;
	}

	public void setOutboundId(long outboundId) {
		this.outboundId = outboundId;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public OutbShipmentDepartureInformation getOutbShipmentDepartureInformation() {
		return outbShipmentDepartureInformation;
	}

	public void setOutbShipmentDepartureInformation(OutbShipmentDepartureInformation outbShipmentDepartureInformation) {
		this.outbShipmentDepartureInformation = outbShipmentDepartureInformation;
	}

	public OutbDeclarationInformation getOutbDeclarationInformation() {
		return outbDeclarationInformation;
	}

	public void setOutbDeclarationInformation(OutbDeclarationInformation outbDeclarationInformation) {
		this.outbDeclarationInformation = outbDeclarationInformation;
	}

	public List<OutbCargoInformation> getOutbCargosInformation() {
		return outbCargosInformation;
	}

	public void setOutbCargosInformation(List<OutbCargoInformation> outbCargosInformation) {
		this.outbCargosInformation = outbCargosInformation;
	}

	public List<OutbUploadDocuments> getOutbUploadDocuments() {
		return outbUploadDocuments;
	}

	public void setOutbUploadDocuments(List<OutbUploadDocuments> outbUploadDocuments) {
		this.outbUploadDocuments = outbUploadDocuments;
	}

	public Boolean getPushedToOci() {
		return pushedToOci;
	}

	public void setPushedToOci(Boolean pushedToOci) {
		this.pushedToOci = pushedToOci;
	}

	public String getDisplayShipmentSoldType() {
		if (outbShipmentDepartureInformation != null)
			return outbShipmentDepartureInformation.getShipmentSoldType().getShipmentSoldType();
		return "";
	}

	public PushApiResponse getPushApiResponse() {
		return pushApiResponse;
	}

	public void setPushApiResponse(PushApiResponse pushApiResponse) {
		this.pushApiResponse = pushApiResponse;
	}

	@Override
	public String toString() {
		return "Outbound [outboundId=" + outboundId + ", referenceNumber=" + referenceNumber + ", companyCode=" + companyCode + ", pushedToOci=" + pushedToOci + ", outbShipmentDepartureInformation="
				+ outbShipmentDepartureInformation + ", outbDeclarationInformation=" + outbDeclarationInformation + ", outbCargosInformation=" + outbCargosInformation + ", outbUploadDocuments="
				+ outbUploadDocuments + ", invoiceDetail=" + invoiceDetail + "]";
	}

}

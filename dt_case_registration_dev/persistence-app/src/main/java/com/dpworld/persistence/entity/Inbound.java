package com.dpworld.persistence.entity;

import java.io.Serializable;
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
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.dpworld.persistence.vo.PushApiResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.hibernate.annotations.DynamicUpdate;

import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

@Entity @OptimisticLocking(type = OptimisticLockType.ALL) @DynamicUpdate
@Table(name = "INBOUND", indexes = @Index(columnList = "REFERENCE_NUMBER"))
public class Inbound extends ECommonFields implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "INBOUND_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_inbound")
	@SequenceGenerator(name = "seq_inbound", sequenceName = "seq_inbound", allocationSize = 1)
	private long inboundId;

	@Column(name = "REFERENCE_NUMBER", length = 20)
	private String referenceNumber;

	@Column(name = "COMPANY_CODE")
	private String companyCode; // pending

	@Column(name = "RETURN_INBOUND_REF_ID") // Default
	private long returnInboundRefId;

	@Enumerated(EnumType.STRING)
	private ReturnType returnType;

	@JsonIgnore
	@JsonManagedReference
	@OneToOne(mappedBy = "inbound", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private InbShipmentArrivalInformation inbShipmentArrivalInformation;

	@JsonIgnore
	@JsonManagedReference
	@OneToOne(mappedBy = "inbound", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private InbDeclarationInformation inbDeclarationInformation;

	@JsonIgnore
	@JsonManagedReference
	@OneToMany(mappedBy = "inbound", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<InbCargoInformation> inbCargosInformation;

	@JsonIgnore
	@JsonManagedReference
	@OneToMany(mappedBy = "inbound", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<InbUploadDocuments> inbUploadDocuments;

	@Transient
	private PushApiResponse pushApiResponse;

	public long getInboundId() {
		return inboundId;
	}

	public void setInboundId(long inboundId) {
		this.inboundId = inboundId;
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

	public long getReturnInboundRefId() {
		return returnInboundRefId;
	}

	public void setReturnInboundRefId(long returnInboundRefId) {
		this.returnInboundRefId = returnInboundRefId;
	}

	public InbShipmentArrivalInformation getInbShipmentArrivalInformation() {
		return inbShipmentArrivalInformation;
	}

	public void setInbShipmentArrivalInformation(InbShipmentArrivalInformation inbShipmentArrivalInformation) {
		this.inbShipmentArrivalInformation = inbShipmentArrivalInformation;
	}

	public InbDeclarationInformation getInbDeclarationInformation() {
		return inbDeclarationInformation;
	}

	public void setInbDeclarationInformation(InbDeclarationInformation inbDeclarationInformation) {
		this.inbDeclarationInformation = inbDeclarationInformation;
	}

	public List<InbCargoInformation> getInbCargosInformation() {
		return inbCargosInformation;
	}

	public void setInbCargosInformation(List<InbCargoInformation> inbCargosInformation) {
		this.inbCargosInformation = inbCargosInformation;
	}

	public List<InbUploadDocuments> getInbUploadDocuments() {
		return inbUploadDocuments;
	}

	public void setInbUploadDocuments(List<InbUploadDocuments> inbUploadDocuments) {
		this.inbUploadDocuments = inbUploadDocuments;
	}

	public ReturnType getReturnType() {
		return returnType;
	}

	public void setReturnType(ReturnType returnType) {
		this.returnType = returnType;
	}

	public Inbound(long inboundId) {
		super();
		this.inboundId = inboundId;
	}

	public Inbound() {
		super();
	}

	public PushApiResponse getPushApiResponse() {
		return pushApiResponse;
	}

	public void setPushApiResponse(PushApiResponse pushApiResponse) {
		this.pushApiResponse = pushApiResponse;
	}

	@Override
	public String toString() {
		return "Inbound [inboundId=" + inboundId + ", referenceNumber=" + referenceNumber + ", companyCode=" + companyCode + ", returnInboundRefId=" + returnInboundRefId + ", returnType=" + returnType
				+ ", inbShipmentArrivalInformation=" + inbShipmentArrivalInformation + ", inbDeclarationInformation=" + inbDeclarationInformation + ", inbCargosInformation=" + inbCargosInformation
				+ ", inbUploadDocuments=" + inbUploadDocuments + ", pushApiResponse=" + pushApiResponse + "]";
	}

}

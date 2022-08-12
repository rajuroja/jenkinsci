package com.dpworld.masterdataapp.model.response;

import java.util.List;

import com.dpworld.persistence.vo.PushApiResponse;

public class InboundResponse {

	private Long inboundId;

	private String referenceNumber;

	// private String companyCode; // pending

	private Long returnInboundRefId;

	private String returnType;

	private InbShipmentArrivalInformationResponse inbShipmentArrivalInformation;

	private InbDeclarationInformationResponse inbDeclarationInformation;

	private List<InbCargoInformationResponse> inbCargosInformation;

	private List<InbUploadDocumentResponse> inbUploadDocuments;

	private PushApiResponse pushApiResponse;

	public Long getInboundId() {
		return inboundId;
	}

	public void setInboundId(Long inboundId) {
		this.inboundId = inboundId;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public Long getReturnInboundRefId() {
		return returnInboundRefId;
	}

	public void setReturnInboundRefId(Long returnInboundRefId) {
		this.returnInboundRefId = returnInboundRefId;
	}

	public InbShipmentArrivalInformationResponse getInbShipmentArrivalInformation() {
		return inbShipmentArrivalInformation;
	}

	public void setInbShipmentArrivalInformation(InbShipmentArrivalInformationResponse inbShipmentArrivalInformation) {
		this.inbShipmentArrivalInformation = inbShipmentArrivalInformation;
	}

	public InbDeclarationInformationResponse getInbDeclarationInformation() {
		return inbDeclarationInformation;
	}

	public void setInbDeclarationInformation(InbDeclarationInformationResponse inbDeclarationInformation) {
		this.inbDeclarationInformation = inbDeclarationInformation;
	}

	public List<InbCargoInformationResponse> getInbCargosInformation() {
		return inbCargosInformation;
	}

	public void setInbCargosInformation(List<InbCargoInformationResponse> inbCargosInformation) {
		this.inbCargosInformation = inbCargosInformation;
	}

	public List<InbUploadDocumentResponse> getInbUploadDocuments() {
		return inbUploadDocuments;
	}

	public void setInbUploadDocuments(List<InbUploadDocumentResponse> inbUploadDocuments) {
		this.inbUploadDocuments = inbUploadDocuments;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public PushApiResponse getPushApiResponse() {
		return pushApiResponse;
	}

	public void setPushApiResponse(PushApiResponse pushApiResponse) {
		this.pushApiResponse = pushApiResponse;
	}

}

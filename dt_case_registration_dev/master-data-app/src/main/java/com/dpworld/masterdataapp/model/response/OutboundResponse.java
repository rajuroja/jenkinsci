package com.dpworld.masterdataapp.model.response;

import java.util.List;

import com.dpworld.persistence.vo.PushApiResponse;

public class OutboundResponse {

	private Long outboundId;

	private String referenceNumber;

	// private String companyCode; // pending

	private OutbShipmentDepartureInformationResponse outbShipmentDepartureInformation;

	private OutbDeclarationInformationResponse outbDeclarationInformation;

	private List<OutbCargoInformationResponse> outbCargosInformation;

	private List<OutbUploadDocumentResponse> outbUploadDocuments;

	private PushApiResponse pushApiResponse;

	public Long getOutboundId() {
		return outboundId;
	}

	public void setOutboundId(Long outboundId) {
		this.outboundId = outboundId;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public OutbShipmentDepartureInformationResponse getOutbShipmentDepartureInformation() {
		return outbShipmentDepartureInformation;
	}

	public void setOutbShipmentDepartureInformation(OutbShipmentDepartureInformationResponse outbShipmentDepartureInformation) {
		this.outbShipmentDepartureInformation = outbShipmentDepartureInformation;
	}

	public OutbDeclarationInformationResponse getOutbDeclarationInformation() {
		return outbDeclarationInformation;
	}

	public void setOutbDeclarationInformation(OutbDeclarationInformationResponse outbDeclarationInformation) {
		this.outbDeclarationInformation = outbDeclarationInformation;
	}

	public List<OutbCargoInformationResponse> getOutbCargosInformation() {
		return outbCargosInformation;
	}

	public void setOutbCargosInformation(List<OutbCargoInformationResponse> outbCargosInformation) {
		this.outbCargosInformation = outbCargosInformation;
	}

	public List<OutbUploadDocumentResponse> getOutbUploadDocuments() {
		return outbUploadDocuments;
	}

	public void setOutbUploadDocuments(List<OutbUploadDocumentResponse> outbUploadDocuments) {
		this.outbUploadDocuments = outbUploadDocuments;
	}

	public PushApiResponse getPushApiResponse() {
		return pushApiResponse;
	}

	public void setPushApiResponse(PushApiResponse pushApiResponse) {
		this.pushApiResponse = pushApiResponse;
	}

}
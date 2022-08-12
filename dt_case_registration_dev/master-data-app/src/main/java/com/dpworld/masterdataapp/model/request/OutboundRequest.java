package com.dpworld.masterdataapp.model.request;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OutboundRequest {

	private long outboundId;

	private OutbShipmentDepartureInformationRequest outbShipmentDepartureInformation;

	private OutbDeclarationInformationRequest outbDeclarationInformation;

	private List<OutbCargoInformationRequest> outbCargosInformation;

	private List<OutbUploadDocumentRequest> uploadDocuments;

	private List<Long> deletedOutbCargoInformations;

	private List<Long> deletedOutbBarCodeDetails;

	private List<Long> deletedCustomDeclarationDocuments;

	private List<Long> deletedPackingListDocuments;

	private List<Long> deletedSalesInvoiceDocuments;

	public long getOutboundId() {
		return outboundId;
	}

	public void setOutboundId(long outboundId) {
		this.outboundId = outboundId;
	}

	public OutbShipmentDepartureInformationRequest getOutbShipmentDepartureInformation() {
		return outbShipmentDepartureInformation;
	}

	public void setOutbShipmentDepartureInformation(OutbShipmentDepartureInformationRequest outbShipmentDepartureInformation) {
		this.outbShipmentDepartureInformation = outbShipmentDepartureInformation;
	}

	public OutbDeclarationInformationRequest getOutbDeclarationInformation() {
		return outbDeclarationInformation;
	}

	public void setOutbDeclarationInformation(OutbDeclarationInformationRequest outbDeclarationInformation) {
		this.outbDeclarationInformation = outbDeclarationInformation;
	}

	public List<OutbCargoInformationRequest> getOutbCargosInformation() {
		return outbCargosInformation;
	}

	public void setOutbCargosInformation(List<OutbCargoInformationRequest> outbCargosInformation) {
		this.outbCargosInformation = outbCargosInformation;
	}

	public List<Long> getDeletedOutbCargoInformations() {
		if(deletedOutbCargoInformations == null)
			return new ArrayList<>();
		return deletedOutbCargoInformations;
	}

	public void setDeletedOutbCargoInformations(List<Long> deletedOutbCargoInformations) {
		this.deletedOutbCargoInformations = deletedOutbCargoInformations;
	}

	public List<Long> getDeletedOutbBarCodeDetails() {
		if(deletedOutbBarCodeDetails == null)
			return new ArrayList<>();
		return deletedOutbBarCodeDetails;
	}

	public void setDeletedOutbBarCodeDetails(List<Long> deletedOutbBarCodeDetails) {
		this.deletedOutbBarCodeDetails = deletedOutbBarCodeDetails;
	}

	public List<OutbUploadDocumentRequest> getUploadDocuments() {
		return uploadDocuments;
	}

	public void setUploadDocuments(List<OutbUploadDocumentRequest> uploadDocuments) {
		this.uploadDocuments = uploadDocuments;
	}

	public List<Long> getDeletedCustomDeclarationDocuments() {
		if(deletedCustomDeclarationDocuments == null)
			return new ArrayList<>();
		return deletedCustomDeclarationDocuments;
	}

	public void setDeletedCustomDeclarationDocuments(List<Long> deletedCustomDeclarationDocuments) {
		this.deletedCustomDeclarationDocuments = deletedCustomDeclarationDocuments;
	}

	public List<Long> getDeletedPackingListDocuments() {
		if(deletedPackingListDocuments == null)
			return new ArrayList<>();
		return deletedPackingListDocuments;
	}

	public void setDeletedPackingListDocuments(List<Long> deletedPackingListDocuments) {
		this.deletedPackingListDocuments = deletedPackingListDocuments;
	}

	public List<Long> getDeletedSalesInvoiceDocuments() {
		if(deletedSalesInvoiceDocuments == null)
			return new ArrayList<>();
		return deletedSalesInvoiceDocuments;
	}

	public void setDeletedSalesInvoiceDocuments(List<Long> deletedSalesInvoiceDocuments) {
		this.deletedSalesInvoiceDocuments = deletedSalesInvoiceDocuments;
	}

}

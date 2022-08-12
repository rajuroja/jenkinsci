package com.dpworld.masterdataapp.model.request;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InboundRequest {

	private long inboundId;

	private InbShipmentArrivalInformationRequest inbShipmentArrivalInformation;

	private InbDeclarationInformationRequest inbDeclarationInformation;

	private List<InbCargoInformationRequest> inbCargosInformation;

	private List<InbUploadDocumentRequest> inbUploadDocs;

	private String returnType;

	private List<Long> deletedInbCargoInformations;

	private List<Long> deletedInbBarCodeDetails;

	private List<Long> deletedCustomDeclarationDocuments;

	private List<Long> deletedCertificateOfOriginDocuments;

	private List<Long> deletedCommercialInvoiceDocuments;

	private List<Long> deletedPackingListDocuments;
	
	// do not auto generate getter setter.   example ==> getDeletedInbCargoInformations()
	
	public long getInboundId() {
		return inboundId;
	}

	public void setInboundId(long inboundId) {
		this.inboundId = inboundId;
	}

	public InbShipmentArrivalInformationRequest getInbShipmentArrivalInformation() {
		return inbShipmentArrivalInformation;
	}

	public void setInbShipmentArrivalInformation(InbShipmentArrivalInformationRequest inbShipmentArrivalInformation) {
		this.inbShipmentArrivalInformation = inbShipmentArrivalInformation;
	}

	public InbDeclarationInformationRequest getInbDeclarationInformation() {
		return inbDeclarationInformation;
	}

	public void setInbDeclarationInformation(InbDeclarationInformationRequest inbDeclarationInformation) {
		this.inbDeclarationInformation = inbDeclarationInformation;
	}

	public List<InbCargoInformationRequest> getInbCargosInformation() {
		return inbCargosInformation;
	}

	public void setInbCargosInformation(List<InbCargoInformationRequest> inbCargosInformation) {
		this.inbCargosInformation = inbCargosInformation;
	}

	public List<InbUploadDocumentRequest> getInbUploadDocs() {
		return inbUploadDocs;
	}

	public void setInbUploadDocs(List<InbUploadDocumentRequest> inbUploadDocs) {
		this.inbUploadDocs = inbUploadDocs;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public List<Long> getDeletedInbCargoInformations() {
		if (deletedInbCargoInformations == null)
			return new ArrayList<Long>();
		return deletedInbCargoInformations;
	}

	public void setDeletedInbCargoInformations(List<Long> deletedInbCargoInformations) {
		this.deletedInbCargoInformations = deletedInbCargoInformations;
	}

	public List<Long> getDeletedInbBarCodeDetails() {
		if (deletedInbBarCodeDetails == null)
			return new ArrayList<Long>();
		return deletedInbBarCodeDetails;
	}

	public void setDeletedInbBarCodeDetails(List<Long> deletedInbBarCodeDetails) {
		this.deletedInbBarCodeDetails = deletedInbBarCodeDetails;
	}

	public List<Long> getDeletedCustomDeclarationDocuments() {
		if(deletedCustomDeclarationDocuments == null)
			return new ArrayList<>();
		return deletedCustomDeclarationDocuments;
	}

	public void setDeletedCustomDeclarationDocuments(List<Long> deletedCustomDeclarationDocuments) {
		this.deletedCustomDeclarationDocuments = deletedCustomDeclarationDocuments;
	}

	public List<Long> getDeletedCertificateOfOriginDocuments() {
		if(deletedCertificateOfOriginDocuments == null)
			return new ArrayList<>();
		return deletedCertificateOfOriginDocuments;
	}

	public void setDeletedCertificateOfOriginDocuments(List<Long> deletedCertificateOfOriginDocuments) {
		this.deletedCertificateOfOriginDocuments = deletedCertificateOfOriginDocuments;
	}

	public List<Long> getDeletedCommercialInvoiceDocuments() {
		if(deletedCommercialInvoiceDocuments == null)
			return new ArrayList<>();
		return deletedCommercialInvoiceDocuments;
	}

	public void setDeletedCommercialInvoiceDocuments(List<Long> deletedCommercialInvoiceDocuments) {
		this.deletedCommercialInvoiceDocuments = deletedCommercialInvoiceDocuments;
	}

	public List<Long> getDeletedPackingListDocuments() {
		if(deletedPackingListDocuments == null)
			return new ArrayList<>();
		return deletedPackingListDocuments;
	}

	public void setDeletedPackingListDocuments(List<Long> deletedPackingListDocuments) {
		this.deletedPackingListDocuments = deletedPackingListDocuments;
	}

	@Override
	public String toString() {
		return "InboundRequest [inboundId=" + inboundId + ", inbShipmentArrivalInformation=" + inbShipmentArrivalInformation + ", inbDeclarationInformation=" + inbDeclarationInformation
				+ ", inbCargosInformation=" + inbCargosInformation + ", inbUploadDocs=" + inbUploadDocs + ", returnType=" + returnType + ", deletedInbCargoInformations=" + deletedInbCargoInformations
				+ ", deletedInbBarCodeDetails=" + deletedInbBarCodeDetails + ", deletedCustomDeclarationDocuments=" + deletedCustomDeclarationDocuments + ", deletedCertificateOfOriginDocuments="
				+ deletedCertificateOfOriginDocuments + ", deletedCommercialInvoiceDocuments=" + deletedCommercialInvoiceDocuments + ", deletedPackingListDocuments=" + deletedPackingListDocuments
				+ "]";
	}

}

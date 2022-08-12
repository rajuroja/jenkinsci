package com.dpworld.masterdataapp.model.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OutbCargoInformationRequest {

	private Long id;

	private Long hsCodeId;

	private Integer manufacturerCountryId;

	private Byte goodsConditionId;

	private Double dutyValue;

	private Boolean pallet;

	private Double palletQuantity;

	private Double palletNetWeight;

	private Boolean carton;

	private Double cartonQuantity;

	private Double cartonNetWeight;

	private Boolean pieces;

	private Double piecesQuantity;

	private Double piecesNetWeight;

	private String packageDescription;

	private OutbInvoiceDetailsRequest outbInvoiceDetail;

	private List<OutbBarCodeDetailsRequest> outbBarcodeDetails;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getHsCodeId() {
		return hsCodeId;
	}

	public void setHsCodeId(Long hsCodeId) {
		this.hsCodeId = hsCodeId;
	}

	public Integer getManufacturerCountryId() {
		return manufacturerCountryId;
	}

	public void setManufacturerCountryId(Integer manufacturerCountryId) {
		this.manufacturerCountryId = manufacturerCountryId;
	}

	public Byte getGoodsConditionId() {
		return goodsConditionId;
	}

	public void setGoodsConditionId(Byte goodsConditionId) {
		this.goodsConditionId = goodsConditionId;
	}

	public Double getDutyValue() {
		return dutyValue;
	}

	public void setDutyValue(Double dutyValue) {
		this.dutyValue = dutyValue;
	}

	public Boolean getPallet() {
		return pallet;
	}

	public void setPallet(Boolean pallet) {
		this.pallet = pallet;
	}

	public Double getPalletQuantity() {
		return palletQuantity;
	}

	public void setPalletQuantity(Double palletQuantity) {
		this.palletQuantity = palletQuantity;
	}

	public Double getPalletNetWeight() {
		return palletNetWeight;
	}

	public void setPalletNetWeight(Double palletNetWeight) {
		this.palletNetWeight = palletNetWeight;
	}

	public Boolean getCarton() {
		return carton;
	}

	public void setCarton(Boolean carton) {
		this.carton = carton;
	}

	public Double getCartonQuantity() {
		return cartonQuantity;
	}

	public void setCartonQuantity(Double cartonQuantity) {
		this.cartonQuantity = cartonQuantity;
	}

	public Double getCartonNetWeight() {
		return cartonNetWeight;
	}

	public void setCartonNetWeight(Double cartonNetWeight) {
		this.cartonNetWeight = cartonNetWeight;
	}

	public Boolean getPieces() {
		return pieces;
	}

	public void setPieces(Boolean pieces) {
		this.pieces = pieces;
	}

	public Double getPiecesQuantity() {
		return piecesQuantity;
	}

	public void setPiecesQuantity(Double piecesQuantity) {
		this.piecesQuantity = piecesQuantity;
	}

	public Double getPiecesNetWeight() {
		return piecesNetWeight;
	}

	public void setPiecesNetWeight(Double piecesNetWeight) {
		this.piecesNetWeight = piecesNetWeight;
	}

	public String getPackageDescription() {
		return packageDescription;
	}

	public void setPackageDescription(String packageDescription) {
		this.packageDescription = packageDescription;
	}

	public OutbInvoiceDetailsRequest getOutbInvoiceDetail() {
		return outbInvoiceDetail;
	}

	public void setOutbInvoiceDetail(OutbInvoiceDetailsRequest outbInvoiceDetail) {
		this.outbInvoiceDetail = outbInvoiceDetail;
	}

	public List<OutbBarCodeDetailsRequest> getOutbBarcodeDetails() {
		return outbBarcodeDetails;
	}

	public void setOutbBarcodeDetails(List<OutbBarCodeDetailsRequest> outbBarcodeDetails) {
		this.outbBarcodeDetails = outbBarcodeDetails;
	}

	@Override
	public String toString() {
		return "OutbCargoInformationRequest [id=" + id + ", hsCodeId=" + hsCodeId + ", manufacturerCountryId=" + manufacturerCountryId + ", goodsConditionId=" + goodsConditionId + ", dutyValue="
				+ dutyValue + ", pallet=" + pallet + ", palletQuantity=" + palletQuantity + ", palletNetWeight=" + palletNetWeight + ", carton=" + carton + ", cartonQuantity=" + cartonQuantity
				+ ", cartonNetWeight=" + cartonNetWeight + ", pieces=" + pieces + ", piecesQuantity=" + piecesQuantity + ", piecesNetWeight=" + piecesNetWeight + ", packageDescription="
				+ packageDescription + ", outbInvoiceDetail=" + outbInvoiceDetail + ", outbBarcodeDetails=" + outbBarcodeDetails + "]";
	}

}

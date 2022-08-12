package com.dpworld.masterdataapp.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OutbCargoInformationResponse {

	private Long id;

	private HsCodeResponse hsCodeId;

	private CountryDetailsResponse manufacturerCountryId;

	private GoodsConditionResponse goodsConditionId;

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

	private OutbInvoiceDetailsResponse outbInvoiceDetail;

	private List<OutbBarCodeDetailResponse> outbBarcodeDetails;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public HsCodeResponse getHsCodeId() {
		return hsCodeId;
	}

	public void setHsCodeId(HsCodeResponse hsCodeId) {
		this.hsCodeId = hsCodeId;
	}

	public CountryDetailsResponse getManufacturerCountryId() {
		return manufacturerCountryId;
	}

	public void setManufacturerCountryId(CountryDetailsResponse manufacturerCountryId) {
		this.manufacturerCountryId = manufacturerCountryId;
	}

	public GoodsConditionResponse getGoodsConditionId() {
		return goodsConditionId;
	}

	public void setGoodsConditionId(GoodsConditionResponse goodsConditionId) {
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

	public OutbInvoiceDetailsResponse getOutbInvoiceDetail() {
		return outbInvoiceDetail;
	}

	public void setOutbInvoiceDetail(OutbInvoiceDetailsResponse outbInvoiceDetail) {
		this.outbInvoiceDetail = outbInvoiceDetail;
	}

	public List<OutbBarCodeDetailResponse> getOutbBarcodeDetails() {
		return outbBarcodeDetails;
	}

	public void setOutbBarcodeDetails(List<OutbBarCodeDetailResponse> outbBarcodeDetails) {
		this.outbBarcodeDetails = outbBarcodeDetails;
	}

}

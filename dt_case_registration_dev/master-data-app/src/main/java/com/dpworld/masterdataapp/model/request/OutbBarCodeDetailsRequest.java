package com.dpworld.masterdataapp.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OutbBarCodeDetailsRequest {

	private long outbBarcodeDetails;

	private long barCodeId;

	private Boolean pallet;

	private Double palletQuantity;

	private Double palletNetWeight;

	private Boolean carton;

	private Double cartonQuantity;

	private Double cartonNetWeight;

	private Boolean pieces;

	private Double piecesQuantity;

	private Double piecesNetWeight;

	public long getOutbBarcodeDetails() {
		return outbBarcodeDetails;
	}

	public void setOutbBarcodeDetails(long outbBarcodeDetails) {
		this.outbBarcodeDetails = outbBarcodeDetails;
	}

	public long getBarCodeId() {
		return barCodeId;
	}

	public void setBarCodeId(long barCodeId) {
		this.barCodeId = barCodeId;
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

}

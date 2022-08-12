package com.dpworld.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "INB_BARCODE_DETAILS")
public class InbBarCodeDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "INB_BARCODE_DETAILS_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_inb_barcode_details")
	@SequenceGenerator(name = "seq_inb_barcode_details", sequenceName = "seq_inb_barcode_details", allocationSize = 1)
	private long inbBarcodeDetails;

	@JsonBackReference
	@NotNull
	@ManyToOne
	@JoinColumn(name = "INB_CARGO_INFORMATION_ID", referencedColumnName = "ID")
	private InbCargoInformation inbCargoInformationId;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "BARCODE_ID", referencedColumnName = "BARCODE_ID")
	private BarCode barCodeId;

	@Type(type = "true_false")
	@Column(name = "PALLET")
	private Boolean pallet;

	@Column(name = "PALLET_QUANTITY")
	private Double palletQuantity;

	@Column(name = "PALLET_NET_WEIGHT")
	private Double palletNetWeight;

	@Type(type = "true_false")
	@Column(name = "CARTON")
	private Boolean carton;

	@Column(name = "CARTON_QUANTITY")
	private Double cartonQuantity;

	@Column(name = "CARTON_NET_WEIGHT")
	private Double cartonNetWeight;

	@Type(type = "true_false")
	@Column(name = "PIECES")
	private Boolean pieces;

	@Column(name = "PIECES_QUANTITY")
	private Double piecesQuantity;

	@Column(name = "PIECES_NET_WEIGHT")
	private Double piecesNetWeight;

	public InbBarCodeDetails() {
		super();
	}

	public InbBarCodeDetails(long inbBarcodeDetails) {
		super();
		this.inbBarcodeDetails = inbBarcodeDetails;
	}

	public long getInbBarcodeDetails() {
		return inbBarcodeDetails;
	}

	public void setInbBarcodeDetails(long inbBarcodeDetails) {
		this.inbBarcodeDetails = inbBarcodeDetails;
	}

	public InbCargoInformation getInbCargoInformationId() {
		return inbCargoInformationId;
	}

	public void setInbCargoInformationId(InbCargoInformation inbCargoInformationId) {
		this.inbCargoInformationId = inbCargoInformationId;
	}

	public BarCode getBarCodeId() {
		return barCodeId;
	}

	public void setBarCodeId(BarCode barCodeId) {
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

	@Override
	public String toString() {
		return "InbBarCodeDetails [inbBarcodeDetails=" + inbBarcodeDetails + ", inbCargoInformationId=" + inbCargoInformationId + ", barCodeId=" + barCodeId + ", pallet=" + pallet
				+ ", palletQuantity=" + palletQuantity + ", palletNetWeight=" + palletNetWeight + ", carton=" + carton + ", cartonQuantity=" + cartonQuantity + ", cartonNetWeight=" + cartonNetWeight
				+ ", pieces=" + pieces + ", piecesQuantity=" + piecesQuantity + ", piecesNetWeight=" + piecesNetWeight + "]";
	}

}

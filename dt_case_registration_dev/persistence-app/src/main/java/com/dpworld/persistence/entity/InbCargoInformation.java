package com.dpworld.persistence.entity;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity @OptimisticLocking(type = OptimisticLockType.ALL) @DynamicUpdate
@Table(name = "INB_CARGO_INFORMATION", indexes = { @Index(columnList = "INBOUND_ID"), @Index(columnList = "HS_CODE_ID") })
public class InbCargoInformation implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_inb_cargo_info")
	@SequenceGenerator(name = "seq_inb_cargo_info", sequenceName = "seq_inb_cargo_info", allocationSize = 1)
	private long id;

	@JsonBackReference
	@NotNull
	@ManyToOne
	@JoinColumn(name = "INBOUND_ID", referencedColumnName = "INBOUND_ID")
	private Inbound inbound;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "HS_CODE_ID", referencedColumnName = "HS_CODE_ID")
	private HsCode hsCodeId;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "MANUFACTURER_COUNTRY_ID", referencedColumnName = "COUNTRY_ID")
	private CountryDetails manufacturerCountryId;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "GOODS_CONDITION_ID", referencedColumnName = "GOODS_CONDITION_ID")
	private GoodsCondition goodsConditionId;

	@Column(name = "DUTY_VALUE")
	private Double dutyValue;

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

	@Column(name = "PACKAGE_DESCRIPTION", length = 30)
	private String packageDescription;

	@JsonManagedReference
	@OneToOne(mappedBy = "inbCargoInformationId", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private InbInvoiceDetail inbInvoiceDetail;

	@JsonManagedReference
	@OneToMany(mappedBy = "inbCargoInformationId", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<InbBarCodeDetails> inbBarcodeDetails;

	public InbCargoInformation(long id) {
		super();
		this.id = id;
	}

	public InbCargoInformation() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Inbound getInbound() {
		return inbound;
	}

	public void setInbound(Inbound inbound) {
		this.inbound = inbound;
	}

	public HsCode getHsCodeId() {
		return hsCodeId;
	}

	public void setHsCodeId(HsCode hsCodeId) {
		this.hsCodeId = hsCodeId;
	}

	public CountryDetails getManufacturerCountryId() {
		return manufacturerCountryId;
	}

	public void setManufacturerCountryId(CountryDetails manufacturerCountryId) {
		this.manufacturerCountryId = manufacturerCountryId;
	}

	public GoodsCondition getGoodsConditionId() {
		return goodsConditionId;
	}

	public void setGoodsConditionId(GoodsCondition goodsConditionId) {
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

	public InbInvoiceDetail getInbInvoiceDetail() {
		return inbInvoiceDetail;
	}

	public void setInbInvoiceDetail(InbInvoiceDetail inbInvoiceDetail) {
		this.inbInvoiceDetail = inbInvoiceDetail;
	}

	public List<InbBarCodeDetails> getInbBarcodeDetails() {
		return inbBarcodeDetails;
	}

	public void setInbBarcodeDetails(List<InbBarCodeDetails> inbBarcodeDetails) {
		this.inbBarcodeDetails = inbBarcodeDetails;
	}

	@Override
	public String toString() {
		return "InbCargoInformation [id=" + id + ", hsCodeId=" + hsCodeId + ", manufacturerCountryId=" + manufacturerCountryId + ", goodsConditionId=" + goodsConditionId + ", dutyValue=" + dutyValue
				+ ", pallet=" + pallet + ", palletQuantity=" + palletQuantity + ", palletNetWeight=" + palletNetWeight + ", carton=" + carton + ", cartonQuantity=" + cartonQuantity
				+ ", cartonNetWeight=" + cartonNetWeight + ", pieces=" + pieces + ", piecesQuantity=" + piecesQuantity + ", piecesNetWeight=" + piecesNetWeight + ", packageDescription="
				+ packageDescription + "]";
	}
	
	

}

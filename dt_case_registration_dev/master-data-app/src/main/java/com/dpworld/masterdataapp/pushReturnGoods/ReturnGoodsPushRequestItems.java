package com.dpworld.masterdataapp.pushReturnGoods;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReturnGoodsPushRequestItems {

	@XmlElement(name = "ArrivedFrom")
	private String arrivedFrom;

	@XmlElement(name = "Freezone_LocalMarket")
	private String marketType;

	@XmlElement(name = "ArrivalDate")
	private String arrivalDate;

	@XmlElement(name = "ModeofTransport")
	private String modeofTransport;

	@XmlElement(name = "VoyageNumber")
	private String voyageNumber;

	@XmlElement(name = "Carrier")
	private String carrier;

	@XmlElement(name = "Inbound_Declaration_Number")
	private String inbDeclarationNumber;

	@XmlElement(name = "DeclarationType")
	private String declarationType;

	@XmlElement(name = "LocalGoodsPassNumber")
	private String localGoodsPassNumber;

	@XmlElement(name = "ConsigneeName")
	private String consigneeName;

	@XmlElement(name = "BusinessCode")
	private String businessCode;

	@XmlElement(name = "HSCode")
	private String hsCode;

	@XmlElement(name = "HSCodeDescription")
	private String hsCodeDescription;

	@XmlElement(name = "SKU_Barcode")
	private String skuBarcode;

	@XmlElement(name = "PackageType")
	private String packageType;

	@XmlElement(name = "Pallet")
	private String pallet;

	@XmlElement(name = "Quantity")
	private String quantity;

	@XmlElement(name = "Weight")
	private String weight;

	@XmlElement(name = "Carton")
	private String carton;

	@XmlElement(name = "CountryOfManufacture")
	private String countryOfManufacture;

	@XmlElement(name = "PackageDescription")
	private String packageDescription;

	@XmlElement(name = "GoodsCondition")
	private String goodsCondition;

	@XmlElement(name = "Permit")
	private String permit;

	@XmlElement(name = "Authority")
	private String authority;

	@XmlElement(name = "InvoiceNumber")
	private String invoiceNumber;

	@XmlElement(name = "InvoiceDate")
	private String invoiceDate;

	@XmlElement(name = "Currency")
	private String currency;

	@XmlElement(name = "CIFValue")
	private String cifValue;

	@XmlElement(name = "CustomsDeclaration")
	private String customsDeclaration;

	@XmlElement(name = "CertificateOfOrigin")
	private String certificateOfOrigin;

	@XmlElement(name = "CommercialInvoice")
	private String commercialInvoice;

	@XmlElement(name = "PackingList")
	private String packingList;

	@XmlElement(name = "returngoods_Reference_Number")
	private String returngoodsReferenceNumber;

	@XmlElement(name = "Outbound_Declaration_Number")
	private String outboundDeclarationNumber;

	public String getArrivedFrom() {
		return arrivedFrom;
	}

	public void setArrivedFrom(String arrivedFrom) {
		this.arrivedFrom = arrivedFrom;
	}

	public String getMarketType() {
		return marketType;
	}

	public void setMarketType(String marketType) {
		this.marketType = marketType;
	}

	public String getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public String getModeofTransport() {
		return modeofTransport;
	}

	public void setModeofTransport(String modeofTransport) {
		this.modeofTransport = modeofTransport;
	}

	public String getVoyageNumber() {
		return voyageNumber;
	}

	public void setVoyageNumber(String voyageNumber) {
		this.voyageNumber = voyageNumber;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getInbDeclarationNumber() {
		return inbDeclarationNumber;
	}

	public void setInbDeclarationNumber(String inbDeclarationNumber) {
		this.inbDeclarationNumber = inbDeclarationNumber;
	}

	public String getDeclarationType() {
		return declarationType;
	}

	public void setDeclarationType(String declarationType) {
		this.declarationType = declarationType;
	}

	public String getLocalGoodsPassNumber() {
		return localGoodsPassNumber;
	}

	public void setLocalGoodsPassNumber(String localGoodsPassNumber) {
		this.localGoodsPassNumber = localGoodsPassNumber;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getHsCode() {
		return hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	public String getHsCodeDescription() {
		return hsCodeDescription;
	}

	public void setHsCodeDescription(String hsCodeDescription) {
		this.hsCodeDescription = hsCodeDescription;
	}

	public String getSkuBarcode() {
		return skuBarcode;
	}

	public void setSkuBarcode(String skuBarcode) {
		this.skuBarcode = skuBarcode;
	}

	public String getPackageType() {
		return packageType;
	}

	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}

	public String getPallet() {
		return pallet;
	}

	public void setPallet(String pallet) {
		this.pallet = pallet;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getCarton() {
		return carton;
	}

	public void setCarton(String carton) {
		this.carton = carton;
	}

	public String getCountryOfManufacture() {
		return countryOfManufacture;
	}

	public void setCountryOfManufacture(String countryOfManufacture) {
		this.countryOfManufacture = countryOfManufacture;
	}

	public String getPackageDescription() {
		return packageDescription;
	}

	public void setPackageDescription(String packageDescription) {
		this.packageDescription = packageDescription;
	}

	public String getGoodsCondition() {
		return goodsCondition;
	}

	public void setGoodsCondition(String goodsCondition) {
		this.goodsCondition = goodsCondition;
	}

	public String getPermit() {
		return permit;
	}

	public void setPermit(String permit) {
		this.permit = permit;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCifValue() {
		return cifValue;
	}

	public void setCifValue(String cifValue) {
		this.cifValue = cifValue;
	}

	public String getCustomsDeclaration() {
		return customsDeclaration;
	}

	public void setCustomsDeclaration(String customsDeclaration) {
		this.customsDeclaration = customsDeclaration;
	}

	public String getCertificateOfOrigin() {
		return certificateOfOrigin;
	}

	public void setCertificateOfOrigin(String certificateOfOrigin) {
		this.certificateOfOrigin = certificateOfOrigin;
	}

	public String getCommercialInvoice() {
		return commercialInvoice;
	}

	public void setCommercialInvoice(String commercialInvoice) {
		this.commercialInvoice = commercialInvoice;
	}

	public String getPackingList() {
		return packingList;
	}

	public void setPackingList(String packingList) {
		this.packingList = packingList;
	}

	public String getReturngoodsReferenceNumber() {
		return returngoodsReferenceNumber;
	}

	public void setReturngoodsReferenceNumber(String returngoodsReferenceNumber) {
		this.returngoodsReferenceNumber = returngoodsReferenceNumber;
	}

	public String getOutboundDeclarationNumber() {
		return outboundDeclarationNumber;
	}

	public void setOutboundDeclarationNumber(String outboundDeclarationNumber) {
		this.outboundDeclarationNumber = outboundDeclarationNumber;
	}

	@Override
	public String toString() {
		return "XMLReturnGoodsPushItems [arrivedFrom=" + arrivedFrom + ", marketType=" + marketType + ", arrivalDate=" + arrivalDate + ", modeofTransport=" + modeofTransport + ", voyageNumber="
				+ voyageNumber + ", carrier=" + carrier + ", inbDeclarationNumber=" + inbDeclarationNumber + ", declarationType=" + declarationType + ", localGoodsPassNumber=" + localGoodsPassNumber
				+ ", consigneeName=" + consigneeName + ", businessCode=" + businessCode + ", hsCode=" + hsCode + ", hsCodeDescription=" + hsCodeDescription + ", skuBarcode=" + skuBarcode
				+ ", packageType=" + packageType + ", pallet=" + pallet + ", quantity=" + quantity + ", weight=" + weight + ", carton=" + carton + ", countryOfManufacture=" + countryOfManufacture
				+ ", packageDescription=" + packageDescription + ", goodsCondition=" + goodsCondition + ", permit=" + permit + ", authority=" + authority + ", invoiceNumber=" + invoiceNumber
				+ ", invoiceDate=" + invoiceDate + ", currency=" + currency + ", cifValue=" + cifValue + ", customsDeclaration=" + customsDeclaration + ", certificateOfOrigin=" + certificateOfOrigin
				+ ", commercialInvoice=" + commercialInvoice + ", packingList=" + packingList + ", returngoodsReferenceNumber=" + returngoodsReferenceNumber + ", outboundDeclarationNumber="
				+ outboundDeclarationNumber + "]";
	}

}
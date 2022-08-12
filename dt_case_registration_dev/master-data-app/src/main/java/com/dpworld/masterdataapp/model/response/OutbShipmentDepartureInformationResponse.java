package com.dpworld.masterdataapp.model.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OutbShipmentDepartureInformationResponse {

	private Long id;

	private CountryDetailsResponse countryDetail;

	private ShipmentSoldTypeResponse shipmentSoldTypeId;

	//private MarketTypeResponse marketTypeId;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private LocalDate shipmentDate;

	private String voyageNumber;

	private String carrier;

	private Byte transportMode; // Pending length

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CountryDetailsResponse getCountryDetail() {
		return countryDetail;
	}

	public void setCountryDetail(CountryDetailsResponse countryDetail) {
		this.countryDetail = countryDetail;
	}

	public ShipmentSoldTypeResponse getShipmentSoldTypeId() {
		return shipmentSoldTypeId;
	}

	public void setShipmentSoldTypeId(ShipmentSoldTypeResponse shipmentSoldTypeId) {
		this.shipmentSoldTypeId = shipmentSoldTypeId;
	}

	public LocalDate getShipmentDate() {
		return shipmentDate;
	}

	public void setShipmentDate(LocalDate shipmentDate) {
		this.shipmentDate = shipmentDate;
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

	public Byte getTransportMode() {
		return transportMode;
	}

	public void setTransportMode(Byte transportMode) {
		this.transportMode = transportMode;
	}

}

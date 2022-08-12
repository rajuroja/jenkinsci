package com.dpworld.masterdataapp.model.response;

import java.time.LocalDate;

import com.dpworld.persistence.entity.CountryDetails;
import com.dpworld.persistence.entity.MarketType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InbShipmentArrivalInformationResponse {

	private Long id;

	private CountryDetailsResponse arrivedFromCountryId;

	private MarketType marketTypeId;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private LocalDate arrivalDate;

	private String voyageNumber;

	private String carrier;

	private Byte transportMode; // Pending length

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CountryDetailsResponse getArrivedFromCountryId() {
		return arrivedFromCountryId;
	}

	public void setArrivedFromCountryId(CountryDetailsResponse arrivedFromCountryId) {
		this.arrivedFromCountryId = arrivedFromCountryId;
	}

	public MarketType getMarketTypeId() {
		return marketTypeId;
	}

	public void setMarketTypeId(MarketType marketTypeId) {
		this.marketTypeId = marketTypeId;
	}

	public LocalDate getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(LocalDate arrivalDate) {
		this.arrivalDate = arrivalDate;
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

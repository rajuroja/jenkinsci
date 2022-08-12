package com.dpworld.masterdataapp.model.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OutbShipmentDepartureInformationRequest {

	private long id;

	private Integer countryDetail;

	// private Integer marketTypeId;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private LocalDate shipmentDate;

	private String voyageNumber;

	private String carrier;

	private Byte shipmentSoldTypeId;

	private Byte transportMode; // Pending length

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getCountryDetail() {
		return countryDetail;
	}

	public void setCountryDetail(Integer countryDetail) {
		this.countryDetail = countryDetail;
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

	public Byte getShipmentSoldTypeId() {
		return shipmentSoldTypeId;
	}

	public void setShipmentSoldTypeId(Byte shipmentSoldTypeId) {
		this.shipmentSoldTypeId = shipmentSoldTypeId;
	}

	public Byte getTransportMode() {
		return transportMode;
	}

	public void setTransportMode(Byte transportMode) {
		this.transportMode = transportMode;
	}

	@Override
	public String toString() {
		return "OutbShipmentDepartureInformationRequest [id=" + id + ", countryDetail=" + countryDetail + ", shipmentDate=" + shipmentDate + ", voyageNumber=" + voyageNumber + ", carrier=" + carrier
				+ ", shipmentSoldTypeId=" + shipmentSoldTypeId + ", transportMode=" + transportMode + "]";
	}

}

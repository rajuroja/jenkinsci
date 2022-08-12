package com.dpworld.persistence.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

@Entity @OptimisticLocking(type = OptimisticLockType.ALL) @DynamicUpdate
@Table(name = "OUTB_SHIPMENT_DEPARTURE_INFO", indexes = @Index(columnList = "OUTBOUND_ID"))
public class OutbShipmentDepartureInformation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_outb_ship_dept_info")
	@SequenceGenerator(name = "seq_outb_ship_dept_info", sequenceName = "seq_outb_ship_dept_info", allocationSize = 1)
	private long id;

	@JsonBackReference
	@NotNull
	@OneToOne
	@JoinColumn(name = "OUTBOUND_ID", referencedColumnName = "OUTBOUND_ID")
	private Outbound outbound;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "SHIPMENT_SOLD_TYPE_ID", referencedColumnName = "SHIPMENT_SOLD_TYPE_ID")
	private ShipmentSoldType shipmentSoldTypeId;

	@ManyToOne
	@JoinColumn(name = "SHIPMENT_DESTINATION_ID", referencedColumnName = "COUNTRY_ID", nullable = true)
	private CountryDetails countryDetail;

//	@ManyToOne
//	@JoinColumn(name = "MARKET_TYPE_ID", referencedColumnName = "MARKET_TYPE_ID", nullable = true)
//	private MarketType marketTypeId;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "dd-MM-yyyy")
	@Column(name = "SHIPMENT_DATE")
	private LocalDate shipmentDate;

	@Column(name = "TRANSPORT_MODE")
	private Byte transportMode;

	@Column(name = "VOYAGE_NUMBER", length = 20)
	private String voyageNumber;

	@Column(name = "CARRIER", length = 20)
	private String carrier;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Outbound getOutbound() {
		return outbound;
	}

	public void setOutbound(Outbound outbound) {
		this.outbound = outbound;
	}

	public ShipmentSoldType getShipmentSoldType() {
		return shipmentSoldTypeId;
	}

	public void setShipmentSoldType(ShipmentSoldType shipmentSoldType) {
		this.shipmentSoldTypeId = shipmentSoldType;
	}

	public CountryDetails getCountryDetail() {
		return countryDetail;
	}

	public void setCountryDetail(CountryDetails countryDetail) {
		this.countryDetail = countryDetail;
	}

	public LocalDate getShipmentDate() {
		return shipmentDate;
	}

	public void setShipmentDate(LocalDate shipmentDate) {
		this.shipmentDate = shipmentDate;
	}

	public Byte getTransportMode() {
		return transportMode;
	}

	public void setTransportMode(Byte transportMode) {
		this.transportMode = transportMode;
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

	public ShipmentSoldType getShipmentSoldTypeId() {
		return shipmentSoldTypeId;
	}

	public void setShipmentSoldTypeId(ShipmentSoldType shipmentSoldTypeId) {
		this.shipmentSoldTypeId = shipmentSoldTypeId;
	}

}

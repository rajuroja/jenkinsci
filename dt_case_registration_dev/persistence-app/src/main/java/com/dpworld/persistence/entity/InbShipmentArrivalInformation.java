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
@Table(name = "INB_SHIPMENT_ARRIVAL_INFO", indexes = @Index(columnList = "INBOUND_ID"))
public class InbShipmentArrivalInformation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_inb_ship_arr_info")
	@SequenceGenerator(name = "seq_inb_ship_arr_info", sequenceName = "seq_inb_ship_arr_info", allocationSize = 1)
	private long id;

	@JsonBackReference
	@NotNull
	@OneToOne
	@JoinColumn(name = "INBOUND_ID", referencedColumnName = "INBOUND_ID")
	private Inbound inbound;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "ARRIVED_FROM_ID", referencedColumnName = "COUNTRY_ID")
	private CountryDetails arrivedFromCountryId;

	@ManyToOne
	@JoinColumn(name = "MARKET_TYPE_ID", referencedColumnName = "MARKET_TYPE_ID", nullable = true)
	private MarketType marketTypeId;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "dd-MM-yyyy")
	@Column(name = "ARRIVAL_DATE")
	private LocalDate arrivalDate;

	@Column(name = "VOYAGE_NUMBER", length = 20)
	private String voyageNumber;

	@Column(name = "CARRIER", length = 20)
	private String carrier;

	@Column(name = "TRANSPORT_MODE")
	private Byte transportMode; //Pending

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

	public CountryDetails getArrivedFromCountryId() {
		return arrivedFromCountryId;
	}

	public void setArrivedFromCountryId(CountryDetails arrivedFromCountryId) {
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

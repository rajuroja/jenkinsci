package com.dpworld.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "SHIPMENT_SOLD_TYPE")
public class ShipmentSoldType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SHIPMENT_SOLD_TYPE_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_shipment_sold_type")
	@SequenceGenerator(name = "seq_shipment_sold_type", sequenceName = "seq_shipment_sold_type", allocationSize = 1)
	private Byte shipmentSoldTypeId;

	@Column(name = "SHIPMENT_SOLD_TYPE", length = 10)
	private String shipmentSoldType;

	public ShipmentSoldType() {
		super();
	}

	public ShipmentSoldType(Integer shipmentSoldTypeId) {
		super();
		this.shipmentSoldTypeId = shipmentSoldTypeId.byteValue();
	}

	public ShipmentSoldType(Byte shipmentSoldTypeId, String shipmentSoldType) {
		super();
		this.shipmentSoldTypeId = shipmentSoldTypeId;
		this.shipmentSoldType = shipmentSoldType;
	}

	public Byte getShipmentSoldTypeId() {
		return shipmentSoldTypeId;
	}

	public void setShipmentSoldTypeId(Byte shipmentSoldTypeId) {
		this.shipmentSoldTypeId = shipmentSoldTypeId;
	}

	public String getShipmentSoldType() {
		return shipmentSoldType;
	}

	public void setShipmentSoldType(String shipmentSoldType) {
		this.shipmentSoldType = shipmentSoldType;
	}

}

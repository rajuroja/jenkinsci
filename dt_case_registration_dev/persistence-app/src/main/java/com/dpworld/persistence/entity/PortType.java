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
@Table(name = "PORT_MASTER")
public class PortType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_port_master")
	@SequenceGenerator(name = "seq_port_master", sequenceName = "seq_port_master", allocationSize = 1)
	@Column(name = "PORT_ID")
	private Integer portId;

	@Column(name = "PORT_NAME", length = 255)
	private String portName;

	public PortType() {
	}

	public PortType(Integer portId) {
		super();
		this.portId = portId;
	}

	public PortType(Integer portId, String portName) {
		super();
		this.portId = portId;
		this.portName = portName;
	}

	public Integer getPortId() {
		return portId;
	}

	public void setPortId(Integer portId) {
		this.portId = portId;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	@Override
	public String toString() {
		return "PortType [portId=" + portId + ", portName=" + portName + "]";
	}

}
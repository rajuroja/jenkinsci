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
@Table(name = "TERMINAL_MASTER")
public class TerminalType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_terminal_master")
	@SequenceGenerator(name = "seq_terminal_master", sequenceName = "seq_terminal_master", allocationSize = 1)
	@Column(name = "TERMINAL_ID")
	private Integer terminalId;

	@Column(name = "TERMINAL_NAME", length = 255)
	private String terminalName;

	@Column(name = "PORT_MASTER_ID")
	private Integer portId;

	public TerminalType() {
	}

	public TerminalType(Integer terminalId) {
		super();
		this.terminalId = terminalId;
	}

	public TerminalType(Integer terminalId, String terminalName) {
		super();
		this.terminalId = terminalId;
		this.terminalName = terminalName;
	}

	public TerminalType(Integer terminalId, String terminalName, Integer portId) {
		super();
		this.terminalId = terminalId;
		this.terminalName = terminalName;
		this.portId = portId;
	}

	public Integer getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}

	public String getTerminalName() {
		return terminalName;
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

	public Integer getPortId() {
		return portId;
	}

	public void setPortId(Integer portId) {
		this.portId = portId;
	}

	@Override
	public String toString() {
		return "TerminalType [terminalId=" + terminalId + ", terminalName=" + terminalName + ", portId=" + portId + "]";
	}

}
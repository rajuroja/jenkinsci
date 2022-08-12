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
@Table(name = "MARKET_TYPE")
public class MarketType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MARKET_TYPE_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_market_type")
	@SequenceGenerator(name = "seq_market_type", sequenceName = "seq_market_type", allocationSize = 1)
	private Integer marketTypeId;

	@Column(name = "MARKET_TYPE", length = 30)
	private String marketType;

	public MarketType() {
	}

	public MarketType(Integer marketTypeId) {
		super();
		this.marketTypeId = marketTypeId;
	}

	public MarketType(Integer marketTypeId, String marketType) {
		super();
		this.marketTypeId = marketTypeId;
		this.marketType = marketType;
	}

	public Integer getMarketTypeId() {
		return marketTypeId;
	}

	public void setMarketTypeId(Integer marketTypeId) {
		this.marketTypeId = marketTypeId;
	}

	public String getMarketType() {
		return marketType;
	}

	public void setMarketType(String marketType) {
		this.marketType = marketType;
	}

	@Override
	public String toString() {
		return "MarketType [marketTypeId=" + marketTypeId + ", marketType=" + marketType + "]";
	}

}

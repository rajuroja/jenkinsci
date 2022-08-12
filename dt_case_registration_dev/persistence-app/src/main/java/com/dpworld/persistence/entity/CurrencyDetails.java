package com.dpworld.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "CURRENCY_DETAILS", indexes = @Index(columnList = "CURRENCY_NAME"))
public class CurrencyDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CURRENCY_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_currency_details")
	@SequenceGenerator(name = "seq_currency_details", sequenceName = "seq_currency_details", allocationSize = 1)
	private Integer currencyId;

	@Column(name = "CURRENCY_NAME", length = 100)
	private String currencyName;

//	@OneToMany(mappedBy = "currencyId", fetch = FetchType.LAZY)
//	private List<HsCode> hsCodes;

	public CurrencyDetails() {

	}

	public CurrencyDetails(Integer currencyId) {
		super();
		this.currencyId = currencyId;
	}

	public CurrencyDetails(Integer currencyId, String currencyName) {
		super();
		this.currencyId = currencyId;
		this.currencyName = currencyName;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	@Override
	public String toString() {
		return "CurrencyDetails [currencyId=" + currencyId + ", currencyName=" + currencyName + "]";
	}

}

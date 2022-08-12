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
@Table(name = "COUNTRY_DETAILS")
public class CountryDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "COUNTRY_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_country_details")
	@SequenceGenerator(name = "seq_country_details", sequenceName = "seq_country_details", allocationSize = 1)
	private Integer countryId;

	@Column(name = "COUNTRY_NAME", length = 60)
	private String countryName;

	public CountryDetails() {
		super();
	}

	public CountryDetails(Integer countryId) {
		super();
		this.countryId = countryId;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

}

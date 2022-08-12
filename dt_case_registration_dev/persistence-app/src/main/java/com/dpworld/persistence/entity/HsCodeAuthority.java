package com.dpworld.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "HS_CODE_AUTHORITY", indexes = { @Index(columnList = "AUTHORITY_ID"), @Index(columnList = "HS_CODE_ID") })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class HsCodeAuthority implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_hs_code_authority")
	@SequenceGenerator(name = "seq_hs_code_authority", sequenceName = "seq_hs_code_authority", allocationSize = 1)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "AUTHORITY_ID")
	private Authority authority;

	@ManyToOne
	@JoinColumn(name = "HS_CODE_ID")
	private HsCode hsCode;

	@Column(name = "DUTY_VALUE") // Default
	private Double dutyValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Authority getAuthority() {
		return authority;
	}

	public void setAuthority(Authority authority) {
		this.authority = authority;
	}

	public HsCode getHsCode() {
		return hsCode;
	}

	public void setHsCode(HsCode hsCode) {
		this.hsCode = hsCode;
	}

	public Double getDutyValue() {
		return dutyValue;
	}

	public void setDutyValue(Double dutyValue) {
		this.dutyValue = dutyValue;
	}

}

package com.dpworld.persistence.entity;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "HS_CODE", indexes = @Index(columnList = "HS_CODE"))
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "hsCodeId")
public class HsCode implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "HS_CODE_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_hs_code")
	@SequenceGenerator(name = "seq_hs_code", sequenceName = "seq_hs_code", allocationSize = 1)
	private Long hsCodeId;

	@Column(name = "HS_CODE", length = 30)
	private String hsCode;

	@Column(name = "HS_CODE_DECRIPTION", length = 255)
	private String hsCodeDescription;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "CURRENCY_ID", referencedColumnName = "CURRENCY_ID")
	private CurrencyDetails currencyId;

	@Column(name = "DUTY_VALUE")
	private Double dutyValue;

//	@JsonIgnore
//	@JsonManagedReference		// not needed JsonManagedReference here
//	@OneToMany(mappedBy = "hsCodeId", fetch = FetchType.LAZY)
//	private List<BarCode> barCodes;

	@JsonIgnore
//	@JsonManagedReference
	@OneToMany(mappedBy = "hsCode", fetch = FetchType.LAZY)
	private List<HsCodeAuthority> hsCodeAuthorities;

	@Transient
	private String authorities;

	@Transient
	private String authoritiyCodes;

	public HsCode() {
		super();
	}

	public HsCode(Long hsCodeId) {
		super();
		this.hsCodeId = hsCodeId;
	}

	public Long getHsCodeId() {
		return hsCodeId;
	}

	public void setHsCodeId(Long hsCodeId) {
		this.hsCodeId = hsCodeId;
	}

	public String getHsCode() {
		return hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	public String getHsCodeDescription() {
		return hsCodeDescription;
	}

	public void setHsCodeDescription(String hsCodeDescription) {
		this.hsCodeDescription = hsCodeDescription;
	}

	public CurrencyDetails getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(CurrencyDetails currencyId) {
		this.currencyId = currencyId;
	}

	public Double getDutyValue() {
		return dutyValue;
	}

	public void setDutyValue(Double dutyValue) {
		this.dutyValue = dutyValue;
	}

//	public List<BarCode> getBarCodes() {
//		return barCodes;
//	}
//
//	public void setBarCodes(List<BarCode> barCodes) {
//		this.barCodes = barCodes;
//	}

	public List<HsCodeAuthority> getHsCodeAuthorities() {
		return hsCodeAuthorities;
	}

	public void setHsCodeAuthorities(List<HsCodeAuthority> hsCodeAuthorities) {
		this.hsCodeAuthorities = hsCodeAuthorities;
	}

	public String getAuthorities() {
		if (hsCodeAuthorities != null && !hsCodeAuthorities.isEmpty())
			return String.join(", ", hsCodeAuthorities.stream().map(e -> e.getAuthority().getAuthorityName()).collect(Collectors.toList()));
		return "";
	}

	public String getAuthoritiyCodes() {
		if (hsCodeAuthorities != null && !hsCodeAuthorities.isEmpty())
			return String.join(", ", hsCodeAuthorities.stream().map(e -> e.getAuthority().getAuthorityShortName()).collect(Collectors.toList()));
		return "";
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

	public void setAuthoritiyCodes(String authoritiyCodes) {
		this.authoritiyCodes = authoritiyCodes;
	}

}

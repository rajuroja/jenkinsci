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
@Table(name = "AUTHORITY_DETAILS")
//@Table(name = "AUTHORITY_DETAILS", indexes = @Index(columnList = "SHORT_NAME"))
public class AuthorityDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_authority_details")
	@SequenceGenerator(name = "seq_authority_details", sequenceName = "seq_authority_details", allocationSize = 1)
	private Long id;

//	@Column(name = "SHORT_NAME")
	private String shortName;

//	@Column(name = "FULL_NAME")
	private String fullName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}

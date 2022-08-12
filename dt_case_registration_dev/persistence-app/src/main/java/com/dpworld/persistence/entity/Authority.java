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
@Table(name = "AUTHORITY", indexes = @Index(columnList = "AUTHORITY_SHORT_NAME"))
public class Authority implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "AUTHORITY_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_authority")
	@SequenceGenerator(name = "seq_authority", sequenceName = "seq_authority", allocationSize = 1)
	private Integer authorityId;

	@Column(name = "AUTHORITY_NAME", length = 255)
	private String authorityName;

	@Column(name = "AUTHORITY_SHORT_NAME", length = 20)
	private String authorityShortName;

	public Authority() {
		super();
	}

	public Authority(Integer authorityId) {
		super();
		this.authorityId = authorityId;
	}

	public Integer getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(Integer authorityId) {
		this.authorityId = authorityId;
	}

	public String getAuthorityName() {
		return authorityName;
	}

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

	public String getAuthorityShortName() {
		return authorityShortName;
	}

	public void setAuthorityShortName(String authorityShortName) {
		this.authorityShortName = authorityShortName;
	}

}

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
@Table(name = "CASE_TYPE_MASTER")
public class CaseType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_case_type_master")
	@SequenceGenerator(name = "seq_case_type_master", sequenceName = "seq_case_type_master", allocationSize = 1)
	@Column(name = "CASE_TYPE_ID")
	private Integer caseTypeId;

	@Column(name = "CASE_TYPE_NAME", length = 255)
	private String caseTypeName;

	public CaseType() {
	}

	public CaseType(Integer caseTypeId) {
		super();
		this.caseTypeId = caseTypeId;
	}

	public CaseType(Integer caseTypeId, String caseTypeName) {
		super();
		this.caseTypeId = caseTypeId;
		this.caseTypeName = caseTypeName;
	}

	public Integer getCaseTypeId() {
		return caseTypeId;
	}

	public void setCaseTypeId(Integer caseTypeId) {
		this.caseTypeId = caseTypeId;
	}

	public String getCaseTypeName() {
		return caseTypeName;
	}

	public void setCaseTypeName(String caseTypeName) {
		this.caseTypeName = caseTypeName;
	}

	@Override
	public String toString() {
		return "CaseType [caseTypeId=" + caseTypeId + ", caseTypeName=" + caseTypeName + "]";
	}

}
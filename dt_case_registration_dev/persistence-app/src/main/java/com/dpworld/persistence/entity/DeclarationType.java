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
@Table(name = "DECLARATION_TYPE")
public class DeclarationType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_declaration_type")
	@SequenceGenerator(name = "seq_declaration_type", sequenceName = "seq_declaration_type", allocationSize = 1)
	@Column(name = "DECLARATION_TYPE_ID")
	private Integer declarationTypeId;

	@Column(name = "DECLARATION_TYPE", length = 255)
	private String declarationType;

	public DeclarationType() {
	}

	public DeclarationType(Integer declarationTypeId) {
		super();
		this.declarationTypeId = declarationTypeId;
	}

	public DeclarationType(Integer declarationTypeId, String declarationType) {
		super();
		this.declarationTypeId = declarationTypeId;
		this.declarationType = declarationType;
	}

	public Integer getDeclarationTypeId() {
		return declarationTypeId;
	}

	public void setDeclarationTypeId(Integer declarationTypeId) {
		this.declarationTypeId = declarationTypeId;
	}

	public String getDeclarationType() {
		return declarationType;
	}

	public void setDeclarationType(String declarationType) {
		this.declarationType = declarationType;
	}

	@Override
	public String toString() {
		return "DeclarationType [declarationTypeId=" + declarationTypeId + ", declarationType=" + declarationType + "]";
	}

}
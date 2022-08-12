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
@Table(name = "COMPLAIN_TYPE_MASTER")
public class ComplainType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_complain_type_master")
	@SequenceGenerator(name = "seq_complain_type_master", sequenceName = "seq_complain_type_master", allocationSize = 1)
	@Column(name = "COMPLAIN_TYPE_KEY_ID")
	private Integer complainTypeKeyId;

	@Column(name = "COMPLAIN_TYPE_NAME")
	private String complainTypeName;

	
	@Column(name = "COMPLAIN_TYPE_ID")
	private String complainTypeId;

	public ComplainType() {
	}

	public ComplainType(Integer complainTypeKeyId, String complainTypeName, String complainTypeId) {
		super();
		this.complainTypeKeyId = complainTypeKeyId;
		this.complainTypeName = complainTypeName;
		this.complainTypeId = complainTypeId;
	}

	public ComplainType(Integer complainTypeKeyId) {
		super();
		this.complainTypeKeyId = complainTypeKeyId;
	}

	public ComplainType(Integer complainTypeKeyId, String complainTypeName) {
		super();
		this.complainTypeKeyId = complainTypeKeyId;
		this.complainTypeName = complainTypeName;
	}

	public Integer getComplainTypeKeyId() {
		return complainTypeKeyId;
	}

	public void setComplainTypeKeyId(Integer complainTypeKeyId) {
		this.complainTypeKeyId = complainTypeKeyId;
	}

	public String getComplainTypeName() {
		return complainTypeName;
	}

	public void setComplainTypeName(String complainTypeName) {
		this.complainTypeName = complainTypeName;
	}

	public String getComplainTypeId() {
		return complainTypeId;
	}

	public void setComplainTypeId(String complainTypeId) {
		this.complainTypeId = complainTypeId;
	}

	@Override
	public String toString() {
		return "ComplainType [complainTypeKeyId=" + complainTypeKeyId + ", complainTypeName=" + complainTypeName
				+ ", complainTypeId=" + complainTypeId + "]";
	}

}
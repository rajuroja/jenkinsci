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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity @OptimisticLocking(type = OptimisticLockType.ALL) @DynamicUpdate
@Table(name = "INB_DECLARATION_INFORMATION", indexes = { @Index(columnList = "DECLARATION_NUMBER"), @Index(columnList = "BUSINESS_CODE"), @Index(columnList = "INBOUND_ID") })
public class InbDeclarationInformation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_inb_decl_info")
	@SequenceGenerator(name = "seq_inb_decl_info", sequenceName = "seq_inb_decl_info", allocationSize = 1)
	private long id;

	@JsonBackReference
	@NotNull
	@OneToOne
	@JoinColumn(name = "INBOUND_ID", referencedColumnName = "INBOUND_ID")
	private Inbound inbound;

	@ManyToOne
	@JoinColumn(name = "DECLARATION_TYPE_ID", referencedColumnName = "DECLARATION_TYPE_ID")
	private DeclarationType declarationTypeId;

	@Column(name = "DECLARATION_NUMBER", length = 13)
	private String declarationNumber;

	@Column(name = "LOCAL_GOODS_PASS_NUMBER", length = 15)
	private String localGoodsPassNumber;

	@Column(name = "CONSIGNEE_NAME", length = 255)
	private String consigneeName;

	@Column(name = "BUSINESS_CODE", length = 30)
	private String businessCode; // Check type

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Inbound getInbound() {
		return inbound;
	}

	public void setInbound(Inbound inbound) {
		this.inbound = inbound;
	}

	public DeclarationType getDeclarationTypeId() {
		return declarationTypeId;
	}

	public void setDeclarationTypeId(DeclarationType declarationTypeId) {
		this.declarationTypeId = declarationTypeId;
	}

	public String getDeclarationNumber() {
		return declarationNumber;
	}

	public void setDeclarationNumber(String declarationNumber) {
		this.declarationNumber = declarationNumber;
	}

	public String getLocalGoodsPassNumber() {
		return localGoodsPassNumber;
	}

	public void setLocalGoodsPassNumber(String localGoodsPassNumber) {
		this.localGoodsPassNumber = localGoodsPassNumber;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

}

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
@Table(name = "OUTB_DECLARATION_INFO", indexes = { @Index(columnList = "INB_DECLARATION_TYPE_ID"), @Index(columnList = "BUSINESS_CODE"), @Index(columnList = "OUTBOUND_ID") })
public class OutbDeclarationInformation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_outb_decl_info")
	@SequenceGenerator(name = "seq_outb_decl_info", sequenceName = "seq_outb_decl_info", allocationSize = 1)
	private long id;

	@JsonBackReference
	@NotNull
	@OneToOne
	@JoinColumn(name = "OUTBOUND_ID", referencedColumnName = "OUTBOUND_ID")
	private Outbound outbound;

	@Column(name = "INB_DECLARATION_NUMBER", length = 13)
	private String inbDeclarationNumber;

	@ManyToOne
	@JoinColumn(name = "INB_DECLARATION_TYPE_ID", referencedColumnName = "DECLARATION_TYPE_ID", nullable = true)
	private DeclarationType inbDeclarationTypeId;

	@Column(name = "OUTB_DECLARATION_NUMBER", length = 13)
	private String outbDeclarationNumber;

	@ManyToOne
	@JoinColumn(name = "OUTB_DECLARATION_TYPE_ID", referencedColumnName = "DECLARATION_TYPE_ID", nullable = true)
	private DeclarationType outbDeclarationTypeId;

	@Column(name = "BUYER_NAME", length = 255)
	private String consigneeName;

	@Column(name = "BUSINESS_CODE", length = 30)
	private String businessCode;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Outbound getOutbound() {
		return outbound;
	}

	public void setOutbound(Outbound outbound) {
		this.outbound = outbound;
	}

	public String getInbDeclarationNumber() {
		return inbDeclarationNumber;
	}

	public void setInbDeclarationNumber(String inbDeclarationNumber) {
		this.inbDeclarationNumber = inbDeclarationNumber;
	}

	public DeclarationType getInbDeclarationTypeId() {
		return inbDeclarationTypeId;
	}

	public void setInbDeclarationTypeId(DeclarationType inbDeclarationTypeId) {
		this.inbDeclarationTypeId = inbDeclarationTypeId;
	}

	public String getOutbDeclarationNumber() {
		return outbDeclarationNumber;
	}

	public void setOutbDeclarationNumber(String outbDeclarationNumber) {
		this.outbDeclarationNumber = outbDeclarationNumber;
	}

	public DeclarationType getOutbDeclarationTypeId() {
		return outbDeclarationTypeId;
	}

	public void setOutbDeclarationTypeId(DeclarationType outbDeclarationTypeId) {
		this.outbDeclarationTypeId = outbDeclarationTypeId;
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

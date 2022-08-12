package com.dpworld.persistence.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity @OptimisticLocking(type = OptimisticLockType.ALL) @DynamicUpdate
@Table(name = "INB_UPLOAD_DOCUMENTS")
public class InbUploadDocuments implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_inb_upload_doc")
	@SequenceGenerator(name = "seq_inb_upload_doc", sequenceName = "seq_inb_upload_doc", allocationSize = 1)
	private long id;

	@JsonIgnore
	@NotNull
	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "INBOUND_ID", referencedColumnName = "INBOUND_ID")
	private Inbound inbound;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "UPLOAD_DOCUMENT_TYPE_ID", referencedColumnName = "UPLOAD_DOCUMENT_TYPE_ID")
	private UploadDocumentType uploadDocumentTypeId;

	@Column(name = "FILE_PATH", length = 300)
	private String filePath;

	@Type(type="true_false")
	@Column(name = "IS_CANCEL") //Default
	private Boolean isCancel;

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

	public UploadDocumentType getUploadDocumentTypeId() {
		return uploadDocumentTypeId;
	}

	public void setUploadDocumentTypeId(UploadDocumentType uploadDocumentTypeId) {
		this.uploadDocumentTypeId = uploadDocumentTypeId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Boolean getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(Boolean isCancel) {
		this.isCancel = isCancel;
	}

}

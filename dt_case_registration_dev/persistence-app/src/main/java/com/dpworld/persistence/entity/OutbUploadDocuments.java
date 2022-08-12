package com.dpworld.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity @OptimisticLocking(type = OptimisticLockType.ALL) @DynamicUpdate
@Table(name = "OUTB_UPLOAD_DOCUMENTS")
public class OutbUploadDocuments implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_outb_upload_doc")
	@SequenceGenerator(name = "seq_outb_upload_doc", sequenceName = "seq_outb_upload_doc", allocationSize = 1)
	private long id;

	@JsonBackReference
	@NotNull
	@ManyToOne
	@JoinColumn(name = "OUTBOUND_ID", referencedColumnName = "OUTBOUND_ID")
	private Outbound outbound;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "UPLOAD_DOCUMENT_TYPE_ID", referencedColumnName = "UPLOAD_DOCUMENT_TYPE_ID")
	private UploadDocumentType uploadDocumentTypeId;

	@Column(name = "FILE_PATH", length = 300)
	private String filePath;

	@Type(type = "true_false")
	@Column(name = "IS_CANCEL") // Default
	private Boolean isCancel;

	@Transient
	private String documentType;

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

	public String getDocumentType() {
		return (uploadDocumentTypeId != null) ? uploadDocumentTypeId.getUploadDocumentTypeName() : "";
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

}

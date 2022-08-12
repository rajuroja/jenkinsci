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
@Table(name = "UPLOAD_DOCUMENT_TYPE")
public class UploadDocumentType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "UPLOAD_DOCUMENT_TYPE_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_upload_doc_type")
	@SequenceGenerator(name = "seq_upload_doc_type", sequenceName = "seq_upload_doc_type", allocationSize = 1)
	private Byte uploadDocumentTypeId;

	@Column(name = "UPLOAD_DOCUMENT_TYPE_NAME", length = 50)
	private String uploadDocumentTypeName;

	public Byte getUploadDocumentTypeId() {
		return uploadDocumentTypeId;
	}

	public void setUploadDocumentTypeId(Byte uploadDocumentTypeId) {
		this.uploadDocumentTypeId = uploadDocumentTypeId;
	}

	public String getUploadDocumentTypeName() {
		return uploadDocumentTypeName;
	}

	public void setUploadDocumentTypeName(String uploadDocumentTypeName) {
		this.uploadDocumentTypeName = uploadDocumentTypeName;
	}

}

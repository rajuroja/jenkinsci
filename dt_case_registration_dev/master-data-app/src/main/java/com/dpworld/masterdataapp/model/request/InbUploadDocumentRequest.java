package com.dpworld.masterdataapp.model.request;

import com.dpworld.persistence.entity.Inbound;
import com.dpworld.persistence.entity.UploadDocumentType;

public class InbUploadDocumentRequest {

	private long id;

	private Inbound inbound;

	private UploadDocumentType uploadDocumentTypeId;

	private String filePath;

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

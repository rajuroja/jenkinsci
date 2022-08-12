package com.dpworld.masterdataapp.model.response;

import com.dpworld.persistence.entity.UploadDocumentType;

public class InbUploadDocumentResponse {

	private long id;

	private UploadDocumentType uploadDocumentTypeId;

	private String filePath;

	private Boolean isCancel;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

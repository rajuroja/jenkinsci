package com.dpworld.masterdataapp.model.request;

import com.dpworld.persistence.entity.Outbound;
import com.dpworld.persistence.entity.UploadDocumentType;

public class OutbUploadDocumentRequest {

	private long id;

	private Outbound outbound;

	private UploadDocumentType uploadDocumentTypeId;

	private String filePath;

	private Boolean isCancel;

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

}

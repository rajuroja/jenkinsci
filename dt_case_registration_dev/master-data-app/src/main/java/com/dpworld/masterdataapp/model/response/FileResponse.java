package com.dpworld.masterdataapp.model.response;

public class FileResponse {

	private String fileBase64;

	private String fileName;

	public FileResponse() {
		super();
	}

	public FileResponse(String fileBase64, String fileName) {
		super();
		this.fileBase64 = fileBase64;
		this.fileName = fileName;
	}

	public String getFileBase64() {
		return fileBase64;
	}

	public void setFileBase64(String fileBase64) {
		this.fileBase64 = fileBase64;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}

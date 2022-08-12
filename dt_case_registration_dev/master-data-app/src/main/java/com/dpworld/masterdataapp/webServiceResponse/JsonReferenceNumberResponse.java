package com.dpworld.masterdataapp.webServiceResponse;

public class JsonReferenceNumberResponse {

	private String sequnce_Number;
	private String status;

	public String getSequnce_Number() {
		return sequnce_Number;
	}

	public void setSequnce_Number(String sequnce_Number) {
		this.sequnce_Number = sequnce_Number;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "JsonReferenceNumberResponse [sequnce_Number=" + sequnce_Number + ", status=" + status + "]";
	}

}

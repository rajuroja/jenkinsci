package com.dpworld.masterdataapp.webServiceResponse;

public class JsonResponse {

	private String code;

	private String status;

	private String message;

	private JsonResponseData data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public JsonResponseData getData() {
		return data;
	}

	public void setData(JsonResponseData data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "JsonResponse [code=" + code + ", status=" + status + ", message=" + message + ", data=" + data + "]";
	}

}

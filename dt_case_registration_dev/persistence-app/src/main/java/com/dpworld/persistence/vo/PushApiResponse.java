package com.dpworld.persistence.vo;

import java.io.Serializable;
import java.util.List;

public class PushApiResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<String> messages;

	private int code;

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
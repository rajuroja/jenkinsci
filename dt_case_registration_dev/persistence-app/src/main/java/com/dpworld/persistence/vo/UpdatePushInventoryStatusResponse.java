package com.dpworld.persistence.vo;

import java.io.Serializable;
import java.util.List;

public class UpdatePushInventoryStatusResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<String> messages;

	private int successCount;

	private int failedCount;

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public int getFailedCount() {
		return failedCount;
	}

	public void setFailedCount(int failedCount) {
		this.failedCount = failedCount;
	}

}

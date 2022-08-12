package com.dpworld.validation.exceptionhandling.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

/**
 * @author INTECH Creative Services Pvt Ltd Custom Class to convert error
 *         message.
 *
 */
public class APIError {

	private HttpStatus status;
	private LocalDateTime timestamp;
	private String message;
	private String debugMessage;

	private List<String> errors;

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDebugMessage() {
		return debugMessage;
	}

	public void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	private APIError() {
		timestamp = LocalDateTime.now();
	}

	public APIError(HttpStatus status) {
		this();
		this.status = status;
	}

	public APIError(HttpStatus status, String message, Throwable ex) {
		this();
		this.status = status;
		this.message = message;
		this.debugMessage = ex.getLocalizedMessage();
	}

	public APIError(HttpStatus status, String debugMessage, List<String> errors) {
		super();
		this.status = status;
		this.debugMessage = debugMessage;
		this.errors = errors;
	}

}

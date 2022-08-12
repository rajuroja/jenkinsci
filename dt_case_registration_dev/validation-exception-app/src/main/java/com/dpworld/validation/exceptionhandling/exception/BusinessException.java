package com.dpworld.validation.exceptionhandling.exception;

/**
 * To be thrown when there is an issue with business validations. i.e. no crane
 * activity found. or response from third party API was null.
 * 
 * @author INTECH Creative Services Pvt Ltd
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = -2197741848813963310L;
	
	public final String errorLocation;

	public BusinessException(String errorLocation, final Throwable cause) {
		super(cause.getMessage(), cause);
		this.errorLocation = errorLocation;
	}

	public BusinessException(final String message) {
		super(message);
		errorLocation = "";
	}

	public BusinessException(final Throwable cause) {
		super(cause);
		errorLocation = cause.getLocalizedMessage();
	}
}

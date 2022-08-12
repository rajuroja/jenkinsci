package com.dpworld.validation.exceptionhandling.exception;

public class AuthenticationFailedException extends RuntimeException {

	private static final long serialVersionUID = 7290797320809019682L;

	public AuthenticationFailedException(String message) {
		super(message);
	}

	public AuthenticationFailedException(Throwable cause) {
		super(cause);
	}

}

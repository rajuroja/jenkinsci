package com.dpworld.validation.exceptionhandling.exception;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.dpworld.common.config.AppConstants;
import com.dpworld.common.model.ApiResponse;
import com.dpworld.validation.exceptionhandling.model.APIError;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(AuthenticationFailedException.class)
	protected ResponseEntity<Object> authenticationfailed(AuthenticationFailedException ex) {
		ApiResponse<?> apiError = new ApiResponse<>(false, HttpStatus.UNAUTHORIZED.name(), AppConstants.INVALID_CREDENTIALS_MESSAGE);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.ACCEPTED);
	}

	@ExceptionHandler(javax.validation.ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {

		final List<String> errors = new ArrayList<String>();
		for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage());
		}

		final APIError apiError = new APIError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
		return buildResponseEntity(apiError);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

		final List<String> errors = new ArrayList<String>();
		for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}
		final APIError apiError = new APIError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
		return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
	}

	@ExceptionHandler({ AccessDeniedException.class })
	public ResponseEntity<Object> handleAccessDenied(Exception ex, WebRequest request) {
		ApiResponse<?> apiError = new ApiResponse<>(false, HttpStatus.UNAUTHORIZED.name(), AppConstants.ACCESS_DENIED_MESSAGE);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.OK);
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
		ApiResponse<?> apiError = new ApiResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.name(), AppConstants.SERVER_ERROR_MESSAGE, ex);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.OK);
	}

	private ResponseEntity<Object> buildResponseEntity(APIError apiError) {
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

}

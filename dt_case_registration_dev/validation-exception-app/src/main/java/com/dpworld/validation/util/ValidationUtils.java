package com.dpworld.validation.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @author INTECH Creative Services Pvt Ltd This class is used for validation of
 *         Message Type request.
 *
 */
@Component
public class ValidationUtils {

	private static final Logger logger = LoggerFactory.getLogger(ValidationUtils.class);

	@Autowired
	private Validator pdaValidator;

	public boolean validateMessageRequest(Object message, String messageType) {
		logger.info("Inside validation");
		// Class validationGroupClass = this.getGroupClassFromMessageType(messageType);
		Set<ConstraintViolation<Object>> errorSet = pdaValidator.validate(message);
		if (!CollectionUtils.isEmpty(errorSet)) {
			throw new ConstraintViolationException(errorSet);
		}
		return true;
	}

}

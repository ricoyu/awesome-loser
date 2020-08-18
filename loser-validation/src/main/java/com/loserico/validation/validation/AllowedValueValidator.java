package com.loserico.validation.validation;

import com.loserico.validation.validation.annotation.AllowedValues;

import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 验证只接受给定的值
 * <p>
 * Copyright: Copyright (c) 2020-08-18 10:21
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class AllowedValueValidator implements ConstraintValidator<AllowedValues, String> {
	private String[] candidateValues = null;
	private String[] exceptValues = null;
	private boolean caseSensitive = false;
	private boolean mandatory = true;

	@Override
	public void initialize(AllowedValues constraintAnnotation) {
		candidateValues = constraintAnnotation.value();
		exceptValues = constraintAnnotation.except();
		caseSensitive = constraintAnnotation.caseSensitive();
		mandatory = constraintAnnotation.mandatory();

		checkIfValueConflict();
	}

	/**
	 * Allowed values cannot conflict with except values
	 */
	private void checkIfValueConflict() {
		if (candidateValues.length > 0 && exceptValues.length > 0) {
			throw new ConstraintDeclarationException("value and except cannot exist in both. ");
		}
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		if (null == value) {
			if (mandatory) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("Is required.").addConstraintViolation();
				return false;
			}
			return true;
		}

		for (int i = 0; i < exceptValues.length; i++) {
			boolean matchExceptValue = caseSensitive ? value.equals(exceptValues[i])
					: value.equalsIgnoreCase(exceptValues[i]);
			if (matchExceptValue) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("Is required.").addConstraintViolation();
				return false;
			}
		}
		if (exceptValues.length > 0) {
			return true;
		}

		for (int i = 0; i < candidateValues.length; i++) {
			boolean matchCandidateValue = caseSensitive ? value.equals(candidateValues[i])
					: value.equalsIgnoreCase(candidateValues[i]);
			if (matchCandidateValue) {
				return true;
			}
		}
		return false;
	}

}

package com.loserico.validation.validation;

import com.loserico.validation.validation.annotation.Past;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 验证日期是一个过去时 
 * <p>
 * Copyright: Copyright (c) 2020-08-18 10:13
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class PastValidator implements ConstraintValidator<Past, Object> {

	@Override
	public void initialize(Past constraintAnnotation) {
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		//不对null值做校验
		if (value == null) {
			return true;
		}

		//检查必须早于当前
		boolean isPast = true;
		if (value instanceof LocalDateTime) {
			isPast = ((LocalDateTime) value).isBefore(LocalDateTime.now());
		} else if (value instanceof LocalDate) {
			isPast = ((LocalDate) value).isBefore(LocalDate.now());
		} else if (value instanceof LocalTime) {
			isPast = ((LocalTime) value).isBefore(LocalTime.now());
		}

		return isPast;
	}

}

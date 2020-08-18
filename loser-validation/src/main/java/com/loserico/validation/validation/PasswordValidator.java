package com.loserico.validation.validation;


import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.validation.validation.annotation.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.loserico.common.lang.utils.StringUtils.equalTo;
import static java.util.Optional.ofNullable;

/**
 * 验证两次密码需一致
 * <p>
 * Copyright: Copyright (c) 2019-03-07 10:32
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class PasswordValidator implements ConstraintValidator<Password, Object> {

	private String passwordField;

	private String passwordRepeatField;

	private String message;

	@Override
	public void initialize(Password constraintAnnotation) {
		passwordField = constraintAnnotation.password();
		passwordRepeatField = constraintAnnotation.passwordRepeat();
		message = constraintAnnotation.message();
	}

	@Override
	public boolean isValid(Object bean, ConstraintValidatorContext context) {
		String password = ReflectionUtils.getFieldValue(passwordField, bean);
		String passwordRepeat = ReflectionUtils.getFieldValue(passwordRepeatField, bean);

		if (!equalTo(passwordRepeat, password)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(ofNullable(message).orElse("两次密码不一致"))
					.addPropertyNode(passwordField)
					.addConstraintViolation();
			return false;
		}

		return true;
	}

}

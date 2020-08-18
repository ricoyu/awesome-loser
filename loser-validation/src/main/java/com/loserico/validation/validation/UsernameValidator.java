package com.loserico.validation.validation;


import com.loserico.validation.validation.annotation.Username;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * 可以包含字母、数字、下划线、@符号，必须以字母或者数字开头
 * 
 * https://www.mkyong.com/regular-expressions/how-to-validate-username-with-regular-expression/
 * <p>
 * Copyright: Copyright (c) 2018-03-29 17:13
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class UsernameValidator implements ConstraintValidator<Username, String> {

	private static Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+[_@]*[a-zA-Z0-9]*$");

	@Override
	public void initialize(Username constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		//不做必填验证
		if (isBlank(value)) {
			return true;
		}

		Matcher matcher = pattern.matcher(value);
		if (matcher.matches()) {
			return value.length() >= 3 && value.length() <= 15;
		}

		return false;
	}
}

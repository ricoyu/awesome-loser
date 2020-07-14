package com.loserico.validation.validation;

import com.loserico.validation.validation.annotation.Mobile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * 手机号验证器 
 * <p>
 * Copyright: Copyright (c) 2019-10-14 13:52
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {

	private static Pattern pattern = Pattern
			.compile("^(0|86|17951)?(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$");

	@Override
	public void initialize(Mobile constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		//不做必填验证
		if (isBlank(value)) {
			return true;
		}

		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}

}

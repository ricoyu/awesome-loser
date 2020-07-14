package com.loserico.workbook.validation;

import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * <p>
 * Copyright: (C), 2019/12/14 10:55
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ValidatorTest {
	
	@Test
	public void testValidateParam() throws NoSuchMethodException {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		//获取校验器
		Validator validator = validatorFactory.getValidator();
		//获取校验方法参数的校验器
		ExecutableValidator executableValidator = validator.forExecutables();
		
		ParamValidateService paramValidateService = new ParamValidateService();
		//获取要校验的方法
		Method method = paramValidateService.getClass().getMethod("update", String.class);
		//传递校验参数的输入的参数
		Object[] params = new Object[]{null};
		Set<ConstraintViolation<ParamValidateService>> constraintViolations =
				executableValidator.validateParameters(paramValidateService, method, params);
		for(ConstraintViolation constraintViolation : constraintViolations) {
			System.out.println(constraintViolation.getMessage());
		}
	}
}

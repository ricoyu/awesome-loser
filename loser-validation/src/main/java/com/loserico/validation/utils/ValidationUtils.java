package com.loserico.validation.utils;

import com.loserico.common.lang.i18n.I18N;
import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.common.lang.utils.RegexUtils;
import com.loserico.common.lang.utils.RegexUtils.MsgTemplate;
import com.loserico.validation.bean.ErrorMessage;
import com.loserico.validation.exception.AbstractPropertyExistsException;
import com.loserico.validation.exception.SimpleBindingResult;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.loserico.validation.utils.Arrays.asArray;
import static java.util.stream.Collectors.*;

/**
 * Validation 帮助类
 * <p>
 * Copyright: Copyright (c) 2019-10-14 13:43
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class ValidationUtils {
	
	private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	
	/**
	 * 执行手工数据校验, 校验失败抛出ValidationException, 如果校验通过, 什么都不做
	 *
	 * @param instance
	 * @param <T>
	 */
	public static <T> void validate(T instance) throws BindException {
		Set<ConstraintViolation<T>> violations = factory.getValidator().validate(instance);
		throwBindExceptionIfNeed(violations);
	}
	
	public static ErrorMessage getErrorMessage(BindException e) {
		return getErrorMessage(e.getBindingResult());
	}
	
	public static ErrorMessage getErrorMessage(BindingResult bindingResult) {
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		List<ObjectError> globalErrors = bindingResult.getGlobalErrors();
		List<String[]> errors = new ArrayList<String[]>(fieldErrors.size() + globalErrors.size());
		String[] error;
		for (FieldError fieldError : fieldErrors) {
			MsgTemplate msgTemplate = RegexUtils.msgTemplate(fieldError.getDefaultMessage());
			error = new String[]{fieldError.getField(), toString(msgTemplate)};
			errors.add(error);
		}
		for (ObjectError objectError : globalErrors) {
			MsgTemplate msgTemplate = RegexUtils.msgTemplate(objectError.getDefaultMessage());
			error = new String[]{objectError.getObjectName(), toString(msgTemplate)};
			errors.add(error);
		}
		errors = errors.stream()
				.sorted((previous, next) -> {
					return previous[0].compareTo(next[0]);
				}).collect(toList());
		
		return new ErrorMessage(errors);
	}
	
	/**
	 * @param field
	 * @param messageTemplate
	 * @param args
	 * @return
	 */
	public static ErrorMessage getErrorMessage(String field, String messageTemplate, Object... args) {
		List<String[]> errors = new ArrayList<String[]>();
		String message = I18N.i18nMessage(messageTemplate, args);
		errors.add(new String[]{field, message});
		return new ErrorMessage(errors);
	}
	
	/**
	 * @param e
	 * @return
	 */
	public static ErrorMessage getErrorMessage(AbstractPropertyExistsException e) {
		List<String[]> errors = new ArrayList<String[]>();
		String message = I18N.i18nMessage(e.getMessageTemplate(), asArray(e.getTemplateParam()));
		errors.add(new String[]{e.getField(), message});
		return new ErrorMessage(errors);
	}
	
	/**
	 * 如果校验失败, 把校验失败信息封装到BindException里面
	 *
	 * @param violations
	 * @param <T>
	 */
	private static <T> void throwBindExceptionIfNeed(Set<ConstraintViolation<T>> violations) throws BindException {
		if (violations.isEmpty()) {
			return;
		}
		
		String objectName = null;
		for (ConstraintViolation constraintViolation : violations) {
			Object rootBean = ReflectionUtils.getFieldValue("rootBean", constraintViolation);
			objectName = rootBean == null ? "" : rootBean.getClass().getSimpleName();
			break;
		}
		
		SimpleBindingResult bindingResult = new SimpleBindingResult(objectName);
		
		for (ConstraintViolation constraintViolation : violations) {
			Object rootBean = ReflectionUtils.getFieldValue("rootBean", constraintViolation);
			PathImpl propertyPath = ReflectionUtils.getFieldValue("propertyPath", constraintViolation);
			String field = propertyPath.asString();
			Object rejectedValue = ReflectionUtils.getFieldValue("value", constraintViolation);
			String defaultMessage = ReflectionUtils.getFieldValue("interpolatedMessage", constraintViolation);
			/*
			 * 第四个参数boolean bindingFailure, false表示是一个validation error
			 */
			FieldError fieldError = new FieldError(objectName, field, rejectedValue, false, null, null, defaultMessage);
			bindingResult.add(fieldError);
		}
		throw new BindException(bindingResult);
	}
	
	private static String toString(MsgTemplate msgTemplate) {
		if (msgTemplate.isMsgTemplate()) {
			return I18N.i18nMessage(msgTemplate.getMsgTemplate());
		}
		
		return msgTemplate.getMsgTemplate();
	}
}

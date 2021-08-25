package com.loserico.workbook.utils;

import com.loserico.workbook.exception.ValidationException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * Copyright: Copyright (c) 2019/10/15 16:30
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public final class ValidationUtils {

	public static <T> void throwBindException(int rowNum, Set<ConstraintViolation<T>> violations){
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
		bindingResult.setRowNum(rowNum);

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
		throw new ValidationException(new BindException(bindingResult));
	}
	
	public static ErrorMessage getErrorMessage(BindingResult bindingResult) {
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		List<ObjectError> globalErrors = bindingResult.getGlobalErrors();
		List<String[]> errors = new ArrayList<String[]>(fieldErrors.size() + globalErrors.size());
		String[] error;
		for (FieldError fieldError : fieldErrors) {
			error = new String[] { fieldError.getField(), fieldError.getDefaultMessage() };
			errors.add(error);
		}
		for (ObjectError objectError : globalErrors) {
			error = new String[] { objectError.getObjectName(), objectError.getDefaultMessage() };
			errors.add(error);
		}
		errors = errors.stream()
				.sorted((previous, next) -> {
					return previous[0].compareTo(next[0]);
				}).collect(toList());
		
		if (bindingResult instanceof SimpleBindingResult) {
			Integer rowNum = ((SimpleBindingResult)bindingResult).getRowNum();
			return new ErrorMessage(rowNum, errors);
		}
		
		return new ErrorMessage(errors);
	}
	
	public static class SimpleBindingResult extends AbstractBindingResult {

		/**
		 * 行号
		 */
		private int rowNum;

		private Object target;

		private List<FieldError> fieldErrors = new ArrayList<>(1);

		public SimpleBindingResult(String objectName) { 
			super(objectName);
		}

		public SimpleBindingResult(String objectName, Object target, FieldError fieldError) {
			super(objectName);
			this.target = target;
			this.fieldErrors.add(fieldError);
		}

		public void add(FieldError fieldError) {
			this.fieldErrors.add(fieldError);
		}

		@Override
		public List<FieldError> getFieldErrors() {
			return this.fieldErrors;
		}

		@Override
		public Object getTarget() {
			return target;
		}

		@Override
		protected Object getActualFieldValue(String field) {
			return null;
		}

		public int getRowNum() {
			return rowNum;
		}

		public void setRowNum(int rowNum) {
			this.rowNum = rowNum;
		}
		
		public void setTarget(Object target) {
			this.target = target;
		}
		
		public void setFieldErrors(List<FieldError> fieldErrors) {
			this.fieldErrors = fieldErrors;
		}
	}
	
	public static class ErrorMessage {
		
		private Integer rowNum;
		
		private List<String[]> errors;
		
		public ErrorMessage() {
			this.errors = new ArrayList<String[]>();
		}
		
		public ErrorMessage(List<String[]> errors) {
			this.errors = errors;
		}
		
		public ErrorMessage(Integer rowNum, List<String[]> errors) {
			this.rowNum = rowNum;
			this.errors = errors;
		}
		
		public ErrorMessage(String[] error) {
			this(Collections.singletonList(error));
		}
		
		public ErrorMessage(String[]... errors) {
			this(Arrays.asList(errors));
		}
		
		public List<String[]> getErrors() {
			return errors;
		}
		
		public void setErrors(List<String[]> errors) {
			this.errors = errors;
		}
		
		public Integer getRowNum() {
			return rowNum;
		}
		
		public void setRowNum(Integer rowNum) {
			this.rowNum = rowNum;
		}
	}
}

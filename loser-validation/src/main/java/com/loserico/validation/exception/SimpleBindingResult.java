package com.loserico.validation.exception;

import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Copyright: Copyright (c) 2021-02-03 10:32
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class SimpleBindingResult extends AbstractBindingResult {
	
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
	
	public void setTarget(Object target) {
		this.target = target;
	}
	
	public void setFieldErrors(List<FieldError> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}
}

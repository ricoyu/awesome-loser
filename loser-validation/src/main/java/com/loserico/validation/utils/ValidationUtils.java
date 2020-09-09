package com.loserico.validation.utils;

import com.loserico.common.lang.i18n.I18N;
import com.loserico.common.lang.utils.RegexUtils;
import com.loserico.common.lang.utils.RegexUtils.MsgTemplate;
import com.loserico.validation.bean.ErrorMessage;
import com.loserico.validation.exception.AbstractPropertyExistsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

import static com.loserico.validation.utils.Arrays.asArray;
import static java.util.stream.Collectors.toList;

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
	
	private static String toString(MsgTemplate msgTemplate) {
		if (msgTemplate.isMsgTemplate()) {
			return I18N.i18nMessage(msgTemplate.getMsgTemplate());
		}
		
		return msgTemplate.getMsgTemplate();
	}
}

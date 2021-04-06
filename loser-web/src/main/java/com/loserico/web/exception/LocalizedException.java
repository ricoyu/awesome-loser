package com.loserico.web.exception;

import com.loserico.common.lang.utils.CollectionUtils;
import com.loserico.common.lang.errors.ErrorType;
import com.loserico.web.utils.MessageHelper;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 通用国际化的异常类
 * <p>
 * Copyright: Copyright (c) 2018-04-11 11:21
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class LocalizedException extends RuntimeException {

	private static final long serialVersionUID = -6377039158210405281L;

	private String statusCode = "500";

	private String messageTemplate;

	private List<Object> messageParams = new ArrayList<>();

	private String defaultMessage;
	
	public LocalizedException(ErrorType errorType) {
		this.statusCode = errorType.code();
		this.messageTemplate = errorType.msgTemplate();
		this.defaultMessage = errorType.message();
	}
	
	public LocalizedException(String statusCode, String messageTemplate) {
		this.statusCode = statusCode;
		this.messageTemplate = messageTemplate;
	}

	public LocalizedException(String statusCode, String messageTemplate, String defaultMesssage) {
		this.statusCode = statusCode;
		this.messageTemplate = messageTemplate;
		this.defaultMessage = defaultMesssage;
	}
	
	public LocalizedException(String[] datas) {
		this.statusCode = datas[0];
		this.messageTemplate = datas[1];
		this.defaultMessage = datas[2];
	}

	public LocalizedException(String statusCode, String messageTemplate, List<Object> messageParams) {
		this.statusCode = statusCode;
		this.messageTemplate = messageTemplate;
		this.messageParams = messageParams;
	}
	
	public LocalizedException(String statusCode, String messageTemplate, List<Object> messageParams,
			String defaultMesssage) {
		this.statusCode = statusCode;
		this.messageTemplate = messageTemplate;
		this.defaultMessage = defaultMesssage;
		this.messageParams = messageParams;
	}

	public LocalizedException(String statusCode, String messageTemplate, String defaultMesssage, Throwable cause) {
		super(cause);
		this.statusCode = statusCode;
		this.messageTemplate = messageTemplate;
		this.defaultMessage = defaultMesssage;
	}

	public LocalizedException(String statusCode, String messageTemplate, List<Object> messageParams,
			String defaultMesssage, Throwable cause) {
		super(cause);
		this.statusCode = statusCode;
		this.messageTemplate = messageTemplate;
		this.defaultMessage = defaultMesssage;
		this.messageParams = messageParams;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessageTemplate() {
		return messageTemplate;
	}

	public void setMessageTemplate(String messageTemplate) {
		this.messageTemplate = messageTemplate;
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}

	public void setDefaultMessage(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	@Override
	public String getLocalizedMessage() {
		if (isNotBlank(messageTemplate) && CollectionUtils.isNotEmpty(messageParams)) {
			return MessageHelper.getMessage(messageTemplate, messageParams);
		}
		if (isNotBlank(messageTemplate) && messageParams.isEmpty()) {
			return MessageHelper.getMessage(messageTemplate);
		}

		return defaultMessage;
	}

	@Override
	public String toString() {
		return getLocalizedMessage();
	}

	public List<Object> getMessageParams() {
		return messageParams;
	}

	public void setMessageParams(List<Object> messageParams) {
		this.messageParams = messageParams;
	}
}

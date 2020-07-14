package com.loserico.validation.exception;

import com.loserico.validation.bean.ErrorMessage;

/**
 * 手工验证时把验证信息填到
 * <p>
 * Copyright: Copyright (c) 2018-09-06 14:20
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class GeneralValidationException extends RuntimeException {

	private static final long serialVersionUID = 4997103221193653162L;
	
	private ErrorMessage errorMessage;

	public GeneralValidationException() {
	}

	public GeneralValidationException(ErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}

	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(ErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}

}

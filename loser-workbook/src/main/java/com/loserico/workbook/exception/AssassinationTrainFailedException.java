package com.loserico.workbook.exception;

/**
 *  
 * <p>
 * Copyright: Copyright (c) 2019-10-15 10:32
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class AssassinationTrainFailedException extends RuntimeException {

	public AssassinationTrainFailedException() {
		super();
	}

	public AssassinationTrainFailedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AssassinationTrainFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AssassinationTrainFailedException(String message) {
		super(message);
	}

	public AssassinationTrainFailedException(Throwable cause) {
		super(cause);
	}

}

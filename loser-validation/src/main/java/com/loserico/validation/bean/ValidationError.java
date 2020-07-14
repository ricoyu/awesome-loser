package com.loserico.validation.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 代表某个具体的验证错误
 * <p>
 * Copyright: Copyright (c) 2019-10-14 13:47
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Data
public class ValidationError implements Serializable {
	private String code;
	private String message;
}
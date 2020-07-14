package com.loserico.validation.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 验证错误详情, 包含错误出现在bean的哪个属性上, 不合法的值, 错误消息
 * <p>
 * Copyright: Copyright (c) 2019-10-14 13:48
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorDetail implements Serializable {

	private String errorMessage;
	private String rejectedValue;
	private String propertyPath;

}

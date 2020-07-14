package com.loserico.workbook.validation;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * Copyright: (C), 2019/12/14 10:58
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ParamValidateService {
	
	//校验参数
	public void update(@NotNull String userId) {
		System.out.println("通过校验");
	}
}

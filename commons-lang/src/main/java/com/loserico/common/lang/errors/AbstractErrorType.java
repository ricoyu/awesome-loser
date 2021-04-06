package com.loserico.common.lang.errors;

import com.loserico.common.lang.exception.InvalidCodeException;
import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.loserico.common.lang.utils.Assert.notNull;

/**
 * <p>
 * Copyright: (C), 2021-03-30 10:35
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
public abstract class AbstractErrorType implements ErrorType {
	
	/**
	 * 前3位为状态码的类别, 如200, 400, 401, 405, 500, 504<p>
	 * 第4,5位是模块代码, 如10, 11<p>
	 * 最后3位是自定义错误代码, 如001<p>
	 */
	private static final Pattern ERROR_CODE_PATTERN = Pattern.compile("^(\\d{3})(\\d{2})(\\d{3})$");
	
	/**
	 * 最后三位是自定义错误码
	 */
	private String code;
	
	/**
	 * 国际化消息模板
	 */
	private String msgTemplate;
	
	/**
	 * 错误类型描述信息
	 */
	private String msg;
	
	public AbstractErrorType(String code) {
		notNull(code, "代码不能为空");
		Matcher matcher = ERROR_CODE_PATTERN.matcher(code);
		if (!matcher.matches()) {
			throw new InvalidCodeException("错误码格式: 前3位类别码+2两位模块代码+3位自定义错误码");
		}
		this.code = code;
	}
	
	@Override
	public String code() {
		return code;
	}
	
	@Override
	public String message() {
		return this.msg;
	}
	
	@Override
	public void message(String message) {
		this.msg = message;
	}
	
	@Override
	public void msgTemplate(String msgTemplate) {
		this.msgTemplate = msgTemplate;
	}
	
	@Override
	public String msgTemplate() {
		return this.msgTemplate;
	}
}

package com.loserico.common.lang.errors;

/**
 * 标准错误对象接口, 建议将各种错误代码以实现本接口的enum形式提供
 * <p>
 * Copyright: Copyright (c) 2020-05-02 10:26
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public interface ErrorType {
	
	/**
	 * 推荐格式: 共7位数字<p>
	 * 前3位是状态码的类别, 如200, 400, 401, 405, 500, 504<p>
	 * 第4,5位是模块代码, 如10 用户模块, 20配置管理模块<p>
	 * 最后2位是自定义错误代码, 如01<p>
	 *
	 * @return String
	 */
	String code();
	
	/**
	 * 设置默认消息
	 *
	 * @param message
	 */
	default void message(String message) {
	}
	
	/**
	 * 返回默认消息
	 *
	 * @return
	 */
	default String message() {
		return null;
	}
	
	/**
	 * 设置国际化消息模板
	 * @param msgTemplate
	 */
	default void msgTemplate(String msgTemplate) {
	}
	
	/**
	 * 返回国际化消息模板
	 *
	 * @return
	 */
	default String msgTemplate() {
		return null;
	}
	
}

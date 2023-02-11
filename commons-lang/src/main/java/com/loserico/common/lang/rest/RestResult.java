package com.loserico.common.lang.rest;

import com.loserico.common.lang.vo.Page;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * REST通用输出结果封装
 * <p>
 * Copyright: Copyright (c) 2020-08-17 16:54
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Data
@Slf4j
public class RestResult<T> {
	
	/**
	 * 请求接口状态码, 0表示成功
	 */
	private String code = "0";
	
	/**
	 * 只有success或error两个值
	 */
	private String status;

	/**
	 * message表示在API调用失败的情况下详细的错误信息, 这个信息可以由客户端直接呈现给用户
	 * 调用成功则固定为OK；
	 * 数据校验失败时的验证错误页放到这个字段里面
	 */
	private Object message = "OK";
	
	/**
	 * 输出的调试信息
	 */
	//private Object debugMessage;
	
	/**
	 * 返回的数据
	 */
	private T data;
	
	/**
	 * 分页对象, 如果没有分页, 这个字段不会输出到json串中
	 */
	private Page page;
	
}

package com.loserico.common.lang.vo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.loserico.common.lang.i18n.I18N.i18nMessage;

/**
 * <blockquote><pre>
 * status    请求接口状态码
 * code      数据请求状态码
 * message   数据请求返回消息
 * data      单个数据类
 * results   数组类型
 *
 * 情况1  status==200&&code==0 的时候请求接口数据成功, 解析数据
 * 情况2  status!=200          直接捕获异常 exception
 * 情况3  status＝200 code!=0  弹框message给用户
 * </pre></blockquote>
 * <p>
 * Copyright: Copyright (c) 2019-10-14 15:54
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class Results {
	
	private Results() {
	}
	
	public static class Builder {
		private final Result result = new Result();
		
		/**
		 * 请求接口状态码
		 */
		private String code = "200";
		
		/**
		 * message表示在API调用失败的情况下详细的错误信息, 这个信息可以由客户端直接呈现给用户
		 * 调用成功则固定为OK；
		 */
		private Object message = "OK";
		
		/**
		 * 调试消息
		 */
		private Object debugMessage;
		
		/**
		 * 单个数据对象
		 */
		private Object data;
		
		/**
		 * 返回集合类型对象
		 */
		private Collection<?> results;
		
		private Page page;
		
		/**
		 * 设置status code和message
		 *
		 * @param code    设置请求状态代码
		 * @param message 设置返回消息描述
		 * @return
		 */
		public Builder status(String code, Object message) {
			this.code = code;
			this.message = message;
			return this;
		}
		
		public Builder debugMessage(Object debugMessage) {
			this.debugMessage = debugMessage;
			return this;
		}
		
		/**
		 * 设置返回数据
		 *
		 * @param data
		 * @return Result
		 */
		public Result result(Object data) {
			this.data = data;
			return build();
		}
		
		/**
		 * 设置返回数据, 数据是集合类型
		 *
		 * @param results
		 * @return Result
		 */
		public Result results(Collection<?> results) {
			this.results = results;
			return build();
		}
		
		/**
		 * 分页支持
		 *
		 * @param page
		 * @return Builder
		 */
		public Builder page(Page page) {
			this.page = page;
			return this;
		}
		
		public Result build() {
			result.setMessage(message);
			result.setDebugMessage(debugMessage);
			result.setCode(code);
			
			if (results != null) {
				Map<String, Object> resultMap = new HashMap<>();
				resultMap.put("items", results);
				if (page != null) {
					resultMap.put("page", page);
				}
				result.setData(resultMap);
			} else {
				result.setData(data);
			}
			return result;
		}
	}
	
	/**
	 * 设置status 200 OK
	 *
	 * @return Builder
	 */
	public static Builder success() {
		return new Builder();
	}
	
	/**
	 * code设为"-1"
	 *
	 * @param message
	 * @return
	 */
	public static Builder message(Object message) {
		return status("-1", message);
	}
	
	public static Builder status(String code, Object message) {
		Builder builder = new Builder();
		return builder.status(code, message);
	}
	
	public static Builder status(ErrorType errorType) {
		Builder builder = new Builder();
		return builder.status(errorType.getCode(), i18nMessage(errorType));
	}
	
}

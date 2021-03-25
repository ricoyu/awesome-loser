package com.loserico.common.lang.bean;

import lombok.Data;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * <p>
 * Copyright: (C), 2021-03-23 10:58
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
public class UrlParts {
	
	/**
	 * http://192.168.100.101:9200/rico/_mapping
	 * https://www.google.com/dir/1/2/search.html?arg=0-a&arg1=1-b&arg3-c#hash
	 * <p>
	 * 其中的 http, https 部分<p>
	 * 默认http
	 */
	private String scheme;
	
	/**
	 * 192.168.100.101, www.google.com
	 */
	private String host;
	
	/**
	 * 端口号, 默认80
	 */
	private Integer port;
	
	/**
	 * /rico/_mapping, /dir/1/2/search.html
	 */
	private String path;
	
	/**
	 * null, arg=0-a&arg1=1-b&arg3-c
	 */
	private String params;
	
	/**
	 * 将从URL中抽取的参数部分, 如arg=0-a&arg1=1-b&arg3-c<p>
	 * 转成一个Map形式返回
	 * @return
	 */
	public MultiMap paramMap() {
		MultiMap multiMap = new MultiValueMap();
		if (isBlank(params)) {
			return multiMap;
		}
		
		//不同参数间通过&符号分隔
		String[] paramArr = params.split("&");
		if (paramArr.length == 0) {
			return multiMap;
		}
		
		for (int i = 0; i < paramArr.length; i++) {
			String nameValue = paramArr[i];
			if (isNotBlank(nameValue)) {
				//每个参数都由 参数名=参数值 组成
				String[] paramNameValue = nameValue.split("=");
				if (paramNameValue.length == 2) {
					multiMap.put(paramNameValue[0], paramNameValue[1]);
				}
			}
		}
		
		return multiMap;
	}
	
}

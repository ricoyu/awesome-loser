package com.loserico.networking.constants;

/**
 * <p>
 * Copyright: (C), 2021-03-22 11:36
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class MediaType {
	
	public static final String APPLICATION_JSON = "application/json";
	
	/**
	 * 表单提交时默认的Content-Type
	 */
	public static final String APPLICATION_FORM = "application/x-www-form-urlencoded";
	
	/**
	 * 表单提交时上传文件的话, Content-Type必须是这个
	 */
	public static final String MULTIPART_FORM_DATA = "multipart/form-data";
}

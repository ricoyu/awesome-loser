package com.loserico.networking.builder;

import com.loserico.common.lang.transformer.Transformers;
import com.loserico.common.lang.utils.Assert;
import com.loserico.networking.enums.GrantType;
import com.loserico.networking.enums.Scope;
import com.loserico.networking.http.OAuth2Support;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

/**
 * 模拟表单提交
 * <p>
 * Copyright: Copyright (c) 2020-05-13 19:48
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class FormRequestBuilder extends AbstractRequestBuilder implements OAuth2Support {
	
	/**
	 * 表单提交时的一些表单参数放在这里
	 */
	private ConcurrentMap<String, Object> formData = new ConcurrentHashMap<>();
	
	/**
	 * Basic Authentication
	 * 设置请求头: Authorization, 值为: "Basic XXXX" 形式
	 *
	 * @param username
	 * @param password
	 */
	public FormRequestBuilder basicAuth(String username, String password) {
		super.basicAuth(username, password);
		return this;
	}
	
	@Override
	public FormRequestBuilder bearerAuth(String token) {
		super.bearerAuth(token);
		return this;
	}
	
	@Override
	public FormRequestBuilder addHeader(String headerName, Object headerValue) {
		super.addHeader(headerName, headerValue);
		return this;
	}
	
	/**
	 * 设置请求参数
	 *
	 * @param paramName
	 * @param paramValue
	 * @return FormRequestBuilder
	 */
	public FormRequestBuilder param(String paramName, String paramValue) {
		Assert.notNull(paramName, "Param name cannot be null");
		params.put(paramName, paramValue);
		return this;
	}
	
	/**
	 * 文件上传
	 *
	 * @param paramName
	 * @param file
	 * @return FormRequestBuilder
	 */
	public FormRequestBuilder file(String paramName, File file) {
		formData.put(paramName, file);
		return this;
	}
	
	/**
	 * 设置表单提交的数据
	 *
	 * @param paramName
	 * @param paramValue
	 * @return FormRequestBuilder
	 */
	public FormRequestBuilder formData(String paramName, Object paramValue) {
		formData.put(paramName, paramValue);
		return this;
	}
	
	/**
	 * 设置username请求参数
	 *
	 * @param username
	 * @return
	 */
	public FormRequestBuilder username(String username) {
		return param("username", username);
	}
	
	/**
	 * 设置password请求参数
	 *
	 * @param password
	 * @return FormRequestBuilder
	 */
	public FormRequestBuilder password(String password) {
		return param("password", password);
	}
	
	/**
	 * 同时设置username和password
	 *
	 * @param username
	 * @param password
	 * @return FormRequestBuilder
	 */
	public FormRequestBuilder userpwd(String username, String password) {
		username(username);
		password(password);
		return this;
	}
	
	/**
	 * 设置OAuth2的grant_type参数
	 *
	 * @param grantType
	 * @return OAuth2Support
	 */
	@Override
	public FormRequestBuilder grantType(GrantType grantType) {
		Assert.notNull(grantType, "grantType cannot be null");
		params.put("grant_type", grantType.toString());
		return this;
	}
	
	/**
	 * 设置OAuth2的scope参数
	 *
	 * @param scope
	 * @return FormRequestBuilder
	 */
	@Override
	public FormRequestBuilder scope(Scope scope) {
		Assert.notNull(scope, "scope cannot be null");
		params.put("scope", scope.toString());
		return this;
	}
	
	/**
	 * 设置返回结果类型
	 *
	 * @param responseType
	 * @return FormRequestBuilder
	 */
	public FormRequestBuilder responseType(Class responseType) {
		this.responseType = responseType;
		return this;
	}
	
	@Override
	public FormRequestBuilder onError(Consumer<Exception> errorCallback) {
		super.onError(errorCallback);
		return this;
	}
	
	@Override
	protected void addFormData(HttpEntityEnclosingRequestBase request) {
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		
		for (Entry<String, Object> entry : formData.entrySet()) {
			Object value = entry.getValue();
			//如果是上传文件
			if (value instanceof File) {
				FileBody fileBody = new FileBody((File) value, ContentType.DEFAULT_BINARY);
				builder.addPart(entry.getKey(), fileBody);
				//同时要修改Content-Type为multipart/form-data
				//request.setHeader(CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA);
				request.removeHeaders(CONTENT_TYPE);
				continue;
			}
			//普通表单数据
			ContentType.create("multipart/form-data", UTF_8);
			StringBody stringBody = new StringBody(Transformers.convert(value), ContentType.create("multipart/form-data", UTF_8));
			//StringBody stringBody = new StringBody(Transformers.convert(value), ContentType.MULTIPART_FORM_DATA);
			builder.addPart(entry.getKey(), stringBody);
		}
		
		HttpEntity httpEntity = builder.build();
		request.setEntity(httpEntity);
	}
}

package com.loserico.networking.builder;

import com.loserico.common.lang.transformer.Transformers;
import com.loserico.common.lang.utils.Assert;
import com.loserico.common.lang.utils.IOUtils;
import com.loserico.networking.constants.ContentTypes;
import com.loserico.networking.enums.GrantType;
import com.loserico.networking.enums.Scope;
import com.loserico.networking.http.OAuth2Support;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
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
	
	@Override
	public FormRequestBuilder addCookie(String name, String value) {
		BasicClientCookie cookie = new BasicClientCookie(name, value);
		cookie.setDomain("sexy-uncle.com");
		cookie.setPath("/");
		cookie.setAttribute(ClientCookie.DOMAIN_ATTR, "true");
		cookieStore.addCookie(cookie);
		return this;
	}
	
	@Override
	public FormRequestBuilder addCookie(String name, String value, String domain, String path) {
		BasicClientCookie cookie = new BasicClientCookie(name, value);
		cookie.setDomain(domain);
		cookie.setPath(path);
		cookieStore.addCookie(cookie);
		return this;
	}
	
	/**
	 * 设置请求参数
	 *
	 * @param paramName
	 * @param paramValue
	 * @return FormRequestBuilder
	 */
	public FormRequestBuilder param(String paramName, Object paramValue) {
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
	 * 文件上传
	 *
	 * @param paramName
	 * @param fullFilename
	 * @return FormRequestBuilder
	 */
	public FormRequestBuilder file(String paramName, String fullFilename) {
		formData.put(paramName, IOUtils.readFile(fullFilename));
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
	
	/**
	 * 返回结果是否以byte[]数组形式给出
	 * @param returnBytes
	 * @return JsonRequestBuilder
	 */
	public FormRequestBuilder returnBytes(boolean returnBytes) {
		this.returnBytes = returnBytes;
		return this;
	}
	
	@Override
	public FormRequestBuilder onError(Consumer<Exception> errorCallback) {
		super.onError(errorCallback);
		return this;
	}
	
	/**
	 * http.socket.timeout
	 * <p>
	 * 建立连接后, 传输数据的超时时间
	 * <p>
	 * 超时会抛出 java.net.SocketTimeoutException
	 * <p>
	 * The time waiting for data – after establishing the connection; maximum time of inactivity between two data packets
	 *
	 * @param timeout
	 * @param timeUnit
	 * @return FormRequestBuilder
	 */
	public FormRequestBuilder soTimeout(Integer timeout, TimeUnit timeUnit) {
		Objects.requireNonNull(timeout, "timeout cannot be null!");
		Objects.requireNonNull(timeUnit, "timeUnit cannot be null!");
		this.soTimeout = timeUnit.toMillis(timeout);
		;
		return this;
	}
	
	/**
	 * 从连接池中获取连接的超时时间, 在高负载情况下比较有必要设置
	 * <p>
	 * http.connection-manager.timeout
	 * <p>
	 * The time to wait for a connection from the connection manager/pool
	 *
	 * @param timeout
	 * @param timeUnit
	 * @return FormRequestBuilder
	 */
	public FormRequestBuilder connectionManagerTimeout(Integer timeout, TimeUnit timeUnit) {
		Objects.requireNonNull(timeout, "timeout cannot be null!");
		Objects.requireNonNull(timeUnit, "timeUnit cannot be null!");
		this.connectionManagerTimeout = timeUnit.toMillis(timeout);
		;
		return this;
	}
	
	/**
	 * 请求生命周期超时时间, 大致= connectionTimeout + soTimeout
	 *
	 * @param timeout
	 * @param timeUnit
	 * @return FormRequestBuilder
	 */
	public FormRequestBuilder timeout(Integer timeout, TimeUnit timeUnit) {
		Objects.requireNonNull(timeout, "timeout cannot be null!");
		Objects.requireNonNull(timeUnit, "timeUnit cannot be null!");
		this.timeout = timeUnit.toMillis(timeout);
		return this;
	}
	
	/**
	 * 请求超时重试次数
	 * <p>
	 * 如果发生了以下几种异常, 不会重试:
	 * <ul>
	 *     <li/>InterruptedIOException, SocketTimeoutException
	 *     <li/>UnknownHostException
	 *     <li/>ConnectException
	 *     <li/>SSLException
	 * </ul>
	 *
	 * @param retries
	 * @return FormRequestBuilder
	 */
	public FormRequestBuilder retries(Integer retries) {
		Objects.requireNonNull(retries, "retries cannot be null!");
		this.retries = retries;
		return this;
	}
	
	/**
	 * 如果接口是幂等的, 可以放心重试, 设为true, 否则设为false
	 * true if it's OK to retry non-idempotent requests that have been sent
	 *
	 * @param requestSentRetryEnabled
	 * @return FormRequestBuilder
	 */
	public FormRequestBuilder requestSentRetryEnabled(boolean requestSentRetryEnabled) {
		this.requestSentRetryEnabled = requestSentRetryEnabled;
		return this;
	}
	
	@Override
	protected void addFormData(HttpEntityEnclosingRequestBase request) {
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		
		/*
		 * 如果表单数据中有一项数据类型是File类型的, 就认为是文件上传, Content-Type是multipart/form-data
		 * 否则认为是普通的表单提交, Content-Type是 application/x-www-form-urlencoded
		 */
		boolean fileUpload = false;
		for (Entry<String, Object> entry : formData.entrySet()) {
			Object value = entry.getValue();
			//如果是上传文件
			if (value instanceof File) {
				fileUpload = true;
				break;
			}
		}
		
		if (fileUpload) {
			for (Entry<String, Object> entry : formData.entrySet()) {
				Object value = entry.getValue();
				//如果是上传文件
				if (value instanceof File) {
					FileBody fileBody = new FileBody((File) value, ContentType.DEFAULT_BINARY);
					builder.addPart(entry.getKey(), fileBody);
					request.removeHeaders(CONTENT_TYPE);
					continue;
				}
				//普通表单数据
				StringBody stringBody = new StringBody(Transformers.toString(value), ContentType.create(ContentTypes.MULTIPART_FORM_DATA, UTF_8));
				builder.addPart(entry.getKey(), stringBody);
			}
			
			HttpEntity httpEntity = builder.build();
			request.setEntity(httpEntity);
			return;
		}
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (Entry<String, Object> entry : formData.entrySet()) {
			Object value = entry.getValue();
			String paramName = entry.getKey();
			params.add(new BasicNameValuePair(paramName, Transformers.toString(value)));
		}
		
		try {
			request.setEntity(new UrlEncodedFormEntity(params));
		} catch (UnsupportedEncodingException e) {
			log.error("", e);
			throw new RuntimeException("", e);
		}
	}
	
}

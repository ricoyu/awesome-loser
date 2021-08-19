package com.loserico.networking.builder;

import com.loserico.networking.enums.HttpMethod;
import com.loserico.networking.enums.Scheme;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: (C), 2020/5/5 19:00
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class JsonRequestBuilder extends AbstractRequestBuilder {
	
	public JsonRequestBuilder url(String url) {
		super.url(url);
		return this;
	}
	
	public JsonRequestBuilder scheme(Scheme scheme) {
		super.scheme(scheme);
		return this;
	}
	
	public JsonRequestBuilder host(String host) {
		super.host(host);
		return this;
	}
	
	public JsonRequestBuilder port(int port) {
		super.port(port);
		return this;
	}
	
	public JsonRequestBuilder path(String path) {
		super.path(path);
		return this;
	}
	
	/**
	 * 设置 HTTP 请求方法
	 *
	 * @param method
	 * @return AbstractRequestBuilder
	 */
	public JsonRequestBuilder method(HttpMethod method) {
		super.method(method);
		return this;
	}
	
	/**
	 * 提交的json数据
	 */
	private String jsonBody;
	
	/**
	 * Basic Authentication
	 * 设置请求头: Authorization, 值为: "Basic XXXX" 形式
	 *
	 * @param username
	 * @param password
	 */
	public JsonRequestBuilder basicAuth(String username, String password) {
		super.basicAuth(username, password);
		return this;
	}
	
	/**
	 * 设置请求头: Authorization:Bearer XXXX <br/>
	 * token不需要加 "Bearer ", 这个会自动补全
	 *
	 * @param token
	 * @return
	 */
	@Override
	public JsonRequestBuilder bearerAuth(String token) {
		super.bearerAuth(token);
		return this;
	}
	
	/**
	 * 设置提交的json数据, 适用POST请求
	 *
	 * @param json
	 * @return JsonRequestBuilder
	 */
	public JsonRequestBuilder body(String json) {
		this.jsonBody = json;
		return this;
	}
	
	/**
	 * 设置返回结果类型
	 *
	 * @param responseType
	 * @return JsonRequestBuilder
	 */
	public JsonRequestBuilder responseType(Class responseType) {
		this.responseType = responseType;
		return this;
	}
	
	/**
	 * 返回结果是否以byte[]数组形式给出
	 *
	 * @param returnBytes
	 * @return JsonRequestBuilder
	 */
	public JsonRequestBuilder returnBytes(boolean returnBytes) {
		this.returnBytes = returnBytes;
		return this;
	}
	
	@Override
	public JsonRequestBuilder addHeader(String headerName, Object headerValue) {
		super.addHeader(headerName, headerValue);
		return this;
	}
	
	@Override
	public JsonRequestBuilder addParam(String paramName, Object paramValue) {
		super.addParam(paramName, paramValue);
		return this;
	}
	
	@Override
	public JsonRequestBuilder addCookie(String name, String value) {
		BasicClientCookie cookie = new BasicClientCookie(name, value);
		cookie.setPath("/");
		cookieStore.addCookie(cookie);
		return this;
	}
	
	@Override
	public JsonRequestBuilder addCookie(String name, String value, String domain, String path) {
		BasicClientCookie cookie = new BasicClientCookie(name, value);
		cookie.setDomain(domain);
		cookie.setPath("/");
		cookieStore.addCookie(cookie);
		return this;
	}
	
	@Override
	public JsonRequestBuilder onError(Consumer<Exception> errorCallback) {
		super.onError(errorCallback);
		return this;
	}
	
	/**
	 * http.connection.timeout
	 * <p>
	 * 与远程主机建立连接的超时时间
	 * <p>
	 * 超时会抛出org.apache.http.conn.ConnectTimeoutException
	 *
	 * @param timeout
	 * @param timeUnit
	 * @return JsonRequestBuilder
	 */
	public JsonRequestBuilder connectionTimeout(Integer timeout, TimeUnit timeUnit) {
		Objects.requireNonNull(timeout, "timeout cannot be null!");
		Objects.requireNonNull(timeUnit, "timeUnit cannot be null!");
		this.connectionTimeout = timeUnit.toMillis(timeout);
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
	 * @return JsonRequestBuilder
	 */
	public JsonRequestBuilder soTimeout(Integer timeout, TimeUnit timeUnit) {
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
	 * @return JsonRequestBuilder
	 */
	public JsonRequestBuilder connectionManagerTimeout(Integer timeout, TimeUnit timeUnit) {
		Objects.requireNonNull(timeout, "timeout cannot be null!");
		Objects.requireNonNull(timeUnit, "timeUnit cannot be null!");
		this.connectionManagerTimeout = timeUnit.toMillis(timeout);
		;
		return this;
	}
	
	/**
	 * 整个请求生命周期超时时间, 大致 = connectionTimeout + soTimeout
	 *
	 * @param timeout
	 * @param timeUnit
	 * @return JsonRequestBuilder
	 */
	public JsonRequestBuilder timeout(Integer timeout, TimeUnit timeUnit) {
		Objects.requireNonNull(timeout, "timeout cannot be null!");
		Objects.requireNonNull(timeUnit, "timeUnit cannot be null!");
		this.timeout = timeUnit.toMillis(timeout);
		return this;
	}
	
	/**
	 * 请求超时重试次数
	 * <p>
	 * 如果发生了以下几种异常, 不会重试
	 * <ul>
	 *     <li/>InterruptedIOException, SocketTimeoutException
	 *     <li/>UnknownHostException
	 *     <li/>ConnectException
	 *     <li/>SSLException
	 * </ul>
	 * 
	 * @param retries
	 * @return JsonRequestBuilder
	 */
	public JsonRequestBuilder retries(Integer retries) {
		Objects.requireNonNull(retries, "retries cannot be null!");
		this.retries = retries;
		return this;
	}
	
	/**
	 * 如果接口是幂等的, 可以放心重试, 设为true, 否则设为false
	 * true if it's OK to retry non-idempotent requests that have been sent
	 *
	 * @param requestSentRetryEnabled
	 * @return JsonRequestBuilder
	 */
	public JsonRequestBuilder requestSentRetryEnabled(boolean requestSentRetryEnabled) {
		this.requestSentRetryEnabled = requestSentRetryEnabled;
		return this;
	}
	
	@Override
	protected void addBody(HttpEntityEnclosingRequestBase request) {
		if (jsonBody != null) {
			request.setEntity(new StringEntity(jsonBody, UTF_8));
		}
	}
}

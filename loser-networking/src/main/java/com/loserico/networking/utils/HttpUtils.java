package com.loserico.networking.utils;

import com.loserico.networking.builder.FormRequestBuilder;
import com.loserico.networking.builder.JsonRequestBuilder;
import com.loserico.networking.constants.MediaType;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static com.loserico.networking.enums.HttpMethod.GET;
import static com.loserico.networking.enums.HttpMethod.POST;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

/**
 * 网络相关操作帮助类
 * <p>
 * Copyright: (C), 2019/12/25 10:49
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class HttpUtils {
	
	private static String EMPTY_STR = "";
	private static String UTF_8 = "UTF-8";
	
	/**
	 * 执行HTTP GET请求并返回结果, Content-Type默认application-json
	 *
	 * @param url
	 * @return JsonRequestBuilder
	 */
	public static JsonRequestBuilder get(String url) {
		JsonRequestBuilder requestBuilder = new JsonRequestBuilder();
		requestBuilder.method(GET);
		requestBuilder.url(url);
		requestBuilder.addHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON);
		return requestBuilder;
	}
	
	/**
	 * 执行HTTP GET请求并返回结果, Content-Type默认application-json
	 * @return JsonRequestBuilder
	 */
	public static JsonRequestBuilder get() {
		JsonRequestBuilder requestBuilder = new JsonRequestBuilder();
		requestBuilder.method(GET);
		requestBuilder.addHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON);
		return requestBuilder;
	}
	
	/**
	 * 执行HTTP POST请求并返回结果, Content-Type默认application-json
	 * @param url
	 * @return JsonRequestBuilder
	 */
	public static JsonRequestBuilder post(String url) {
		JsonRequestBuilder requestBuilder = new JsonRequestBuilder();
		requestBuilder.method(POST);
		requestBuilder.url(url);
		requestBuilder.addHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON);
		return requestBuilder;
	}
	
	/**
	 * 执行HTTP POST请求并返回结果, Content-Type默认application-json
	 * @return JsonRequestBuilder
	 */
	public static JsonRequestBuilder post() {
		JsonRequestBuilder requestBuilder = new JsonRequestBuilder();
		requestBuilder.method(POST);
		requestBuilder.addHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON);
		return requestBuilder;
	}
	
	/**
	 * 执行HTTP表单提交, Content-Type默认application/x-www-form-urlencoded
	 * @param url
	 * @return
	 */
	public static FormRequestBuilder form(String url) {
		FormRequestBuilder builder = new FormRequestBuilder();
		builder.url(url);
		builder.method(POST);
		builder.addHeader(CONTENT_TYPE, MediaType.APPLICATION_FORM);
		return builder;
	}
	
	/**
	 * HTTPS 请求调用
	 *
	 * @param requestUrl
	 * @param requestMethod
	 * @param outputStr
	 * @return String
	 */
	public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		try {
			
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod(requestMethod);
			conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			// 当outputStr不为null时向输出流写数据
			if (null != outputStr) {
				OutputStream outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			return buffer.toString();
		} catch (ConnectException ce) {
			log.error("连接超时：{}", ce);
		} catch (Exception e) {
			log.error("https请求异常：{}", e);
		}
		return null;
	}
	
	/**
	 * 绕过SSL验证
	 *
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sc = SSLContext.getInstance("SSLv3");
		
		// 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
		X509TrustManager trustManager = new X509TrustManager() {
			@Override
			public void checkClientTrusted(
					X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException {
			}
			
			@Override
			public void checkServerTrusted(
					X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException {
			}
			
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		
		sc.init(null, new TrustManager[]{trustManager}, null);
		return sc;
	}

}

package com.loserico.networking.utils;

import com.loserico.networking.exception.HttpRequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Copyright: (C), 2019/12/25 11:29
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class HttpClientUtils {
	
	private static String EMPTY_STR = "";
	private static String UTF_8 = "UTF-8";
	
	private static PoolingHttpClientConnectionManager connectionManager;
	
	static {
		if (connectionManager == null) {
			connectionManager = new PoolingHttpClientConnectionManager();
			connectionManager.setMaxTotal(50);// 整个连接池最大连接数
			connectionManager.setDefaultMaxPerRoute(5);// 每路由最大连接数, 默认值是2
		}
	}
	
	/**
	 * 通过连接池获取HttpClient
	 *
	 * @return CloseableHttpClient
	 */
	public static CloseableHttpClient getHttpClient() {
		return HttpClients.custom().setConnectionManager(connectionManager).build();
	}
	
	/**
	 * 执行HTTP GET请求并返回结果
	 *
	 * @param url
	 * @return String
	 */
	public static String httpGetRequest(String url) {
		HttpGet httpGet = new HttpGet(url);
		return getResult(httpGet);
	}
	
	/**
	 * 执行HTTP GET请求, 带上参数, 返回结果
	 *
	 * @param url
	 * @param params
	 * @return String
	 * @throws URISyntaxException
	 */
	public static String httpGetRequest(String url, Map<String, Object> params) throws URISyntaxException {
		URIBuilder ub = new URIBuilder();
		ub.setPath(url);
		
		List<NameValuePair> pairs = toNameValuePairs(params);
		ub.setParameters(pairs);
		
		HttpGet httpGet = new HttpGet(ub.build());
		return getResult(httpGet);
	}
	
	/**
	 * 执行HTTP GET请求, 带上请求头, 请求参数, 返回结果
	 *
	 * @param url
	 * @param headers
	 * @param params
	 * @return String
	 * @throws URISyntaxException
	 */
	public static String httpGetRequest(String url, Map<String, Object> headers,
	                                    Map<String, Object> params) throws URISyntaxException {
		URIBuilder ub = new URIBuilder();
		ub.setPath(url);
		
		List<NameValuePair> pairs = toNameValuePairs(params);
		ub.setParameters(pairs);
		
		HttpGet httpGet = new HttpGet(ub.build());
		for (Map.Entry<String, Object> param : headers.entrySet()) {
			httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
		}
		return getResult(httpGet);
	}
	
	/**
	 * 执行HTTP POST请求并返回结果
	 *
	 * @param url
	 * @return String
	 */
	public static String httpPostRequest(String url) {
		HttpPost httpPost = new HttpPost(url);
		return getResult(httpPost);
	}
	
	/**
	 * 执行HTTP POST请求, 带上参数, 返回结果
	 *
	 * @param url
	 * @param params
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public static String httpPostRequest(String url, Map<String, Object> params) throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> pairs = toNameValuePairs(params);
		httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
		return getResult(httpPost);
	}
	
	/**
	 * 执行HTTP POST请求, 带上请求头, 请求参数, 返回结果
	 *
	 * @param url
	 * @param headers
	 * @param params
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params)
			throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(url);
		
		for (Map.Entry<String, Object> param : headers.entrySet()) {
			httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
		}
		
		List<NameValuePair> pairs = toNameValuePairs(params);
		httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
		
		return getResult(httpPost);
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
	
	/**
	 * 处理Http请求
	 *
	 * @param request
	 * @return String
	 */
	private static String getResult(HttpRequestBase request) {
		// CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpClient httpClient = getHttpClient();
		try {
			CloseableHttpResponse response = httpClient.execute(request);
			// response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// long len = entity.getContentLength();// -1 表示长度未知
				String result = EntityUtils.toString(entity, "UTF-8");
				response.close();
				// httpClient.close();
				return result;
			}
		} catch (ClientProtocolException e) {
			log.error("", e);
			throw new HttpRequestException(e);
		} catch (IOException e) {
			log.error("", e);
			throw new HttpRequestException(e);
		}
		
		return EMPTY_STR;
	}
	
	/**
	 * 将Map中的参数名/值对转成HTTPClient的NameValuePair
	 *
	 * @param params
	 * @return List<NameValuePair>
	 */
	private static List<NameValuePair> toNameValuePairs(Map<String, Object> params) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (Map.Entry<String, Object> param : params.entrySet()) {
			pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
		}
		
		return pairs;
	}
}

package com.loserico.networking;

import com.loserico.common.lang.utils.IOUtils;
import com.loserico.json.jackson.JacksonUtils;
import com.loserico.json.jsonpath.JsonPathUtils;
import com.loserico.networking.enums.Scheme;
import com.loserico.networking.exception.HttpRequestException;
import com.loserico.networking.utils.HttpUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import static java.util.concurrent.TimeUnit.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2020/4/22 16:52
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class HttpUtilsTest {
	
	@Test
	public void testGet() {
		String url = "http://192.168.100.101:9200/rico/_mapping";
		String response = HttpUtils.get(url).request();
		System.out.println(response);
		
		String response2 = HttpUtils.get()
				.scheme(Scheme.HTTP)
				.host("192.168.100.101")
				.port(9200)
				.path("/rico/_mapping")
				.request();
		
		assertEquals(response, response2);
	}
	
	@Test
	public void testGetWithHeadersAndParams() {
		String response = HttpUtils.get("http://localhost:8080/hello-boot/hello?name=三少爷")
				.addParam("name", "俞雪华")
				.addParam("age", "25")
				.addHeader("origin", "https://www.baeldung.com")
				.addHeader("date", LocalDateTime.now())
				.basicAuth("ricoyu", "123456")
				.request();
		System.out.println(response);
	}
	
	@Test
	public void testPost() {
		String response = HttpUtils.post("http://localhost:8080/hello-boot/body?name=三少爷")
				.addParam("name", "俞雪华")
				.addParam("age", "25")
				.addHeader("origin", "https://www.baeldung.com")
				.addHeader("date", LocalDateTime.now())
				.body("This is a message from 三少爷: 叼~")
				.basicAuth("ricoyu", "123456")
				.request();
		System.out.println(response);
	}
	
	@Test
	public void testElasticAuth() {
		String response = HttpUtils.get("http://192.168.100.104:9200").request();
		assertThat(response).contains("401");
		System.out.println(response);
		
		response = HttpUtils.get("http://192.168.100.104:9200")
				.basicAuth("elastic", "123456")
				.request();
		assertThat(response).contains("version");
		System.out.println(response);
	}
	
	@Test
	public void testFormSubmit() {
		Object response = HttpUtils.form("http://localhost:8080/form-submit")
				.formData("switcher", true)
				.request();
		System.out.println(response);
	}
	
	@Test
	public void testFileUpload() {
		Object response = HttpUtils.form("http://localhost:8080/upload")
				.file("file", Paths.get("D:\\Software\\redis-5.0.8.tar.gz").toFile())
				.formData("name", "俞雪华")
				.formData("switcher", true)
				.request();
		System.out.println(response);
	}
	
	@Test
	public void testFormSubmit2() {
		String json = HttpUtils.get("http://10.10.26.22:8090/token/get").request();
		String token = JsonPathUtils.readNode(json, "token");
		Object result = HttpUtils.form("http://10.10.26.22:8090/tasks/create/file")
				.bearerAuth(token)
				.file("file", IOUtils.readFile("D://ThreatEventViewServiceImpl.class"))
				//.param("priority", 3)
				.formData("priority", 3)
				.request();
		System.out.println(result);
	}
	
	@Test
	public void testSwitch() {
		String json = HttpUtils.get("http://10.10.26.22:8090/token/get").request();
		String token = JsonPathUtils.readNode(json, "token");
		Object result = HttpUtils.form("http://10.10.26.22:8090/av/set/switch")
				.bearerAuth(token)
				.formData("switch", true)
				.request();
		System.out.println(result);
	}
	
	@Test
	public void testBasicAuth() {
		Object response = HttpUtils.get("http://localhost:8080/security")
				.request();
		System.out.println(response);
		response = HttpUtils.get("http://localhost:8080/security")
				.basicAuth("rico", "654321")
				.request();
		System.out.println(response);
	}
	
	@Test
	public void testSSLAcceptAll() {
		TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
		try {
			SSLContext sslContext = SSLContexts.custom()
					.loadTrustMaterial(null, acceptingTrustStrategy)
					.build();
			
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
			
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("https", sslsf)
					.register("http", new PlainConnectionSocketFactory())
					.build();
			
			BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);
			CloseableHttpClient httpClient = HttpClients.custom()
					.setSSLSocketFactory(sslsf)
					.setConnectionManager(connectionManager)
					.build();
			
			HttpEntity entity = httpClient.execute(new HttpGet("https://192.168.100.101:9200/_cat/nodeattrs?v")).getEntity();
			String response = EntityUtils.toString(entity);
			System.out.println(response);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Test
	public void testNodeAttr() {
		Object response = HttpUtils.get("https://192.168.100.101:9200/_cat/nodeattrs?v").request();
		System.out.println(JacksonUtils.toPrettyJson(response));
	}
	
	
	@Test
	public void testPostBody() {
		String response = HttpUtils.post("http://localhost:8081/body?name=三少爷")
				.addParam("name", "俞雪华")
				.addParam("age", "25")
				.addHeader("origin", "https://www.baeldung.com")
				.addHeader("date", LocalDateTime.now())
				.body("This is a message from 三少爷: 叼~")
				.basicAuth("ricoyu", "123456")
				.request();
		System.out.println(response);
	}
	
	@Test
	public void testAuth() {
		String responseJson = HttpUtils.get("http://localhost:8083/pic-code").request();
		String codeId = JsonPathUtils.readNode(responseJson, "$.data.codeId");
		
	}
	
	@Test
	public void testSetLocale() {
		HttpUtils.form("http://localhost:8080/login")
				.addCookie("lang", "en")
				.request();
	}
	
	@SneakyThrows
	@Test
	public void testGetOctetStream() {
		byte[] data = HttpUtils.get("http://localhost:8080/downloadFile")
				.bearerAuth("XwGnyZ5TWY1d-3jToBhTkA")
				.returnBytes(true)
				.request();
		System.out.println(data.length);
		IOUtils.write("D://a.zip", data);
	}
	
	@Test
	public void testTimeout() {
		byte[] data = null;
		try {
			data = HttpUtils.get("http://localhost:8080/downloadFile")
					.bearerAuth("XwGnyZ5TWY1d-3jToBhTkA")
					.returnBytes(true)
					.connectionManagerTimeout(5, SECONDS)
					.connectionTimeout(1, SECONDS)
					.soTimeout(2, SECONDS)
					.request();
		} catch (Exception e) {
			if (e instanceof ConnectTimeoutException) {
				log.error("连接超时", e);
			} else if (e instanceof SocketTimeoutException) {
				log.error("数据传输超时", e);
			}
		}
		
		if (data != null) {
			System.out.println(data.length);
			IOUtils.write("D://a.zip", data);
		}
		
		data = HttpUtils.get("http://localhost:8080/downloadFile")
				.bearerAuth("XwGnyZ5TWY1d-3jToBhTkA")
				.returnBytes(true)
				.request();
		
		
		if (data != null) {
			System.out.println(data.length);
			IOUtils.write("D://b.zip", data);
		}
	}
	
	@Test
	public void testRequestTimeout() {
		byte[] data = null;
		try {
			data = HttpUtils.get("http://localhost:8080/downloadFile")
					.bearerAuth("XwGnyZ5TWY1d-3jToBhTkA")
					.returnBytes(true)
					.connectionManagerTimeout(5, SECONDS)
					.connectionTimeout(6, SECONDS)
					.soTimeout(7, SECONDS)
					.timeout(1, SECONDS)
					.request();
		} catch (HttpRequestException e) {
			if (e.getCause() instanceof ConnectTimeoutException) {
				log.error("连接超时", e);
			} else if (e.getCause() instanceof SocketTimeoutException) {
				log.error("数据传输超时", e);
			} else if (e.getCause() instanceof SocketException) {
				log.error("请求超时被取消了", e);
			}
		}
		
		if (data != null) {
			System.out.println(data.length);
			IOUtils.write("D://a.zip", data);
		}
		
		data = HttpUtils.get("http://localhost:8080/downloadFile")
				.bearerAuth("XwGnyZ5TWY1d-3jToBhTkA")
				.returnBytes(true)
				.request();
		
		
		if (data != null) {
			System.out.println(data.length);
			IOUtils.write("D://b.zip", data);
		}
	}
	
	
	@Test
	public void testRequestTimeoutWithCallback() {
		byte[] data = null;
		data = HttpUtils.get("http://localhost:8080/downloadFile")
				.bearerAuth("XwGnyZ5TWY1d-3jToBhTkA")
				.returnBytes(true)
				.connectionManagerTimeout(5, SECONDS)
				.connectionTimeout(6, SECONDS)
				.soTimeout(7, SECONDS)
				.timeout(1, SECONDS)
				.onError((e) -> log.error("出错了, 调用回调函数", e))
				.request();
		
		
		if (data != null) {
			System.out.println(data.length);
			IOUtils.write("D://a.zip", data);
		}
		
		data = HttpUtils.get("http://localhost:8080/downloadFile")
				.bearerAuth("XwGnyZ5TWY1d-3jToBhTkA")
				.returnBytes(true)
				.request();
		
		
		if (data != null) {
			System.out.println(data.length);
			IOUtils.write("D://b.zip", data);
		}
	}
	
	@Test
	public void testRetry() {
		byte[] data = null;
		data = HttpUtils.get("http://localhost:8080/downloadFile")
				.bearerAuth("XwGnyZ5TWY1d-3jToBhTkA")
				.returnBytes(true)
				.connectionManagerTimeout(5, SECONDS)
				.connectionTimeout(6, SECONDS)
				.soTimeout(1, SECONDS)
				.onError((e) -> log.error("出错了, 调用回调函数", e))
				.retries(6)
				.requestSentRetryEnabled(false)
				.request();
	}
	
}

package com.loserico.networking;

import lombok.SneakyThrows;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2021-03-22 16:13
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class HttpClientTest {
	
	@SneakyThrows
	@Test
	public void testGivenGetRequestExecuted_whenAnalyzingTheResponse_thenCorrectStatusCode() {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		CloseableHttpResponse response = client.execute(new HttpGet("http://192.168.100.101:9200/rico/_mapping"));
		int statusCode = response.getStatusLine().getStatusCode();
		Assertions.assertThat(statusCode).isEqualTo(HttpStatus.SC_OK);
	}
	
	@SneakyThrows
	@Test(expected = SSLHandshakeException.class)
	public void testWhenHttpsUrlIsConsumed_thenException() {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String urlOverHttps = "https://172.16.0.63/login";
		HttpGet httpGet = new HttpGet(urlOverHttps);
		CloseableHttpResponse response = httpClient.execute(httpGet);
		assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
	}
	
	@SneakyThrows
	@Test
	public void testGivenAcceptingAllCertificates_whenHttpsUrlIsConsumed_thenOk() {
		TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
		SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
		SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
		
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("https", sslFactory)
				.register("http", new PlainConnectionSocketFactory())
				.build();
		
		BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);
		CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLSocketFactory(sslFactory)
				.setConnectionManager(connectionManager)
				.build();
		
		String urlOverHttps = "https://172.16.0.63/login";
		HttpGet httpGet = new HttpGet(urlOverHttps);
		CloseableHttpResponse response = httpClient.execute(httpGet);
		assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
		String result = EntityUtils.toString(response.getEntity(), "UTF-8");
		System.out.println(result);
	}
}

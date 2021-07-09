package com.loserico.httpclient;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>
 * Copyright: (C), 2021-07-08 13:43
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class HttpClientTimeoutTest {
	
	@SneakyThrows
	@Test
	public void testTimeout() {
		int timeout = 5;
		org.apache.http.client.config.RequestConfig config = org.apache.http.client.config.RequestConfig.custom()
				//http.connection.timeout the time to establish the connection with the remote host
				.setConnectTimeout(timeout * 1000) 
				//http.socket.timeout     the time waiting for data – after establishing the connection; maximum time of inactivity between two data packets
				.setSocketTimeout(timeout * 1000)
				//http.connection-manager.timeout  the time to wait for a connection from the connection manager/pool
				.setConnectionRequestTimeout(timeout * 1000)
				.build();
		
		HttpClientBuilder httpClientBuilder;
		CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		
		HttpGet httpGet = new HttpGet("http://localhost:8080/downloadFile");
		
		CloseableHttpResponse response = client.execute(httpGet);
		System.out.println("HTTP Status of response: " + response.getStatusLine().getStatusCode());
	}
	
	@SneakyThrows
	@Test
	public void testHardTimeout() {
		int timeout = 5;
		org.apache.http.client.config.RequestConfig config = org.apache.http.client.config.RequestConfig.custom()
				//http.connection.timeout the time to establish the connection with the remote host
				.setConnectTimeout(timeout * 1000)
				//http.socket.timeout     the time waiting for data – after establishing the connection; maximum time of inactivity between two data packets
				.setSocketTimeout(timeout * 1000)
				//http.connection-manager.timeout  the time to wait for a connection from the connection manager/pool
				.setConnectionRequestTimeout(timeout * 1000)
				.build();
		
		HttpClientBuilder httpClientBuilder;
		CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		
		HttpGet httpGet = new HttpGet("http://localhost:8080/downloadFile");
		
		int hardTimeout = 5;
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				if (httpGet != null) {
					log.warn("总超时阈值达到, 取消请求!");
					httpGet.abort();
				}
			}
		};
		new Timer(true).schedule(timerTask, 3 * 1000);
		
		CloseableHttpResponse response = client.execute(httpGet);
		System.out.println("HTTP Status of response: " + response.getStatusLine().getStatusCode());
	}
}

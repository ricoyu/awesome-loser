package com.loserico.networking;

import com.loserico.networking.enums.Scheme;
import com.loserico.networking.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.file.Paths;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
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
		Object response = HttpUtils.form("http://localhost:8080/upload")
				.file("file", Paths.get("D:\\Downloads\\redis-5.0.8.tar.gz").toFile())
				.formData("name", "俞雪华")
				.request();
		System.out.println(response);
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
}

package com.loserico.search;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2020-11-25 10:37
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ESFieldNameTest {
	
	private static RestHighLevelClient client = null;
	
	@BeforeClass
	@SneakyThrows
	public static void init() {
		RestClientBuilder restClientBuilder;
		client = new RestHighLevelClient(RestClient.builder(
				new HttpHost("192.168.100.104", 9200, "http")));
	}
	
	@Test
	public void test() {
	}
	
	@AfterClass
	@SneakyThrows
	public static void close() {
		client.close();
	}
	
}

package com.loserico.search.factory;

import com.loserico.common.lang.resource.PropertyReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * Copyright: (C), 2021-03-11 15:13
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class RestHighLevelClientFactory {
	
	private static final String CLUSTER_NAME = "cluster.name";
	
	private static final String COLON = ":";
	
	private static final int DEFAULT_PORT = 9300;
	
	public static RestHighLevelClient create() {
		/**
		 * 默认读取classpath下elastic.properties文件
		 */
		PropertyReader propertyReader = new PropertyReader("elastic");
		
		String clusterName = propertyReader.getString(CLUSTER_NAME);
		
		/*
		 * 读取Elasticsearch地址, 可以指定ip,ip, 也可以是ip:port,ip:port形式
		 */
		List<String> hosts = propertyReader.getStrList("elastic.hosts");
		HttpHost[] httpHosts = hosts.stream()
				.filter(Objects::nonNull)
				.map((host) -> {
					String[] hostAndPort = host.split(COLON);
					if (hostAndPort.length == 1) {
						return new HttpHost(hostAndPort[0], DEFAULT_PORT, "http");
					} else {
						return new HttpHost(hostAndPort[0], Integer.parseInt(hostAndPort[1]), "http");
					}
				}).toArray(length -> new HttpHost[length]);
		
		return new RestHighLevelClient(RestClient.builder(httpHosts));
	}
}

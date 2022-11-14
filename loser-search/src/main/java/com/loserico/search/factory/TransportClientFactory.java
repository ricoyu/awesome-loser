package com.loserico.search.factory;

import com.loserico.common.lang.resource.PropertyReader;
import com.loserico.search.exception.TransportClientInitException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * Copyright: (C), 2021-01-01 8:40
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class TransportClientFactory {
	
	private static final String CLUSTER_NAME = "cluster.name";
	private static final String USERNAME = "elastic.username";
	private static final String PASSWORD = "elastic.password";
	
	private static final String COLON = ":";
	
	private static final int DEFAULT_PORT = 9300;
	
	@SneakyThrows
	public static TransportClient create() {
		/**
		 * 默认读取classpath下elastic.properties文件
		 */
		PropertyReader propertyReader = new PropertyReader("elastic");
		
		String clusterName = propertyReader.getString(CLUSTER_NAME);
		String username = propertyReader.getString(USERNAME);
		String password = propertyReader.getString(PASSWORD);
		String keystorePath = propertyReader.getString("keystore.path");
		
		/*
		 * 读取Elasticsearch地址, 可以指定ip,ip, 也可以是ip:port,ip:port形式
		 */
		List<String> hosts = propertyReader.getStrList("elastic.hosts");
		TransportAddress[] transportAddresses = hosts.stream()
				.filter(Objects::nonNull)
				.map((host) -> {
					String[] hostAndPort = host.split(COLON);
					if (hostAndPort.length == 1) {
						try {
							return new TransportAddress(InetAddress.getByName(hostAndPort[0]), DEFAULT_PORT);
						} catch (UnknownHostException e) {
							log.error("", e);
							throw new TransportClientInitException(e);
						}
					} else {
						try {
							return new TransportAddress(InetAddress.getByName(hostAndPort[0]), Integer.parseInt(hostAndPort[1]));
						} catch (UnknownHostException e) {
							log.error("", e);
							throw new TransportClientInitException(e);
						}
					}
				}).toArray(length -> new TransportAddress[length]);
		
		/*
		 * 配置了用户名密码就表示Elasticsearch开启了x-pack身份认证
		 */
		boolean secured = username != null && password != null;
		
		Settings.Builder builder = Settings.builder();
		builder.put(CLUSTER_NAME, clusterName);
		
		Settings settings = null;
		
		if (secured) {
			builder.put("xpack.security.user", username + ":" + password)
					.put("xpack.security.transport.ssl.enabled", true)
					//.put("xpack.security.transport.ssl.keystore.password", keystorePassword)
					//.put("xpack.security.transport.ssl.truststore.password", truststorePassword)
					.put("xpack.security.transport.ssl.verification_mode", "certificate")
					.put("xpack.security.transport.ssl.keystore.path", keystorePath)
					.put("xpack.security.transport.ssl.truststore.path", keystorePath);
					//.put("xpack.security.http.ssl.keystore.path", certificatesPath+"/elastic-certificates.p12")
					//.put("xpack.security.http.ssl.truststore.path", certificatesPath+"/elastic-certificates.p12")
			settings = builder.build();
			return new PreBuiltXPackTransportClient(settings)
					.addTransportAddresses(transportAddresses);
		}
		
		settings = builder.build();
		return new PreBuiltTransportClient(settings)
				.addTransportAddresses(transportAddresses);
	}
}

package com.loserico.search;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.BeforeClass;
import org.junit.Test;

import static java.net.InetAddress.getByName;
import static org.assertj.core.api.Assertions.*;

/**
 * <p>
 * Copyright: (C), 2020-12-31 14:04
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class TransportClientTest {
	
	private static TransportClient transportClient;
	
	@BeforeClass
	@SneakyThrows
	public static void init() {
		transportClient = new PreBuiltTransportClient(Settings.EMPTY)
				.addTransportAddress(new InetSocketTransportAddress(getByName("172.16.0.63"), 29300));
/*
		transportClient = new PreBuiltTransportClient(Settings.EMPTY)
				.addTransportAddress(new TransportAddress(getByName("192.168.100.104"), 9300));
*/
	}
	
	@Test
	public void testTransportClientConnection() {
		assertThat(transportClient != null);
	}
}

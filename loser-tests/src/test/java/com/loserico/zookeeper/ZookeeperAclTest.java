package com.loserico.zookeeper;

import lombok.SneakyThrows;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.junit.Test;

import java.util.Date;

/**
 * <p>
 * Copyright: (C), 2021-03-09 8:25
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ZookeeperAclTest {
	
	@SneakyThrows
	@Test
	public void testGenerateSuperDigest() {
		String sid = DigestAuthenticationProvider.generateDigest("rico:123456");
		System.out.println(sid);
	}
	
	public static void main(String[] args) {
		System.out.println(new Date(1615171539000L));;
	}
}

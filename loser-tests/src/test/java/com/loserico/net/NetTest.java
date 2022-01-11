package com.loserico.net;

import lombok.SneakyThrows;
import org.junit.Test;

import java.net.InetAddress;

/**
 * <p>
 * Copyright: (C), 2021-12-14 9:05
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class NetTest {
	
	@SneakyThrows
	@Test
	public void test() {
		System.out.println(InetAddress.getLocalHost().getCanonicalHostName());
	}
}

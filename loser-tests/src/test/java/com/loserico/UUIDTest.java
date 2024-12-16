package com.loserico;

import org.junit.Test;

import java.util.UUID;

/**
 * <p>
 * Copyright: (C), 2024-01-12 15:16
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class UUIDTest {
	
	@Test
	public void test() {
		String uuid = UUID.randomUUID().toString();
		System.out.println(uuid.replaceAll("-", ""));
	}
}

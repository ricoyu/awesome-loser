package com.loserico.jvm;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Copyright: (C), 2020-08-14 9:05
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class HeapTest {
	
	private byte[] data = new byte[1024 * 100];
	
	@SneakyThrows
	public static void main(String[] args) {
		ArrayList<HeapTest> objects = new ArrayList<>();
		while (true) {
			objects.add(new HeapTest());
			TimeUnit.MILLISECONDS.sleep(100L);
		}
	}
}

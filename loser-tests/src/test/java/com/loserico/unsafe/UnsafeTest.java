package com.loserico.unsafe;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 * Copyright: (C), 2020-12-10 15:59
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class UnsafeTest {
	
	public static void main(String[] args) {
		System.out.println(ObjectMapper.class.getClassLoader());
		//System.out.println(LoserAbstractQueuedSynchronizer.class.getClassLoader());
	}
/*	@SneakyThrows
	@Test
	public void testGetUnsafe() {
		Field field = Unsafe.class.getDeclaredField("theUnsafe");
		field.setAccessible(true);
		Unsafe unsafe = (Unsafe) field.get(null);
		System.out.println(UnsafeTest.class.getClassLoader());
	}*/
	
}

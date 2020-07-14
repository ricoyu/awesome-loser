package com.loserico.jvm.oom;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * ‐Xms10M ‐Xmx10M ‐XX:+PrintGCDetails ‐XX:+HeapDumpOnOutOfMemoryError ‐XX:HeapDumpPath=D:\oomtest.dump
 * <p>
 * Copyright: (C), 2020-7-11 0011 21:10
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class OOMTest {
	
	public static List<Object> list = new ArrayList<>();
	
	public static void main(String[] args) {
		List<Object> list = new ArrayList<>();
		int i=0, j = 0;
		
		while (true) {
			list.add(new User(i++, UUID.randomUUID().toString()));
			new User(j--, UUID.randomUUID().toString());
		}
	}
}

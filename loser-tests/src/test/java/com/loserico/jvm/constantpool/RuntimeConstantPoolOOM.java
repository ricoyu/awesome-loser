package com.loserico.jvm.constantpool;

import java.util.ArrayList;
import java.util.List;

/**
 * 证明字符串常量池位置在哪里
 * VM Args -Xms10M -Xmx10m
 * <p>
 * 运行结果:
 * jdk7及以上: Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
 * jdk6:      Exception in thread "main" java.lang.OutOfMemoryError: PermGen space
 * <p>
 * Copyright: (C), 2020-08-19 8:55
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class RuntimeConstantPoolOOM {
	
	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < 100000000; i++) {
			for (int j = 0; j < 1000000; j++) {
				list.add(String.valueOf(i + j / 1000000).intern());
			}
		}
	}
}

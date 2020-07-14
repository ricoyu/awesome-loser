package com.loserico.jvm.jit;

import java.util.Random;

/**
 * 参数: -XX:+PrintCompilation -XX:-TieredCompilation (关闭分层编译)
 * <p>
 * Copyright: Copyright (c) 2019-08-21 11:10
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class JITDemo2 {

	private static Random random = new Random();

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		int count = 0, i = 0;
		while (i++ < 15000) {
			System.out.println(i);
			count += plus();
		}
		System.out.println("time cost : " + (System.currentTimeMillis() - start));
	}

	private static int plus() {
		return random.nextInt(10);
	}
}

package com.loserico.concurrent.lock;

import java.util.Vector;

/**
 * 打开/关闭偏向锁性能对比
 * <p>
 * Copyright: (C), 2019/11/18 14:50
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BiaseLocking {
	
	public static Vector<Integer> vector = new Vector<>();
	
	/**
	 * 测试方式
	 * 开启偏向锁: -XX:+UseBiasedLocking -XX:BiasedLockingStartupDelay=0
	 * 关闭偏向锁: -XX:-UseBiasedLocking
	 * 实测前者确实快不少, 最好成绩393
	 * 后者最好成绩616
	 * @param args
	 */
	public static void main(String[] args) {
		long begin = System.currentTimeMillis();
		int count = 0;
		int num = 0;
		while (count < 10000000) {
			vector.add(num);
			num = num + 5;
			count++;
		}
		long end = System.currentTimeMillis();
		System.out.println(end - begin);
	}
}

package com.loserico.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * <p>
 * Copyright: (C), 2019/11/22 10:16
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class AtomicIntegerArrayTest {
	
	static int[] value = new int[]{1, 2};
	
	static AtomicIntegerArray aiArray = new AtomicIntegerArray(value);
	
	/**
	 * AtomicIntegerArray会把原始数组克隆一下, 然后再克隆的数组里面操作, 所以原始数组不变
	 * @param args
	 */
	public static void main(String[] args) {
		aiArray.getAndSet(0, 3);
		System.out.println(aiArray.get(0));
		System.out.println(value[0]);
		if (aiArray.get(0) != value[0]) {
			System.out.println("是否相等");
		}
	}
}

package com.loserico.jvm;

/**
 * VM Args: -Xss164k
 * <p>
 * Copyright: Copyright (c) 2019-07-31 14:18
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class JavaVMStackSOF {

	private int stackLength = 1;

	public void stackLeak() {
		System.out.println("Stack length: " + stackLength);
		stackLength++;
		stackLeak();
	}

	public static void main(String[] args) {
		JavaVMStackSOF oom = new JavaVMStackSOF();
		try {
			oom.stackLeak();
		} catch (Throwable e) {
			System.out.println("Stack length: " + oom.stackLength);
			throw e;
		}
	}
}

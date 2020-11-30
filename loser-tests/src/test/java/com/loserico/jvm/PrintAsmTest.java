package com.loserico.jvm;

/**
 * <p>
 * Copyright: (C), 2020-11-16 20:09
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class PrintAsmTest {
	
	volatile boolean initFlag = false;
	
	public static void main(String[] args) {
		PrintAsmTest printAsmTest = new PrintAsmTest();
		
		Runnable task = () -> {
			printAsmTest.initFlag = true;
		};
		Thread t1 = new Thread(task, "t1");
		Thread t2 = new Thread(task, "t2");
		t1.start();
		t2.start();
	}
}

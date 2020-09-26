package com.loserico.nio;

import java.nio.ByteBuffer;

/**
 * 直接内存与堆内存的区别
 * <p>
 * 从程序运行结果看出直接内存申请较慢，但访问效率高。
 * 在java虚拟机实现上，本地IO会直接操作直接内存（直接内存=>系统调用=>硬盘/网卡），
 * 而非直接内存则需要二次拷贝（堆内存=>直接内存=>系统调用=>硬盘/网卡）。
 * <p>
 * Copyright: (C), 2020-09-18 10:25
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DirectMemoryTest {
	
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			/*heapAccess();
			directAccess();*/
			heapAllocate();
			directAllocate();
		}
	}
	
	public static void heapAccess() {
		long startTime = System.currentTimeMillis();
		//分配堆内存
		ByteBuffer buffer = ByteBuffer.allocate(1000);
		for (int i = 0; i < 100000; i++) {
			for (int j = 0; j < 200; j++) {
				buffer.putInt(j);
			}
			buffer.flip();
			for (int j = 0; j < 200; j++) {
				buffer.getInt(j);
			}
			buffer.clear();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("堆内存访问:" + (endTime - startTime));
	}
	
	public static void directAccess() {
		long startTime = System.currentTimeMillis();
		//分配直接内存
		ByteBuffer buffer = ByteBuffer.allocateDirect(1000);
		for (int i = 0; i < 100000; i++) {
			for (int j = 0; j < 200; j++) {
				buffer.putInt(j);
			}
			buffer.flip();
			for (int j = 0; j < 200; j++) {
				buffer.getInt(j);
			}
			buffer.clear();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("直接内存访问:" + (endTime - startTime));
	}
	
	public static void heapAllocate() {
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			ByteBuffer.allocate(1024);
		}
		long endTime = System.currentTimeMillis();
		System.out.println("堆内存申请:" + (endTime - startTime));
	}
	
	public static void directAllocate() {
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			ByteBuffer.allocateDirect(1024);
		}
		long endTime = System.currentTimeMillis();
		System.out.println("直接内存申请:" + (endTime - startTime));
	}
}

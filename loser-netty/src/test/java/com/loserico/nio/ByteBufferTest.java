package com.loserico.nio;

import com.loserico.common.lang.utils.ReflectionUtils;
import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.nio.ByteBuffer;

/**
 * <p>
 * Copyright: (C), 2023-10-31 11:37
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ByteBufferTest {
	
	public static void main(String[] args) {
		OperatingSystemMXBean osmxb = ManagementFactory.getOperatingSystemMXBean();
		/*
		 * 分配后打印的内存信息应该是非常接近的
		 */
		System.out.println("分配前内存信息: " + ReflectionUtils.invokeMethod(osmxb, "getFreePhysicalMemorySize"));
		//在堆上分配内存
		ByteBuffer buffer = ByteBuffer.allocate(200000);
		System.out.println("buffer = " + buffer);
		System.out.println("分配后内存信息: " + ReflectionUtils.invokeMethod(osmxb, "getFreePhysicalMemorySize"));
		
		
		/*
		 * 分配后打印的内存信息应该是非常接近的
		 */
		System.out.println("分配前内存信息: " + ReflectionUtils.invokeMethod(osmxb, "getFreePhysicalMemorySize"));
		//在直接内存上分配内存
		ByteBuffer directBuffer = ByteBuffer.allocateDirect(200000);
		System.out.println("buffer = " + buffer);
		System.out.println("分配后内存信息: " + ReflectionUtils.invokeMethod(osmxb, "getFreePhysicalMemorySize"));
	}
	
	@Test
	public void testByteBufferAllocateOnHeap() {
		OperatingSystemMXBean osmxb = ManagementFactory.getOperatingSystemMXBean();
		/*
		 * 分配后打印的内存信息应该是非常接近的
		 */
		System.out.println("分配前内存信息: " + ReflectionUtils.invokeMethod(osmxb, "getFreePhysicalMemorySize"));
		//在堆上分配内存
		ByteBuffer buffer = ByteBuffer.allocate(200000);
		System.out.println("buffer = " + buffer);
		System.out.println("分配后内存信息: " + ReflectionUtils.invokeMethod(osmxb, "getFreePhysicalMemorySize"));
		
		
		/*
		 * 分配后打印的内存信息应该是非常接近的
		 */
		System.out.println("分配前内存信息: " + ReflectionUtils.invokeMethod(osmxb, "getFreePhysicalMemorySize"));
		//在直接内存上分配内存
		ByteBuffer directBuffer = ByteBuffer.allocateDirect(200000);
		System.out.println("buffer = " + buffer);
		System.out.println("分配后内存信息: " + ReflectionUtils.invokeMethod(osmxb, "getFreePhysicalMemorySize"));
		
	}
	
	@Test
	public void testByteBufferFromByteArray() {
		byte[] bytes = new byte[1024];
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		System.out.println(buffer);
	}
}

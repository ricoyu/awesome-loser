package com.loserico.nio;

import java.nio.ByteBuffer;

public class ByteBufferDemo {
    
	public static void main(String[] args) {
		// 创建一个容量为 10 的 ByteBuffer
		ByteBuffer buffer = ByteBuffer.allocate(10);
		
		System.out.println("After creation:");
		printStatus(buffer);
		
		// 写模式：向 buffer 中写入数据
		buffer.put((byte) 1);
		buffer.put((byte) 2);
		buffer.put((byte) 3);
		
		System.out.println("After adding 3 elements:");
		printStatus(buffer);
		
		// 转换到读模式
		buffer.flip();
		
		System.out.println("After flipping to read mode:");
		printStatus(buffer);
		
		// 读模式：读取一个元素
		byte b = buffer.get();
		System.out.println("Read one byte: " + b);
		
		System.out.println("After reading one byte:");
		printStatus(buffer);
		
		// 设置 mark 在当前位置（位置1）
		buffer.mark();
		
		// 继续读取更多元素
		b = buffer.get();
		System.out.println("Read another byte: " + b);
		
		System.out.println("After reading another byte:");
		printStatus(buffer);
		
		// 重置到 mark 的位置
		buffer.reset();
		
		System.out.println("After resetting to mark:");
		printStatus(buffer);
		
		// 清空 buffer 准备重新写入
		buffer.clear();
		
		System.out.println("After clearing:");
		printStatus(buffer);
	}
	
	private static void printStatus(ByteBuffer buffer) {
		System.out.println("Position: " + buffer.position() +
				", Limit: " + buffer.limit() +
				", Capacity: " + buffer.capacity());
	}
}

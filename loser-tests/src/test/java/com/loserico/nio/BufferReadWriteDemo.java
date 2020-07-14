package com.loserico.nio;

import java.nio.ByteBuffer;

public class BufferReadWriteDemo {
	public static void main(String[] args) {
		ByteBuffer buffer = ByteBuffer.allocate(7);
		//Capacity = 7
		System.out.println("Capacity = " + buffer.capacity());
		//Limit = 7
		System.out.println("Limit = " + buffer.limit());
		//Position = 0
		System.out.println("Position = " + buffer.position());
		//Remaining = 7
		System.out.println("Remaining = " + buffer.remaining());
		buffer.put((byte) 10).put((byte) 20).put((byte) 30);
		//Capacity = 7
		System.out.println("Capacity = " + buffer.capacity());
		//Limit = 7
		System.out.println("Limit = " + buffer.limit());
		//Position = 3
		System.out.println("Position = " + buffer.position());
		//Remaining = 4
		System.out.println("Remaining = " + buffer.remaining());
		//10 20 30
		for (int i = 0; i < buffer.position(); i++) {
			System.out.println(buffer.get(i));
		}
	}
}
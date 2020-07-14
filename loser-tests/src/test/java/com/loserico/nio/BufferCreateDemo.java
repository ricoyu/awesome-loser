package com.loserico.nio;

import java.nio.ByteBuffer;

public class BufferCreateDemo {
	public static void main(String[] args) {
		ByteBuffer buffer1 = ByteBuffer.allocate(10);
		if (buffer1.hasArray()) {
			System.out.println("buffer1 array: " + buffer1.array());
			//Buffer1 array offset: 0
			System.out.println("Buffer1 array offset: " + buffer1.arrayOffset());
			//Capacity: 10
			System.out.println("Capacity: " + buffer1.capacity());
			//Limit: 10
			System.out.println("Limit: " + buffer1.limit());
			//Position: 0
			System.out.println("Position: " + buffer1.position());
			//Remaining: 10
			System.out.println("Remaining: " + buffer1.remaining());
			System.out.println();
		}
		byte[] bytes = new byte[200];
		ByteBuffer buffer2 = ByteBuffer.wrap(bytes);
		buffer2 = ByteBuffer.wrap(bytes, 10, 50);
		if (buffer2.hasArray()) {
			System.out.println("buffer2 array: " + buffer2.array());
			//Buffer2 array offset: 0
			System.out.println("Buffer2 array offset: " + buffer2.arrayOffset());
			//Capacity: 200
			System.out.println("Capacity: " + buffer2.capacity());
			//Limit: 60
			System.out.println("Limit: " + buffer2.limit());
			//Position: 10
			System.out.println("Position: " + buffer2.position());
			//Remaining: 50
			System.out.println("Remaining: " + buffer2.remaining());
		}
	}
}
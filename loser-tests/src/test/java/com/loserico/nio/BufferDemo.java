package com.loserico.nio;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class BufferDemo {
	
	public static void main(String[] args) {
		Buffer buffer = ByteBuffer.allocate(7);
		//Capacity: 7
		System.out.println("Capacity: " + buffer.capacity());
		//Limit: 7
		System.out.println("Limit: " + buffer.limit());
		//Position: 0
		System.out.println("Position: " + buffer.position());
		//Remaining: 7
		System.out.println("Remaining: " + buffer.remaining());
		System.out.println("Changing buffer limit to 5");
		buffer.limit(5);
		//Limit: 5
		System.out.println("Limit: " + buffer.limit());
		//Position: 0
		System.out.println("Position: " + buffer.position());
		//Remaining: 5
		System.out.println("Remaining: " + buffer.remaining());
		System.out.println("Changing buffer position to 3");
		buffer.position(3);
		//Position: 3
		System.out.println("Position: " + buffer.position());
		//Remaining: 2
		System.out.println("Remaining: " + buffer.remaining());
		//java.nio.HeapByteBuffer[pos=3 lim=5 cap=7]
		System.out.println(buffer);
	}
}
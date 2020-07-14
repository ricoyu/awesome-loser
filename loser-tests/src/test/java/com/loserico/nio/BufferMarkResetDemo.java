package com.loserico.nio;

import java.nio.ByteBuffer;

public class BufferMarkResetDemo {
	public static void main(String[] args) {
		ByteBuffer buffer = ByteBuffer.allocate(7);
		buffer.put((byte) 10).put((byte) 20).put((byte) 30).put((byte) 40);
		buffer.limit(4);
		buffer.position(1).mark().position(3);
		//40
		System.out.println(buffer.get());
		System.out.println();
		buffer.reset();
		//20 30 40
		while (buffer.hasRemaining())
			System.out.println(buffer.get());
	}
}
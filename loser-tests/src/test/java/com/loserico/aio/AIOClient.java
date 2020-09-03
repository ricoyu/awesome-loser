package com.loserico.aio;

import lombok.SneakyThrows;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * <p>
 * Copyright: (C), 2020-08-27 10:48
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class AIOClient {
	
	@SneakyThrows
	public static void main(String[] args) {
		AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
		socketChannel.connect(new InetSocketAddress("localhost", 9090)).get();
		socketChannel.write(ByteBuffer.wrap("HelloServer".getBytes()));
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		Integer length = socketChannel.read(buffer).get();
		if (length != -1) {
			System.out.println("客户单收到信息: " + new String(buffer.array(), 0, length));
		}
	}
}

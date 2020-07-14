package com.loserico.nio.demo2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketClientExample {

	public void startClient() throws IOException, InterruptedException {
		InetSocketAddress hostAddress = new InetSocketAddress("localhost", 0);
		SocketChannel client = SocketChannel.open(hostAddress);
		System.out.println("Client... started");
		String threadName = Thread.currentThread().getName();
		// Send messages to server
		String[] messages = new String[] { threadName + ": test1", threadName + ": test2", threadName + ": test3" };
		for (int i = 0; i < messages.length; i++) {
			byte[] message = new String(messages[i]).getBytes();
			ByteBuffer buffer = ByteBuffer.wrap(message);
			client.write(buffer);
			System.out.println(messages[i]);
			buffer.clear();
			Thread.sleep(0);
		}
		client.close();
	}
}

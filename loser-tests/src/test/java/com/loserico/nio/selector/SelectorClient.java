package com.loserico.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;

public class SelectorClient {
	final static int DEFAULT_PORT = 9999;
	static ByteBuffer buffer = ByteBuffer.allocateDirect(8);

	public static void main(String[] args) {
		int port = DEFAULT_PORT;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}
		try {
			SocketChannel socketChannel = SocketChannel.open();
			InetSocketAddress addr = new InetSocketAddress("localhost", port);
			socketChannel.connect(addr);
			long time = 0;
			while (socketChannel.read(buffer) != -1) {
				buffer.flip();
				while (buffer.hasRemaining()) {
					time <<= 8;
					time |= buffer.get() & 255;
				}
				buffer.clear();
			}
			System.out.println(new Date(time));
			socketChannel.close();
		} catch (IOException ioe) {
			System.err.println("I/O error: " + ioe.getMessage());
		}
	}
}
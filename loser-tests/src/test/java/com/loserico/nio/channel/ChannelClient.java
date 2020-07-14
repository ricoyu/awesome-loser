package com.loserico.nio.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ChannelClient {

	public static void main(String[] args) {
		try {
			// obtains a socket channel and configures it to be nonblocking.
			SocketChannel socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			InetSocketAddress addr = new InetSocketAddress("localhost", 9999);
			socketChannel.connect(addr);
			/*
			 * Because of the nonblocking status, it’s necessary to repeatedly invoke
			 * finishConnect() until this method returns true, which indicates a connection to
			 * the remote server application.
			 */
			while (!socketChannel.finishConnect()) {
				System.out.println("waiting to finish connection");
			}
			ByteBuffer buffer = ByteBuffer.allocate(200);
			while (socketChannel.read(buffer) >= 0) {
				buffer.flip();
				while (buffer.hasRemaining()) {
					System.out.print((char) buffer.get());
				}
				buffer.clear();
			}
			socketChannel.close();
		} catch (IOException ioe) {
			System.err.println("I/O error: " + ioe.getMessage());
		}
	}
}
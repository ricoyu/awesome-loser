package com.loserico.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class SelectorMultiServer {
	final static int DEFAULT_PORT = 9999;
	static ByteBuffer buffer = ByteBuffer.allocateDirect(8);

	public static void main(String[] args) throws IOException {
		int port = DEFAULT_PORT;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}
		System.out.println("Server starting ... listening on port 9999");
		ServerSocketChannel serverSocketChannel9999 = ServerSocketChannel.open();
		ServerSocket serverSocket9999 = serverSocketChannel9999.socket();
		serverSocket9999.bind(new InetSocketAddress(port));
		serverSocketChannel9999.configureBlocking(false);

		System.out.println("Server starting ... listening on port 9991");
		ServerSocketChannel serverSocketChannel9991 = ServerSocketChannel.open();
		ServerSocket serverSocket9991 = serverSocketChannel9991.socket();
		serverSocket9991.bind(new InetSocketAddress(9991));
		serverSocketChannel9991.configureBlocking(false);

		Selector selector = Selector.open();
		serverSocketChannel9999.register(selector, SelectionKey.OP_ACCEPT);
		serverSocketChannel9991.register(selector, SelectionKey.OP_ACCEPT, new Message("Server listening port 9991"));

		while (true) {
			int n = selector.select();
			if (n == 0) {
				continue;
			}
			Set<SelectionKey> keys = selector.selectedKeys();
			for (SelectionKey key : keys) {
				if (key.isAcceptable()) {
					ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
					Message message = (Message)key.attachment();
					if(message != null) {
						System.out.println("This is the message: " + message.getMessage());
					}
					SocketChannel socketChannel = serverSocketChannel.accept();
					if (socketChannel == null) {
						continue;
					}
					System.out.println("Receiving connection");
					buffer.clear();
					buffer.putLong(System.currentTimeMillis());
					buffer.flip();
					System.out.println("Writing current time");
					while (buffer.hasRemaining()) {
						socketChannel.write(buffer);
					}
					socketChannel.close();
				}
				keys.remove(key);
			}
		}
	}

	static class Message {
		private String message;

		public Message(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
}
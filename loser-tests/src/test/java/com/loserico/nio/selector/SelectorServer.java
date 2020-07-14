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

public class SelectorServer {
	final static int DEFAULT_PORT = 9999;
	static ByteBuffer buffer = ByteBuffer.allocateDirect(8);

	public static void main(String[] args) throws IOException {
		int port = DEFAULT_PORT;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}
		
		System.out.println("Server starting ... listening on port " + port);
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		ServerSocket serverSocket = serverSocketChannel.socket();
		serverSocket.bind(new InetSocketAddress(port));
		serverSocketChannel.configureBlocking(false);

		Selector selector = Selector.open();
		//ServerSocketChannel只接受ACCEPT？
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		while (true) {
			int n = selector.select();
			if (n == 0) {
				continue;
			}
			Set<SelectionKey> keys = selector.selectedKeys();
			for (SelectionKey key : keys) {
				if (key.isAcceptable()) {
					SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
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
}
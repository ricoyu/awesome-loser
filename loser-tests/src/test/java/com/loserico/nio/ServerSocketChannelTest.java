package com.loserico.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Java NIO ServerSocketChannel is a channel that can listen for incoming TCP
 * connections, just like a ServerSocket in standard Java Networking.
 * 
 * 
 * 
 * @author Rico Yu
 * @since 2016-12-12 10:40
 * @version 1.0
 *
 */
public class ServerSocketChannelTest {

	private static final Logger logger = LoggerFactory.getLogger(ServerSocketChannelTest.class);

	@Test
	public void testOpen() {
		try {
			// You open a ServerSocketChannel by calling the
			// ServerSocketChannel.open() method.
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.socket().bind(new InetSocketAddress(9999));

			/*
			 * Since you are typically not interested in listening just for a
			 * single connection, you call the accept() inside a while-loop.
			 * 
			 * Of course you would use some other stop-criteria than true inside
			 * the while-loop.
			 */
			while (true) {
				/*
				 * Listening for incoming connections is done by calling the
				 * ServerSocketChannel.accept() method. When the accept() method
				 * returns, it returns a SocketChannel with an incoming
				 * connection. Thus, the accept() method blocks until an
				 * incoming connection arrives.
				 */
				SocketChannel socketChannel = serverSocketChannel.accept();
				serverSocketChannel.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * A ServerSocketChannel can be set into non-blocking mode. In non-blocking
	 * mode the accept() method returns immediately, and may thus return null,
	 * if no incoming connection had arrived. Therefore you will have to check
	 * if the returned SocketChannel is null.
	 */
	@Test
	public void testNonBlockingMode() {
		try {
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.socket().bind(new InetSocketAddress(9999));
			serverSocketChannel.configureBlocking(false);

			while (true) {
				SocketChannel socketChannel = serverSocketChannel.accept();
				if (socketChannel != null) {
					// do something with socketChannel...
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}

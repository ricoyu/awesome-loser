package com.loserico.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

public class SelectorTest {

	@Test
	public void testSelector() throws IOException {
		ServerSocketChannel channel = ServerSocketChannel.open();
		channel.socket().bind(new InetSocketAddress(9999));
		channel.configureBlocking(false);

		Selector selector = Selector.open();
		SelectionKey selectionKey = channel.register(selector, SelectionKey.OP_ACCEPT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		while (true) {
			int numReadyChannels = selector.select();
			if (numReadyChannels == 0) {
				// there are no ready channels to process
				continue;
			}
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
			while (keyIterator.hasNext()) {
				SelectionKey key = keyIterator.next();
				if (key.isAcceptable()) {
					// A connection was accepted by a ServerSocketChannel.
					ServerSocketChannel server = (ServerSocketChannel) key.channel();
					SocketChannel client = server.accept();
					if (client == null) // in case accept() returns null
						continue;
					client.configureBlocking(false); // must be nonblocking
					// Register socket channel with selector for read operations.
					client.register(selector, SelectionKey.OP_READ);
				} else if (key.isReadable()) {
					// A socket channel is ready for reading.
					SocketChannel client = (SocketChannel) key.channel();
					// Perform work on the socket channel.
				} else if (key.isWritable()) {
					// A socket channel is ready for writing.
					SocketChannel client = (SocketChannel) key.channel();
					// Perform work on the socket channel.
				}
				keyIterator.remove();
			}
		}
	}
}

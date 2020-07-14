package com.loserico.aio.sscdemo.client;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;

/**
 * To put this into perspective, here is what happens when the client starts running.
 * 
 * 1. Client’s main() method sets isReadMode to false, stores a Hello message in the
 * buffer, and write()s the message to the channel, which results in the message being
 * sent to and displayed by the server.
 * 
 * 2. The server’s read/write handler write()s this message back to the client.
 * 
 * 3. Because isReadMode is false, Client’s read/write handler’s completed() method
 * executes the else part of the if-else statement, setting isReadMode to true and
 * calling read(), which stores in the buffer the Hello bytes written by the server.
 * 
 * 4. Client’s read/write handler’s completed() method is called. It notes that
 * isReadMode is true, extracts the bytes from the buffer, and outputs the equivalent
 * string as part of a Server responded message.
 * 
 * 5. The client will now obtain a message from the user and act accordingly.
 * 
 * @author Rico Yu
 * @since 2016-12-18 17:25
 * @version 1.0
 *
 */
public class Client {
	private final static int PORT = 9090;
	private final static String HOST = "192.168.1.7";
//	private final static String HOST = "architect7.loserico.com";

	/*
	 * main() method first attempts to open an asynchronous socket channel and connect
	 * it to the server at port 9090 on the localhost. It then calls
	 * AsynchronousSocketChannel’s SocketAddress getLocalAddress() method to return
	 * the socket address to which this channel’s socket (the local socket) is bound,
	 * and it then outputs a message stating that the client is connected at this
	 * address.
	 * 
	 * If an exception is thrown, the server outputs a message and terminates.
	 * Otherwise, it creates an Attachment object and initializes its fields in
	 * preparation for a write() method call.
	 * 
	 * An initial message is created and stored in a buffer, which is then written to
	 * the socket channel. The Attachment object and a newly-created ReadWriteHandler
	 * object (which responds to the write() call and performs additional write()s and
	 * read()s) are passed to write().
	 * 
	 * At this point, main()’s thread of execution blocks itself by calling
	 * Thread.join(). The only way to unblock this thread and return from main() is to
	 * interrupt the thread from another thread. ReadWriteHandler takes care of this
	 * task, as you will see later.
	 */
	public static void main(String[] args) {
		AsynchronousSocketChannel channel;
		try {
			channel = AsynchronousSocketChannel.open();
		} catch (IOException ioe) {
			System.err.println("Unable to open client socket channel");
			return;
		}
		try {
			channel.connect(new InetSocketAddress(HOST, PORT)).get();
			System.out.printf("Client at %s connected%n", channel.getLocalAddress());
		} catch (ExecutionException | InterruptedException eie) {
			System.err.println("Server not responding");
			return;
		} catch (IOException ioe) {
			System.err.println("Unable to obtain client socket channel's local address");
			return;
		}
		Attachment attachment = new Attachment();
		attachment.channel = channel;
		attachment.isReadMode = false;
		attachment.buffer = ByteBuffer.allocate(2048);
		attachment.mainThd = Thread.currentThread();
		byte[] data = "Hello".getBytes(UTF_8);
		attachment.buffer.put(data);
		attachment.buffer.flip();
		channel.write(attachment.buffer, attachment, new ReadWriteHandler());
		try {
			attachment.mainThd.join();
		} catch (InterruptedException ie) {
			System.out.println("Client terminating");
		}
	}
}
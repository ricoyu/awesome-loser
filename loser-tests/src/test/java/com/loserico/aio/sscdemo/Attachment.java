package com.loserico.aio.sscdemo;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

public class Attachment {
	/*
	 * The channelServer field stores the reference to the server socket channel that
	 * was created in the Server class’s main() method. It’s used by the connection
	 * handler to call AsynchronousServerSocketChannel’s accept() method.
	 */
	public AsynchronousServerSocketChannel channelServer;

	/*
	 * The channelClient field stores the reference to the socket channel that is
	 * passed to ConnectionHandler’s completed() method in response to accept()
	 * successfully accepting a client connection. It’s also used in this method to
	 * perform an initial read() call, and to perform read()/write() calls in the
	 * ReadWriteHandler object.
	 */
	public AsynchronousSocketChannel channelClient;

	/*
	 * The isReadMode field indicates that a read (true) or write (false) operation
	 * was performed before the ReadWriteHandler object’s completed() method was
	 * called. For a read, completed() obtains and outputs the read data. The
	 * completed() method ends by performing the opposite operation.
	 */
	public boolean isReadMode;

	/*
	 * The buffer field identifies the byte buffer that’s created in the connection
	 * handler and that’s used to communicate bytes between the server and a client.
	 * Each client has its own byte buffer.
	 */
	public ByteBuffer buffer;

	/*
	 * Finally, the clientAddr field stores the java.net.SocketAddress of the remote
	 * client. It stores the client’s socket address and is output as part of various
	 * client-specific messages.
	 */
	public SocketAddress clientAddr;
}
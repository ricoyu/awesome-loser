package com.loserico.aio.sscdemo.client;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class Attachment {
	/*
	 * The channel field identifies the asynchronous socket channel. It’s subsequently
	 * accessed by ReadWriteHandler’s completed() method to perform reads and writes.
	 */
	public AsynchronousSocketChannel channel;
	/*
	 * The isReadMode field is a toggle that lets completed() know if it needs to
	 * perform a read or a write.
	 */
	public boolean isReadMode;
	/*
	 * The buffer field identifies the byte buffer that’s created in Client’s main()
	 * method, and that’s used to communicate bytes between the server and a client.
	 * Each client has its own byte buffer.
	 */
	public ByteBuffer buffer;
	/*
	 * Finally, the mainThd field references a Thread object that identifies main()’s
	 * thread. The completed() method invokes interrupt() on this reference to
	 * interrupt the client so that this application can exit.
	 */
	public Thread mainThd;
}
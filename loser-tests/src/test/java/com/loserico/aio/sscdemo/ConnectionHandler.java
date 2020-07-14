package com.loserico.aio.sscdemo;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * ConnectionHandler is a CompletionHandler that responds to incoming connections. Its
 * completed() method is called when a connection is successful; otherwise, its
 * failed() method is called.
 * 
 * @author Rico Yu
 * @since 2016-12-18 16:03
 * @version 1.0
 *
 */
public class ConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, Attachment> {

	/*
	 * The completed() method’s first task is to obtain and output the client’s socket
	 * address for identification. I specified channelClient. getRemoteAddress()
	 * instead of channelClient.getLocalAddress() to return the client’s socket
	 * address because the socket corresponding to channelClient was created on the
	 * server side and it communicates with the client-side socket. Calling
	 * getLocalAddress() on channelClient returns the server-side socket address.
	 * Calling getRemoteAddress() on channelClient returns the client-side socket
	 * address.
	 * 
	 * Next, completed() calls accept() on the server socket channel with the passed
	 * Attachment object and a reference to the current completion handler as
	 * arguments. This call allows the server to respond to the next incoming
	 * connection.
	 * 
	 * The accept() method is called on the server socket channel referenced from the
	 * Attachment object passed to completed(). This same object is passed as the
	 * first argument to accept(). None of the other fields in the Attachment object
	 * have been initialized because they aren’t required. This Attachment object is
	 * only needed to identify the server socket channel.
	 * 
	 * After calling accept(), a second Attachment object is created and initialized
	 * in preparation for reading from the client. Also, a ReadWriteHandler object is
	 * created to respond to the read operation. The new Attachment object’s byte
	 * buffer, the new Attachment object, and the ReadWriteHandler object are passed
	 * as arguments to the client socket channel’s read() method.
	 */
	@Override
	public void completed(AsynchronousSocketChannel channelClient, Attachment attachment) {
		try {
			SocketAddress clientAddr = channelClient.getRemoteAddress();
			System.out.printf("Accepted connection from %s%n", clientAddr);
			attachment.channelServer.accept(attachment, this);
			Attachment newAttachment = new Attachment();
			newAttachment.channelServer = attachment.channelServer;
			newAttachment.channelClient = channelClient;
			newAttachment.isReadMode = true;
			newAttachment.buffer = ByteBuffer.allocate(2048);
			newAttachment.clientAddr = clientAddr;
			ReadWriteHandler rwh = new ReadWriteHandler();
			channelClient.read(newAttachment.buffer, newAttachment, rwh);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	@Override
	public void failed(Throwable t, Attachment attachment) {
		System.out.println("Failed to accept connection");
	}
}
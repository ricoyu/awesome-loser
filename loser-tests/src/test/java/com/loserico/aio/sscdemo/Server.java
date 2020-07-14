package com.loserico.aio.sscdemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

public class Server {
	
	private final static int PORT = 9090;
	private final static String HOST = "192.168.1.7";
//	private final static String HOST = "architect7.loserico.com";

	/*
	 * main() method first attempts to open an asynchronous server socket channel and
	 * then bind it to a local address, which happens to be port 9090 on the
	 * localhost. It then calls AsynchronousServerSocketChannel’s SocketAddress
	 * getLocalAddress() method to return the socket address to which this channel’s
	 * socket (the local socket) is bound. Finally, it outputs a message stating that
	 * the server is listening at this address.
	 * 
	 * If an exception is thrown, the server outputs a message and terminates.
	 * Otherwise, it creates an Attachment object and initializes the object’s
	 * channelServer field to the newly-opened asynchronous server socket channel.
	 * (For convenience, I directly access public fields instead of calling getter and
	 * setter methods.) This field will be accessed by the ConnectionHandler object
	 * that is subsequently created, and which is passed with the Attachment object to
	 * the <A> void accept(A attachment, Compl
	 * etionHandler<AsynchronousSocketChannel,? super A> handler) generic method.
	 * accept() listens for incoming connections and processes them accordingly via
	 * the ConnectionHandler object.
	 * 
	 * At this point, main()’s thread of execution blocks by calling Thread.join().
	 * The only way to unblock this thread and return from main() is to interrupt the
	 * thread from another thread. However, I have not implemented this feature.
	 * 
	 * The server can interact with multiple clients simultaneously via a single
	 * ConnectionHandler object and multiple ReadWriteHandler objects (one object per
	 * client). The Attachment class lets the connection and read/write handlers
	 * conveniently access the server socket channel. Also, it records client-specific
	 * details.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		AsynchronousServerSocketChannel channelServer;
		try {
			channelServer = AsynchronousServerSocketChannel.open();
			channelServer.bind(new InetSocketAddress(HOST, PORT));
			System.out.printf("Server listening at %s%n", channelServer.getLocalAddress());
		} catch (IOException ioe) {
			System.err.println("Unable to open or bind server socket channel");
			return;
		}
		Attachment attachment = new Attachment();
		attachment.channelServer = channelServer;
		channelServer.accept(attachment, new ConnectionHandler());
		try {
			Thread.currentThread().join();
		} catch (InterruptedException ie) {
			System.out.println("Server terminating");
		}
	}
}

package com.loserico.nio.server;

/**
 * NIO 通信服务端
 * <p>
 * Copyright: (C), 2023-10-31 16:30
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class NioServer {
	
	private static NioServerHandler nioServerHandler;
	
	public static void main(String[] args) {
		nioServerHandler = new NioServerHandler(8080);
		new Thread(nioServerHandler, "Server").start();
	}
}

package com.loserico.nio.blockingdemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * <p>
 * Copyright: (C), 2022-01-25 11:32
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Server {
	
	public static void main(String[] args) throws IOException {
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		//监听 8080 端口进来的 TCP 链接
		serverSocketChannel.socket().bind(new InetSocketAddress(8080));
		
		while (true) {
			// 这里会阻塞, 直到有一个请求的连接进来
			SocketChannel socketChannel = serverSocketChannel.accept();
			//开启一个新的线程来处理这个请求, 然后在 while 循环中继续监听 8080 端口
			SocketHandler socketHandler = new SocketHandler(socketChannel);
			new Thread(socketHandler).start();
		}
	}
}

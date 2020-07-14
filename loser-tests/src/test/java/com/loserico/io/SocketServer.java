package com.loserico.io;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <p>
 * Copyright: (C), 2019/12/21 9:35
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class  SocketServer {
	
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(8080);
		while (true) {
			System.out.println("等待连接...");
			//阻塞方法
			Socket socket = serverSocket.accept();
			
			System.out.println("有客户端连接了...");
			new Thread(() -> {
				try {
					handler(socket);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}).start();
		}
	}
	
	private static void handler(Socket socket) throws IOException {
		System.out.println("thread id = " + Thread.currentThread().getId());
		byte[] bytes = new byte[1024];
		
		System.out.println("准备read...");
		//接收客户端的数据, 阻塞方法, 没有数据可读时就阻塞
		int read = socket.getInputStream().read(bytes);
		System.out.println("read完毕...");
		
		if (read != -1) {
			System.out.println("接收到客户端的数据：" + new String(bytes, 0, read));
			System.out.println("thread id = " + Thread.currentThread().getId());
		}
		
		socket.getOutputStream().write("HelloClient".getBytes());
		socket.getOutputStream().flush();
	}
}

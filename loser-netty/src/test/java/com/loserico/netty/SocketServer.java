package com.loserico.netty;

import com.loserico.common.lang.utils.IOUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class  SocketServer {
	
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(8080);
		while (true) {
			System.out.println("等待连接...");
			//阻塞方法
			Socket socket = serverSocket.accept();
			
			System.out.println("有客户端连接了...");
			
			byte[] bytes = new byte[1024];
			
			System.out.println("准备read...");
			//接收客户端的数据, 阻塞方法, 没有数据可读时就阻塞
			int read = -1;
			while ((read = socket.getInputStream().read(bytes)) != -1) {
				System.out.println("接收到客户端的数据：" + new String(bytes, 0, read));
				System.out.println("thread id = " + Thread.currentThread().getId());
			}
			System.out.println("read完毕...");
			
			//socket.getOutputStream().write("HelloClient".getBytes());
			socket.getOutputStream().write(IOUtils.readFileAsBytes("d:/smart_meal_order.sql"));
			socket.getOutputStream().flush();
		}
	}
}
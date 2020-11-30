package com.loserico.netty;

import com.loserico.common.lang.utils.IOUtils;

import java.io.IOException;
import java.net.Socket;

public class SocketClient {
	
	public static void main(String[] args) throws IOException {
		Socket socket = new Socket("127.0.0.1", 8080);
		//向服务端发送数据
		//socket.getOutputStream().write("HelloServer".getBytes());
		socket.getOutputStream().write(IOUtils.readFileAsBytes("d:/smart_meal_order.sql"));
		socket.getOutputStream().flush();
		System.out.println("向服务端发送数据结束");
		
		byte[] bytes = new byte[1024];
		//接收服务端回传的数据
		int read = socket.getInputStream().read(bytes);
		if (read != -1) {
			System.out.println("接收到服务端的数据：" + new String(bytes));
			socket.close();
		}
	}
}
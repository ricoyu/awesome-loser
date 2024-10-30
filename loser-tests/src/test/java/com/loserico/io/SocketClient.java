package com.loserico.io;

import java.io.IOException;
import java.net.Socket;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: (C), 2019/12/21 13:43
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SocketClient {
	
	public static void main(String[] args) throws IOException {
		Socket socket = new Socket("127.0.0.1", 8080);
		//向服务端发送数据
		socket.getOutputStream().write("HelloServer".getBytes());
		socket.getOutputStream().flush();
		System.out.println("向服务端发送数据结束");
		
		byte[] bytes = new byte[1024];
		//接收服务端回传的数据
		int read = socket.getInputStream().read(bytes);
		if (read != -1) {
			System.out.println("接收到服务端的数据：" + new String(bytes, UTF_8));
			socket.close();
		}
	}
}

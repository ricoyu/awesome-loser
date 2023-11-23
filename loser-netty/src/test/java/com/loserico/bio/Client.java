package com.loserico.bio;

import lombok.SneakyThrows;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * <p>
 * Copyright: (C), 2023-10-30 11:18
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Client {
	
	@SneakyThrows
	public static void main(String[] args) {
		Socket socket = null;
		ObjectInputStream inputStream = null;
		ObjectOutputStream outputStream = null;
		InetSocketAddress address = new InetSocketAddress("192.168.100.11", 8888);
		try {
			socket = new Socket();
			socket.connect(address);
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.writeUTF("Rico");
			outputStream.flush();
			//TimeUnit.MILLISECONDS.sleep(200);
			inputStream = new ObjectInputStream(socket.getInputStream());
			String response = inputStream.readUTF();
			System.out.println("收到服务端响应: " + response);
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
			if (socket != null) {
				socket.close();
			}
		}
		
	}
}

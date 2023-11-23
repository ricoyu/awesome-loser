package com.loserico.bio;

import lombok.SneakyThrows;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BIO通信里面的服务端
 * <p>
 * Copyright: (C), 2023-10-30 10:51
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Server {
	
	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket();
		server.bind(new InetSocketAddress(8888));
		while (true) {
			System.out.println("等待客户端连接...");
			Socket socket = server.accept();
			System.out.println("客户端连接成功");
			new Thread(new ServerHandler(socket)).start();
		}
	}
	
	private static class ServerHandler implements Runnable {
		
		private Socket socket;
		
		public ServerHandler(Socket socket) {
			this.socket = socket;
		}
		
		@SneakyThrows
		@Override
		public void run() {
			try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			     ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream()) ) {
				String username = inputStream.readUTF();
				System.out.println("收到客户端消息: " + username);
				//MILLISECONDS.sleep(200);
				outputStream.writeUTF("Hello, " + username);
				outputStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			  
			}finally {
				socket.close();
			}
		}
	}
}

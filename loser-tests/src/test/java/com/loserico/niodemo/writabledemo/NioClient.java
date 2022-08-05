package com.loserico.niodemo.writabledemo;

import java.util.Scanner;

/**
 * <p>
 * Copyright: (C), 2022-08-04 16:41
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class NioClient {
	
	private static NioClientHandle nioClientHandle;
	
	public static void start() {
		nioClientHandle = new NioClientHandle("localhost", 12345);
		new Thread(nioClientHandle, "client").start();
	}
	
	//向服务器发送消息
	public static boolean sendMsg(String msg) throws Exception {
		nioClientHandle.sendMsg(msg);
		return true;
	}
	
	public static void main(String[] args) throws Exception {
		start();
		Scanner scanner = new Scanner(System.in);
		while (NioClient.sendMsg(scanner.next()));
	}
}

package com.loserico.nio.blockingdemo;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: (C), 2022-01-25 11:42
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class Client {
	
	public static void main(String[] args) throws IOException {
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress("localhost", 8080));
		
		//发送请求
		ByteBuffer byteBuffer = ByteBuffer.wrap("17666".getBytes(UTF_8));
		socketChannel.write(byteBuffer);
		
		//读取响应
		ByteBuffer readBuffer = ByteBuffer.allocate(1024);
		int num;
		if ((num = socketChannel.read(readBuffer)) > 0) {
			readBuffer.flip();
			
			byte[] data = new byte[num];
			readBuffer.get(data);
			
			String response = new String(data);
			log.info("服务器响应: {}", response);
		}
	}
}

package com.loserico.nio.blockingdemo;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: (C), 2022-01-25 11:36
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class SocketHandler implements Runnable {
	
	private SocketChannel socketChannel;
	
	public SocketHandler(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}
	
	@SneakyThrows
	@Override
	public void run() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		try {
			//将请求数据读入 Buffer 中
			int num;
			while ((num = socketChannel.read(buffer)) > 0) {
				//读取 Buffer 内容之前先 flip 一下
				buffer.flip();
				
				//提取 Buffer 中的数据
				byte[] bytes = new byte[num];
				buffer.get(bytes);
				
				String data = new String(bytes, UTF_8);
				log.info("收到数据: {}", data);
				
				//回应客户端
				ByteBuffer writeBuffer = ByteBuffer.wrap(("我已收到你的请求, 你请求的数据是 " + data).getBytes(UTF_8));
				socketChannel.write(writeBuffer);
			}
		} catch (IOException e) {
			log.error("", e);
		}
	}
}

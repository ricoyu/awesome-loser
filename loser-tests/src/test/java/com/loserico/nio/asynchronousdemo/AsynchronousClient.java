package com.loserico.nio.asynchronousdemo;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * <p>
 * Copyright: (C), 2022-01-25 15:59
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class AsynchronousClient {
	
	public static void main(String[] args) throws Exception {
		AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
		Future<Void> future = socketChannel.connect(new InetSocketAddress("localhost", 8080));
		//阻塞一下，等待连接成功
		future.get();
		
		Attachment attachment = new Attachment();
		attachment.setSocketChannel(socketChannel);
		attachment.setReadMode(false);
		attachment.setBuffer(ByteBuffer.allocate(2048));
		byte[] data = "I am rico!".getBytes(UTF_8);
		attachment.getBuffer().put(data);
		attachment.getBuffer().flip();
		
		//异步发送数据到服务端
		socketChannel.write(attachment.getBuffer(), attachment, new ClientChannelHandler());
		
		//这里休息一下再退出，给出足够的时间处理数据
		SECONDS.sleep(2);
	}
}

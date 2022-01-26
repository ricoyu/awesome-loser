package com.loserico.nio.asynchronousdemo;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * <p>
 * Copyright: (C), 2022-01-25 15:24
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class AsynchronousServer {
	
	public static void main(String[] args) throws IOException {
		// 实例化，并监听端口
		AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open()
				.bind(new InetSocketAddress("localhost", 8080));
		
		//AsynchronousChannelGroup group = AsynchronousChannelGroup.
		//		withFixedThreadPool(Runtime.getRuntime().availableProcessors(), Executors.defaultThreadFactory());
		//AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open(group)
		//		.bind(new InetSocketAddress("localhost", 8080));
		
		//自己定义一个 Attachment 类，用于传递一些信息
		Attachment attachment = new Attachment();
		attachment.setServerSocketChannel(serverSocketChannel);
		
		serverSocketChannel.accept(attachment, new CompletionHandler<AsynchronousSocketChannel, Attachment>() {
			@Override
			public void completed(AsynchronousSocketChannel socketChannel, Attachment attachment) {
				try {
					SocketAddress clientAddress = socketChannel.getRemoteAddress();
					log.info("收到新的连接: {}", clientAddress);
					
					// 收到新的连接后, server 应该重新调用 accept 方法等待新的连接进来
					attachment.getServerSocketChannel().accept(attachment, this);
					
					Attachment newAttachment = new Attachment();
					newAttachment.setServerSocketChannel(serverSocketChannel);
					newAttachment.setSocketChannel(socketChannel);
					newAttachment.setReadMode(true);
					newAttachment.setBuffer(ByteBuffer.allocate(1024));
					
					// 这里也可以继续使用匿名实现类
					socketChannel.read(newAttachment.getBuffer(), newAttachment, new ServerChannelHandler());
				} catch (IOException e) {
					log.error("", e);
				}
			}
			
			@Override
			public void failed(Throwable e, Attachment attachment) {
				log.error("Accept failed!", e);
			}
		});
		
		//防止 main 线程退出
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			log.error("", e);
		}
	}
}

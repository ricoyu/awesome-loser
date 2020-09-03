package com.loserico.aio;

import lombok.SneakyThrows;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * <p>
 * Copyright: (C), 2020-08-27 10:36
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class AIOServer {
	
	@SneakyThrows
	public static void main(String[] args) {
		final AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(9000));
		
		serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
			
			@SneakyThrows
			@Override
			public void completed(AsynchronousSocketChannel socketChannel, Object attachment) {
				//在此接收客户端连接，如果不写这行代码后面的客户端连接连不上服务端
				serverSocketChannel.accept(attachment, this);
				System.out.println(socketChannel.getRemoteAddress());
				
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				socketChannel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
					@Override
					public void completed(Integer result, ByteBuffer attachment) {
						buffer.flip();
						System.out.println(new String(buffer.array(), 0 , result));
						socketChannel.write(ByteBuffer.wrap("HelloClient".getBytes()));
					}
					
					@Override
					public void failed(Throwable exc, ByteBuffer attachment) {
						exc.printStackTrace();
					}
				});
			}
			
			@Override
			public void failed(Throwable exc, Object attachment) {
				
			}
		});
		
		Thread.currentThread().join();
	}
}
